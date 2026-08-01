package com.rehan.festforge.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.rehan.festforge.data.model.Booking
import com.rehan.festforge.data.model.BookingStatus
import kotlinx.coroutines.tasks.await
import java.util.UUID

class BookingRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun createBooking(booking: Booking): String? {
        return try {
            val bookingId = if (booking.id.isEmpty()) "BK-" + UUID.randomUUID().toString().take(8).uppercase() else booking.id
            val finalBooking = booking.copy(id = bookingId, createdAt = System.currentTimeMillis())
            firestore.collection("bookings").document(bookingId).set(finalBooking).await()
            bookingId
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getCustomerBookings(customerId: String): List<Booking> {
        return try {
            val snapshot = firestore.collection("bookings")
                .whereEqualTo("customerId", customerId)
                .get()
                .await()
            snapshot.toObjects(Booking::class.java).sortedByDescending { it.createdAt }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getWorkerBookings(workerId: String): List<Booking> {
        return try {
            val snapshot = firestore.collection("bookings")
                .whereEqualTo("workerId", workerId)
                .get()
                .await()
            snapshot.toObjects(Booking::class.java).sortedByDescending { it.createdAt }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getBookingById(bookingId: String): Booking? {
        return try {
            val doc = firestore.collection("bookings").document(bookingId).get().await()
            doc.toObject(Booking::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateBookingStatus(bookingId: String, newStatus: BookingStatus): Boolean {
        return try {
            firestore.collection("bookings").document(bookingId)
                .update(
                    mapOf(
                        "bookingStatus" to newStatus.name,
                        "updatedAt" to System.currentTimeMillis()
                    )
                ).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
