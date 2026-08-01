package com.rehan.festforge.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.rehan.festforge.data.model.NotificationItem
import kotlinx.coroutines.tasks.await
import java.util.UUID

class NotificationRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun getUserNotifications(userId: String): List<NotificationItem> {
        return try {
            val snapshot = firestore.collection("notifications")
                .whereEqualTo("userId", userId)
                .get()
                .await()
            snapshot.toObjects(NotificationItem::class.java).sortedByDescending { it.timestamp }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun createNotification(notification: NotificationItem): Boolean {
        return try {
            val id = if (notification.id.isEmpty()) UUID.randomUUID().toString() else notification.id
            val item = notification.copy(id = id, timestamp = System.currentTimeMillis())
            firestore.collection("notifications").document(id).set(item).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun markAsRead(notificationId: String): Boolean {
        return try {
            firestore.collection("notifications").document(notificationId)
                .update("isRead", true).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
