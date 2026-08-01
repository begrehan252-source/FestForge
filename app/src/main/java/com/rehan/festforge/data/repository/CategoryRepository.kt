package com.rehan.festforge.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.rehan.festforge.data.model.Category
import kotlinx.coroutines.tasks.await

class CategoryRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val defaultCategories = listOf(
        Category("1", "Waiter", "Restaurant", "Professional floor & banquet service", true),
        Category("2", "Butler", "Person", "VIP event & private butler service", true),
        Category("3", "Captain", "Badge", "Banquet floor captain & coordinator", true),
        Category("4", "Supervisor", "Supervisor", "Event execution & team supervisor", true),
        Category("5", "Bartender", "LocalBar", "Craft cocktails & beverage service", true),
        Category("6", "Chef", "SoupKitchen", "Professional culinary & live counter chef", true),
        Category("7", "Helper", "Handshake", "Kitchen & setup auxiliary helper", true),
        Category("8", "Housekeeping", "CleaningServices", "Venue cleanliness & maintenance staff", true),
        Category("9", "Event Staff", "Groups", "All-round general event hospitality staff", true)
    )

    suspend fun getCategories(): List<Category> {
        return try {
            val snapshot = firestore.collection("categories").get().await()
            if (snapshot.isEmpty) {
                // Initialize defaults if empty
                defaultCategories.forEach { cat ->
                    firestore.collection("categories").document(cat.id).set(cat)
                }
                defaultCategories
            } else {
                snapshot.toObjects(Category::class.java).filter { it.isActive }
            }
        } catch (e: Exception) {
            defaultCategories
        }
    }

    suspend fun addCategory(category: Category): Boolean {
        return try {
            firestore.collection("categories").document(category.id).set(category).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateCategory(category: Category): Boolean {
        return try {
            firestore.collection("categories").document(category.id).set(category).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
