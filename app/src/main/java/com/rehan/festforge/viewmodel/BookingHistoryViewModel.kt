package com.rehan.festforge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rehan.festforge.data.model.Booking
import com.rehan.festforge.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BookingHistoryUiState(
    val allBookings: List<Booking> = emptyList(),
    val filteredBookings: List<Booking> = emptyList(),
    val selectedTab: Int = 0, // 0: Upcoming, 1: Active, 2: Completed, 3: Cancelled
    val isLoading: Boolean = false
)

class BookingHistoryViewModel(
    private val bookingRepository: BookingRepository = BookingRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingHistoryUiState())
    val uiState: StateFlow<BookingHistoryUiState> = _uiState.asStateFlow()

    fun loadBookings(userId: String, isWorker: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val list = if (isWorker) {
                bookingRepository.getWorkerBookings(userId)
            } else {
                bookingRepository.getCustomerBookings(userId)
            }
            _uiState.value = _uiState.value.copy(allBookings = list)
            filterTab(_uiState.value.selectedTab)
        }
    }

    fun selectTab(tabIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = tabIndex)
        filterTab(tabIndex)
    }

    private fun filterTab(tabIndex: Int) {
        val all = _uiState.value.allBookings
        val filtered = when (tabIndex) {
            0 -> all.filter { it.bookingStatus.name in listOf("PENDING", "ACCEPTED") }
            1 -> all.filter { it.bookingStatus.name == "IN_PROGRESS" }
            2 -> all.filter { it.bookingStatus.name == "COMPLETED" }
            3 -> all.filter { it.bookingStatus.name == "CANCELLED" }
            else -> all
        }
        _uiState.value = _uiState.value.copy(filteredBookings = filtered, isLoading = false)
    }
}
