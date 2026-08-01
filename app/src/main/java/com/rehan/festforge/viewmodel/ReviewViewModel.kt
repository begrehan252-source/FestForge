package com.rehan.festforge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rehan.festforge.data.model.Review
import com.rehan.festforge.data.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ReviewUiState(
    val rating: Int = 5,
    val comment: String = "",
    val isLoading: Boolean = false,
    val isSubmitted: Boolean = false,
    val errorMessage: String? = null
)

class ReviewViewModel(
    private val reviewRepository: ReviewRepository = ReviewRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    fun updateRating(stars: Int) {
        _uiState.value = _uiState.value.copy(rating = stars)
    }

    fun updateComment(text: String) {
        _uiState.value = _uiState.value.copy(comment = text)
    }

    fun submitReview(
        bookingId: String,
        customerId: String,
        customerName: String,
        workerId: String,
        onSuccess: () -> Unit
    ) {
        val state = _uiState.value
        val review = Review(
            bookingId = bookingId,
            customerId = customerId,
            customerName = customerName,
            workerId = workerId,
            rating = state.rating,
            comment = state.comment
        )

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val success = reviewRepository.submitReview(review)
            if (success) {
                _uiState.value = _uiState.value.copy(isLoading = false, isSubmitted = true)
                onSuccess()
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Already reviewed or submission failed."
                )
            }
        }
    }
}
