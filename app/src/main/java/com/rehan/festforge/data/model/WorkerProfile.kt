package com.rehan.festforge.data.model

enum class VerificationStatus {
    PENDING,
    VERIFIED,
    REJECTED
}

data class WorkerProfile(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val phone: String = "",
    val photoUrl: String = "",
    val category: String = "",
    val skills: List<String> = emptyList(),
    val city: String = "",
    val address: String = "",
    val experienceYears: Int = 0,
    val hourlyRate: Double = 0.0,
    val isAvailable: Boolean = true,
    val verificationStatus: VerificationStatus = VerificationStatus.PENDING,
    val verificationDocUrl: String = "",
    val aadhaarNumber: String = "",
    val isAadhaarVerified: Boolean = false,
    val aadhaarFrontUrl: String = "",
    val aadhaarBackUrl: String = "",
    val documentReviewStatus: String = "Document Pending Review",
    val completedJobs: Int = 0,
    val rating: Double = 0.0,
    val totalReviews: Int = 0,
    val bio: String = ""
)
