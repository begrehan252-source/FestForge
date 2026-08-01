package com.rehan.festforge.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.rehan.festforge.data.model.VerificationStatus
import com.rehan.festforge.data.model.WorkerProfile
import kotlinx.coroutines.tasks.await

class WorkerRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun getAvailableWorkers(): List<WorkerProfile> {
        return try {
            val snapshot = firestore.collection("workers")
                .whereEqualTo("verificationStatus", VerificationStatus.VERIFIED.name)
                .whereEqualTo("isAvailable", true)
                .get()
                .await()
            snapshot.toObjects(WorkerProfile::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun searchWorkers(
        category: String? = null,
        city: String? = null,
        minRating: Double = 0.0,
        maxRate: Double = Double.MAX_VALUE,
        minExperience: Int = 0
    ): List<WorkerProfile> {
        return try {
            var query: Query = firestore.collection("workers")
                .whereEqualTo("verificationStatus", VerificationStatus.VERIFIED.name)

            if (!category.isNullOrEmpty() && category != "All") {
                query = query.whereEqualTo("category", category)
            }
            if (!city.isNullOrEmpty() && city != "All") {
                query = query.whereEqualTo("city", city)
            }

            val snapshot = query.get().await()
            val list = snapshot.toObjects(WorkerProfile::class.java)

            list.filter {
                it.rating >= minRating &&
                it.hourlyRate <= maxRate &&
                it.experienceYears >= minExperience
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getWorkerById(workerId: String): WorkerProfile? {
        return try {
            val doc = firestore.collection("workers").document(workerId).get().await()
            doc.toObject(WorkerProfile::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveWorkerProfile(workerProfile: WorkerProfile): Boolean {
        return try {
            firestore.collection("workers").document(workerProfile.id).set(workerProfile).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateAvailability(workerId: String, isAvailable: Boolean): Boolean {
        return try {
            firestore.collection("workers").document(workerId)
                .update("isAvailable", isAvailable).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
