package com.rehan.festforge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rehan.festforge.data.model.VerificationStatus
import com.rehan.festforge.data.model.WorkerProfile
import com.rehan.festforge.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WorkerOnboardingUiState(
    val category: String = "Waiter",
    val skills: String = "Banquet Service, Fine Dining",
    val city: String = "Mumbai",
    val experienceYears: String = "2",
    val hourlyRate: String = "250",
    val bio: String = "Professional event waiter with fine dining experience.",
    val verificationDocUri: String? = null,
    val isLoading: Boolean = false,
    val isSubmitted: Boolean = false,
    val errorMessage: String? = null
)

class WorkerOnboardingViewModel(
    private val workerRepository: WorkerRepository = WorkerRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkerOnboardingUiState())
    val uiState: StateFlow<WorkerOnboardingUiState> = _uiState.asStateFlow()

    fun updateFields(
        category: String = _uiState.value.category,
        skills: String = _uiState.value.skills,
        city: String = _uiState.value.city,
        exp: String = _uiState.value.experienceYears,
        rate: String = _uiState.value.hourlyRate,
        bio: String = _uiState.value.bio,
        docUri: String? = _uiState.value.verificationDocUri
    ) {
        _uiState.value = _uiState.value.copy(
            category = category,
            skills = skills,
            city = city,
            experienceYears = exp,
            hourlyRate = rate,
            bio = bio,
            verificationDocUri = docUri,
            errorMessage = null
        )
    }

    fun submitOnboarding(userId: String, name: String, phone: String, onComplete: () -> Unit) {
        val state = _uiState.value
        val exp = state.experienceYears.toIntOrNull()
        val rate = state.hourlyRate.toDoubleOrNull()

        if (exp == null || rate == null || state.verificationDocUri == null) {
            _uiState.value = state.copy(errorMessage = "Please complete all required fields and upload ID proof")
            return
        }

        val profile = WorkerProfile(
            id = userId,
            userId = userId,
            name = name,
            phone = phone,
            category = state.category,
            skills = state.skills.split(",").map { it.trim() },
            city = state.city,
            experienceYears = exp,
            hourlyRate = rate,
            bio = state.bio,
            verificationStatus = VerificationStatus.PENDING,
            verificationDocUrl = state.verificationDocUri,
            isAvailable = true
        )

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val success = workerRepository.saveWorkerProfile(profile)
            if (success) {
                _uiState.value = _uiState.value.copy(isLoading = false, isSubmitted = true)
                onComplete()
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "Failed to submit worker registration")
            }
        }
    }
}
