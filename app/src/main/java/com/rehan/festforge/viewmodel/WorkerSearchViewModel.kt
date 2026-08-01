package com.rehan.festforge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rehan.festforge.data.model.WorkerProfile
import com.rehan.festforge.data.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SearchFilterState(
    val selectedCategory: String = "All",
    val selectedCity: String = "All",
    val minRating: Double = 0.0,
    val maxRate: Double = 5000.0,
    val minExperience: Int = 0
)

data class WorkerSearchUiState(
    val workers: List<WorkerProfile> = emptyList(),
    val isLoading: Boolean = false,
    val filterState: SearchFilterState = SearchFilterState()
)

class WorkerSearchViewModel(
    private val workerRepository: WorkerRepository = WorkerRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkerSearchUiState())
    val uiState: StateFlow<WorkerSearchUiState> = _uiState.asStateFlow()

    init {
        performSearch()
    }

    fun updateFilters(newFilter: SearchFilterState) {
        _uiState.value = _uiState.value.copy(filterState = newFilter)
        performSearch()
    }

    fun performSearch() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val filters = _uiState.value.filterState
            val results = workerRepository.searchWorkers(
                category = filters.selectedCategory,
                city = filters.selectedCity,
                minRating = filters.minRating,
                maxRate = filters.maxRate,
                minExperience = filters.minExperience
            )
            _uiState.value = _uiState.value.copy(workers = results, isLoading = false)
        }
    }
}
