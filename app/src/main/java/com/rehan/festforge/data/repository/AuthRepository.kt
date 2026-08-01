package com.rehan.festforge.data.repository

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.rehan.festforge.data.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    val currentUser: FirebaseUser? get() = auth.currentUser

    fun observeAuthState(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser) }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    fun sendOtp(
        activity: Activity,
        phoneNumber: String,
        onCodeSent: (String, PhoneAuthProvider.ForceResendingToken) -> Unit,
        onVerificationCompleted: (PhoneAuthCredential) -> Unit,
        onError: (String) -> Unit,
        resendToken: PhoneAuthProvider.ForceResendingToken? = null
    ) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) = onVerificationCompleted(credential)
            override fun onVerificationFailed(e: FirebaseException) = onError(e.localizedMessage ?: "Phone verification failed")
            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) = onCodeSent(id, token)
        }
        val builder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
        resendToken?.let(builder::setForceResendingToken)
        PhoneAuthProvider.verifyPhoneNumber(builder.build())
    }

    suspend fun signInWithCredential(credential: PhoneAuthCredential): FirebaseUser? =
        auth.signInWithCredential(credential).await().user

    suspend fun verifyCode(verificationId: String, code: String): FirebaseUser? =
        signInWithCredential(PhoneAuthProvider.getCredential(verificationId, code))

    suspend fun getUserProfile(userId: String): User? = try {
        firestore.collection("users").document(userId).get().await().toObject(User::class.java)
    } catch (_: Exception) { null }

    suspend fun createUserProfile(user: User): Boolean = try {
        firestore.collection("users").document(user.id).set(user).await(); true
    } catch (_: Exception) { false }

    fun signOut() = auth.signOut()
}
