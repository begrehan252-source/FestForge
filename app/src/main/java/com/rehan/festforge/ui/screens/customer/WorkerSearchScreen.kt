package com.rehan.festforge.ui.screens.customer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rehan.festforge.data.model.WorkerProfile
import com.rehan.festforge.ui.components.FestForgeTextField
import com.rehan.festforge.ui.components.WorkerCard
import com.rehan.festforge.ui.theme.GoldDark
import com.rehan.festforge.ui.theme.GoldPrimary
import com.rehan.festforge.viewmodel.SearchFilterState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerSearchScreen(
    workers: List<WorkerProfile>,
    isLoading: Boolean,
    filterState: SearchFilterState,
    onFilterChange: (SearchFilterState) -> Unit,
    onSelectWorker: (workerId: String) -> Unit,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val categories = listOf("All", "Waiter", "Butler", "Captain", "Supervisor", "Bartender", "Chef", "Helper")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Event Staff", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            FestForgeTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = "",
                placeholder = "Search staff name or skill..."
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = filterState.selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            onFilterChange(filterState.copy(selectedCategory = cat))
                        },
                        label = { Text(cat, fontSize = 13.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldPrimary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val filteredList = workers.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.skills.any { skill -> skill.contains(searchQuery, ignoreCase = true) }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GoldPrimary)
                }
            } else if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No staff found matching criteria.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(filteredList) { worker ->
                        WorkerCard(
                            worker = worker,
                            onClick = { onSelectWorker(worker.id) }
                        )
                    }
                }
            }
        }
    }
}
