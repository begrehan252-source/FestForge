package com.rehan.festforge.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.rehan.festforge.data.model.User
import com.rehan.festforge.data.model.UserRole
import com.rehan.festforge.data.repository.AuthRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val phoneNumber: String = "", val otpCode: String = "", val verificationId: String? = null,
    val isLoading: Boolean = false, val errorMessage: String? = null, val isAuthenticated: Boolean = false,
    val userProfile: User? = null, val needsRegistration: Boolean = false, val timerSeconds: Int = 0
)

class AuthViewModel(private val authRepository: AuthRepository = AuthRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var timerJob: Job? = null

    init { checkSession() }

    fun checkSession() {
        val firebaseUser = authRepository.currentUser ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val profile = authRepository.getUserProfile(firebaseUser.uid)
            _uiState.value = _uiState.value.copy(isAuthenticated = true, userProfile = profile,
                needsRegistration = profile == null, isLoading = false)
        }
    }

    fun onPhoneNumberChange(number: String) {
        val digits = number.filter(Char::isDigit).take(10)
        _uiState.value = _uiState.value.copy(phoneNumber = digits, errorMessage = null)
    }
    fun onOtpCodeChange(otp: String) {
        _uiState.value = _uiState.value.copy(otpCode = otp.filter(Char::isDigit).take(6), errorMessage = null)
    }

    fun sendOtp(activity: Activity, onSuccess: () -> Unit, forceResend: Boolean = false) {
        val phone = _uiState.value.phoneNumber
        if (phone.length != 10) { _uiState.value = _uiState.value.copy(errorMessage = "Please enter a valid 10-digit mobile number"); return }
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        authRepository.sendOtp(activity, "+91$phone",
            onCodeSent = { id, token ->
                resendToken = token
                _uiState.value = _uiState.value.copy(verificationId = id, isLoading = false)
                startTimer(); onSuccess()
            },
            onVerificationCompleted = { credential -> signInCredential(credential, onSuccess = {}) },
            onError = { message -> _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = message) },
            resendToken = if (forceResend) resendToken else null
        )
    }

    fun verifyOtp(onSuccess: (User?) -> Unit) {
        val id = _uiState.value.verificationId
        val code = _uiState.value.otpCode
        if (id.isNullOrBlank() || code.length != 6) { _uiState.value = _uiState.value.copy(errorMessage = "Enter a valid 6-digit OTP"); return }
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                val firebaseUser = authRepository.verifyCode(id, code) ?: error("Authentication failed")
                finishFirebaseSignIn(firebaseUser.uid, onSuccess)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.localizedMessage ?: "Invalid or expired OTP")
            }
        }
    }

    private fun signInCredential(credential: PhoneAuthCredential, onSuccess: (User?) -> Unit) {
        viewModelScope.launch {
            try { val u = authRepository.signInWithCredential(credential) ?: return@launch; finishFirebaseSignIn(u.uid, onSuccess) }
            catch (e: Exception) { _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.localizedMessage) }
        }
    }
    private suspend fun finishFirebaseSignIn(uid: String, onSuccess: (User?) -> Unit) {
        val profile = authRepository.getUserProfile(uid)
        _uiState.value = _uiState.value.copy(isAuthenticated = true, userProfile = profile, needsRegistration = profile == null, isLoading = false)
        onSuccess(profile)
    }
    private fun startTimer() {
        timerJob?.cancel(); timerJob = viewModelScope.launch { for (s in 60 downTo 0) { _uiState.value = _uiState.value.copy(timerSeconds = s); delay(1000) } }
    }

    fun registerUser(name: String, city: String, role: UserRole, onComplete: (User) -> Unit) {
        if (name.isBlank() || city.isBlank()) { _uiState.value = _uiState.value.copy(errorMessage = "Name and city are required"); return }
        if (role == UserRole.ADMIN) { _uiState.value = _uiState.value.copy(errorMessage = "Admin status cannot be self-assigned"); return }
        val firebaseUser = authRepository.currentUser ?: run { _uiState.value = _uiState.value.copy(errorMessage = "Please verify your phone number first"); return }
        val newUser = User(id = firebaseUser.uid, phone = firebaseUser.phoneNumber ?: "+91${_uiState.value.phoneNumber}", name = name, role = role, city = city, createdAt = System.currentTimeMillis())
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            if (authRepository.createUserProfile(newUser)) {
                _uiState.value = _uiState.value.copy(userProfile = newUser, needsRegistration = false, isAuthenticated = true, isLoading = false); onComplete(newUser)
            } else _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "Could not save profile")
        }
    }
    fun logout() { authRepository.signOut(); _uiState.value = AuthUiState() }
}
