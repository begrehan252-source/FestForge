package com.rehan.festforge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rehan.festforge.data.model.Review
import com.rehan.festforge.data.model.WorkerProfile
import com.rehan.festforge.data.repository.ReviewRepository
import com.rehan.festforge.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WorkerProfileUiState(
    val worker: WorkerProfile? = null,
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class WorkerProfileViewModel(
    private val workerRepository: WorkerRepository = WorkerRepository(),
    private val reviewRepository: ReviewRepository = ReviewRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkerProfileUiState())
    val uiState: StateFlow<WorkerProfileUiState> = _uiState.asStateFlow()

    fun loadWorkerProfile(workerId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val profile = workerRepository.getWorkerById(workerId)
            val reviewsList = reviewRepository.getWorkerReviews(workerId)

            _uiState.value = _uiState.value.copy(
                worker = profile,
                reviews = reviewsList,
                isLoading = false
            )
        }
    }
}
