package com.rehan.festforge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rehan.festforge.data.model.Booking
import com.rehan.festforge.data.model.BookingStatus
import com.rehan.festforge.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EarningsUiState(
    val todayEarnings: Double = 0.0,
    val weeklyEarnings: Double = 0.0,
    val totalEarnings: Double = 0.0,
    val completedJobsCount: Int = 0,
    val completedBookings: List<Booking> = emptyList(),
    val isLoading: Boolean = false
)

class EarningsViewModel(
    private val bookingRepository: BookingRepository = BookingRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(EarningsUiState())
    val uiState: StateFlow<EarningsUiState> = _uiState.asStateFlow()

    fun loadEarnings(workerId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val bookings = bookingRepository.getWorkerBookings(workerId)
            val completed = bookings.filter { it.bookingStatus == BookingStatus.COMPLETED }

            val total = completed.sumOf { it.subtotalAmount }
            val count = completed.size

            _uiState.value = _uiState.value.copy(
                todayEarnings = total * 0.3, // Simulated split
                weeklyEarnings = total * 0.7,
                totalEarnings = total,
                completedJobsCount = count,
                completedBookings = completed,
                isLoading = false
            )
        }
    }
}
