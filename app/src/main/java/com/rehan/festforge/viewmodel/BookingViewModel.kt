package com.rehan.festforge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rehan.festforge.data.model.Booking
import com.rehan.festforge.data.model.BookingStatus
import com.rehan.festforge.data.model.PaymentStatus
import com.rehan.festforge.data.model.WorkerProfile
import com.rehan.festforge.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BookingUiState(
    val selectedWorker: WorkerProfile? = null,
    val eventDate: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val staffQuantity: Int = 1,
    val eventLocation: String = "",
    val fullAddress: String = "",
    val specialInstructions: String = "",
    val subtotal: Double = 0.0,
    val serviceFee: Double = 0.0,
    val totalAmount: Double = 0.0,
    val isLoading: Boolean = false,
    val createdBookingId: String? = null,
    val errorMessage: String? = null
)

class BookingViewModel(
    private val bookingRepository: BookingRepository = BookingRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    fun setWorker(worker: WorkerProfile) {
        _uiState.value = _uiState.value.copy(selectedWorker = worker)
        calculateTotal()
    }

    fun updateBookingFields(
        date: String = _uiState.value.eventDate,
        start: String = _uiState.value.startTime,
        end: String = _uiState.value.endTime,
        qty: Int = _uiState.value.staffQuantity,
        location: String = _uiState.value.eventLocation,
        address: String = _uiState.value.fullAddress,
        instructions: String = _uiState.value.specialInstructions
    ) {
        _uiState.value = _uiState.value.copy(
            eventDate = date,
            startTime = start,
            endTime = end,
            staffQuantity = qty,
            eventLocation = location,
            fullAddress = address,
            specialInstructions = instructions,
            errorMessage = null
        )
        calculateTotal()
    }

    private fun calculateTotal() {
        val worker = _uiState.value.selectedWorker ?: return
        val hours = 4.0 // Default 4-hour shift calculation
        val sub = worker.hourlyRate * hours * _uiState.value.staffQuantity
        val fee = sub * 0.05 // 5% platform service charge
        val total = sub + fee

        _uiState.value = _uiState.value.copy(
            subtotal = sub,
            serviceFee = fee,
            totalAmount = total
        )
    }

    fun confirmBooking(
        customerId: String,
        customerName: String,
        customerPhone: String,
        onSuccess: (String) -> Unit
    ) {
        val state = _uiState.value
        val worker = state.selectedWorker

        if (worker == null || state.eventDate.isBlank() || state.startTime.isBlank() || state.fullAddress.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Please fill in all mandatory booking fields")
            return
        }

        val newBooking = Booking(
            customerId = customerId,
            customerName = customerName,
            customerPhone = customerPhone,
            workerId = worker.id,
            workerName = worker.name,
            category = worker.category,
            eventDate = state.eventDate,
            startTime = state.startTime,
            endTime = state.endTime.ifEmpty { "Late Shift" },
            staffQuantity = state.staffQuantity,
            eventLocation = state.eventLocation.ifEmpty { worker.city },
            fullAddress = state.fullAddress,
            specialInstructions = state.specialInstructions,
            subtotalAmount = state.subtotal,
            serviceFee = state.serviceFee,
            totalAmount = state.totalAmount,
            paymentStatus = PaymentStatus.PENDING,
            bookingStatus = BookingStatus.PENDING
        )

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val bookingId = bookingRepository.createBooking(newBooking)
            if (bookingId != null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    createdBookingId = bookingId
                )
                onSuccess(bookingId)
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to create booking. Please try again."
                )
            }
        }
    }
}
