package com.rehan.festforge.data.model

enum class UserRole {
    CUSTOMER,
    WORKER,
    ADMIN
}

data class User(
    val id: String = "",
    val phone: String = "",
    val name: String = "",
    val email: String = "",
    val profilePicUrl: String = "",
    val role: UserRole = UserRole.CUSTOMER,
    val city: String = "",
    val address: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
