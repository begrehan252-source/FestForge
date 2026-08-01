package com.rehan.festforge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rehan.festforge.data.model.Booking
import com.rehan.festforge.data.model.BookingStatus
import com.rehan.festforge.data.model.WorkerProfile
import com.rehan.festforge.data.repository.BookingRepository
import com.rehan.festforge.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WorkerDashboardUiState(
    val workerProfile: WorkerProfile? = null,
    val pendingRequests: List<Booking> = emptyList(),
    val todayJobs: List<Booking> = emptyList(),
    val isAvailable: Boolean = true,
    val todayEarnings: Double = 0.0,
    val isLoading: Boolean = false
)

class WorkerDashboardViewModel(
    private val workerRepository: WorkerRepository = WorkerRepository(),
    private val bookingRepository: BookingRepository = BookingRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkerDashboardUiState())
    val uiState: StateFlow<WorkerDashboardUiState> = _uiState.asStateFlow()

    fun loadDashboard(workerId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val profile = workerRepository.getWorkerById(workerId)
            val bookings = bookingRepository.getWorkerBookings(workerId)

            val pending = bookings.filter { it.bookingStatus == BookingStatus.PENDING }
            val active = bookings.filter { it.bookingStatus.name in listOf("ACCEPTED", "IN_PROGRESS") }
            val completedToday = bookings.filter { it.bookingStatus == BookingStatus.COMPLETED }
            val earnings = completedToday.sumOf { it.subtotalAmount }

            _uiState.value = _uiState.value.copy(
                workerProfile = profile,
                pendingRequests = pending,
                todayJobs = active,
                isAvailable = profile?.isAvailable ?: true,
                todayEarnings = earnings,
                isLoading = false
            )
        }
    }

    fun toggleAvailability(workerId: String, available: Boolean) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAvailable = available)
            workerRepository.updateAvailability(workerId, available)
        }
    }

    fun respondToRequest(bookingId: String, accept: Boolean, workerId: String) {
        viewModelScope.launch {
            val status = if (accept) BookingStatus.ACCEPTED else BookingStatus.CANCELLED
            bookingRepository.updateBookingStatus(bookingId, status)
            loadDashboard(workerId)
        }
    }
}
