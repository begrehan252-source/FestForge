package com.rehan.festforge.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.rehan.festforge.data.model.Booking
import com.rehan.festforge.data.model.User
import com.rehan.festforge.data.model.VerificationStatus
import com.rehan.festforge.data.model.WorkerProfile
import kotlinx.coroutines.tasks.await

class AdminRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun getPendingWorkerVerifications(): List<WorkerProfile> {
        return try {
            val snapshot = firestore.collection("workers")
                .whereEqualTo("verificationStatus", VerificationStatus.PENDING.name)
                .get()
                .await()
            snapshot.toObjects(WorkerProfile::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateWorkerVerificationStatus(
        workerId: String,
        status: VerificationStatus
    ): Boolean {
        return try {
            firestore.collection("workers").document(workerId)
                .update("verificationStatus", status.name)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getAllUsers(): List<User> {
        return try {
            val snapshot = firestore.collection("users").get().await()
            snapshot.toObjects(User::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getAllWorkers(): List<WorkerProfile> {
        return try {
            val snapshot = firestore.collection("workers").get().await()
            snapshot.toObjects(WorkerProfile::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getAllBookings(): List<Booking> {
        return try {
            val snapshot = firestore.collection("bookings").get().await()
            snapshot.toObjects(Booking::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
