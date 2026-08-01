package com.rehan.festforge.data.model

enum class BookingStatus {
    PENDING,
    ACCEPTED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

enum class PaymentStatus {
    PENDING,
    PAID,
    FAILED,
    REFUNDED
}

data class Booking(
    val id: String = "",
    val customerId: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val workerId: String = "",
    val workerName: String = "",
    val category: String = "",
    val eventDate: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val staffQuantity: Int = 1,
    val eventLocation: String = "",
    val fullAddress: String = "",
    val specialInstructions: String = "",
    val subtotalAmount: Double = 0.0,
    val serviceFee: Double = 0.0,
    val totalAmount: Double = 0.0,
    val paymentStatus: PaymentStatus = PaymentStatus.PENDING,
    val bookingStatus: BookingStatus = BookingStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
