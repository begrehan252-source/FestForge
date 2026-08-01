package com.rehan.festforge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rehan.festforge.data.model.Booking
import com.rehan.festforge.data.model.Category
import com.rehan.festforge.data.model.User
import com.rehan.festforge.data.model.VerificationStatus
import com.rehan.festforge.data.model.WorkerProfile
import com.rehan.festforge.data.repository.AdminRepository
import com.rehan.festforge.data.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdminDashboardUiState(
    val pendingVerifications: List<WorkerProfile> = emptyList(),
    val allWorkers: List<WorkerProfile> = emptyList(),
    val allUsers: List<User> = emptyList(),
    val allBookings: List<Booking> = emptyList(),
    val categories: List<Category> = emptyList(),
    val totalRevenue: Double = 0.0,
    val isLoading: Boolean = false
)

class AdminViewModel(
    private val adminRepository: AdminRepository = AdminRepository(),
    private val categoryRepository: CategoryRepository = CategoryRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminDashboardUiState())
    val uiState: StateFlow<AdminDashboardUiState> = _uiState.asStateFlow()

    fun loadAdminData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val pending = adminRepository.getPendingWorkerVerifications()
            val workers = adminRepository.getAllWorkers()
            val users = adminRepository.getAllUsers()
            val bookings = adminRepository.getAllBookings()
            val cats = categoryRepository.getCategories()

            val revenue = bookings.sumOf { it.serviceFee }

            _uiState.value = _uiState.value.copy(
                pendingVerifications = pending,
                allWorkers = workers,
                allUsers = users,
                allBookings = bookings,
                categories = cats,
                totalRevenue = revenue,
                isLoading = false
            )
        }
    }

    fun approveWorker(workerId: String) {
        viewModelScope.launch {
            adminRepository.updateWorkerVerificationStatus(workerId, VerificationStatus.VERIFIED)
            loadAdminData()
        }
    }

    fun rejectWorker(workerId: String) {
        viewModelScope.launch {
            adminRepository.updateWorkerVerificationStatus(workerId, VerificationStatus.REJECTED)
            loadAdminData()
        }
    }

    fun addCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.addCategory(category)
            loadAdminData()
        }
    }
}
