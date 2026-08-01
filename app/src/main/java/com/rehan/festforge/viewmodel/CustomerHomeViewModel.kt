package com.rehan.festforge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rehan.festforge.data.model.Booking
import com.rehan.festforge.data.model.Category
import com.rehan.festforge.data.model.WorkerProfile
import com.rehan.festforge.data.repository.BookingRepository
import com.rehan.festforge.data.repository.CategoryRepository
import com.rehan.festforge.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CustomerHomeUiState(
    val categories: List<Category> = emptyList(),
    val popularWorkers: List<WorkerProfile> = emptyList(),
    val upcomingBooking: Booking? = null,
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val userCity: String = "Mumbai"
)

class CustomerHomeViewModel(
    private val categoryRepository: CategoryRepository = CategoryRepository(),
    private val workerRepository: WorkerRepository = WorkerRepository(),
    private val bookingRepository: BookingRepository = BookingRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerHomeUiState())
    val uiState: StateFlow<CustomerHomeUiState> = _uiState.asStateFlow()

    fun loadData(customerId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val cats = categoryRepository.getCategories()
            val workers = workerRepository.getAvailableWorkers()
            val bookings = bookingRepository.getCustomerBookings(customerId)
            val upcoming = bookings.firstOrNull { it.bookingStatus.name in listOf("PENDING", "ACCEPTED", "IN_PROGRESS") }

            _uiState.value = _uiState.value.copy(
                categories = cats,
                popularWorkers = workers,
                upcomingBooking = upcoming,
                isLoading = false
            )
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }
}
