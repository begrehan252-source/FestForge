package com.rehan.festforge.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.rehan.festforge.data.model.Review
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ReviewRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun submitReview(review: Review): Boolean {
        return try {
            val reviewId = if (review.id.isEmpty()) UUID.randomUUID().toString() else review.id
            val finalReview = review.copy(id = reviewId, timestamp = System.currentTimeMillis())

            // Prevent duplicate review for the same booking
            val existing = firestore.collection("reviews")
                .whereEqualTo("bookingId", review.bookingId)
                .get()
                .await()

            if (!existing.isEmpty) {
                return false
            }

            firestore.collection("reviews").document(reviewId).set(finalReview).await()

            // Recalculate worker rating
            val workerReviews = firestore.collection("reviews")
                .whereEqualTo("workerId", review.workerId)
                .get()
                .await()
                .toObjects(Review::class.java)

            if (workerReviews.isNotEmpty()) {
                val avgRating = workerReviews.map { it.rating }.average()
                val totalCount = workerReviews.size
                firestore.collection("workers").document(review.workerId)
                    .update(
                        mapOf(
                            "rating" to avgRating,
                            "totalReviews" to totalCount
                        )
                    ).await()
            }

            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getWorkerReviews(workerId: String): List<Review> {
        return try {
            val snapshot = firestore.collection("reviews")
                .whereEqualTo("workerId", workerId)
                .get()
                .await()
            snapshot.toObjects(Review::class.java).sortedByDescending { it.timestamp }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
