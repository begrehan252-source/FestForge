package com.rehan.festforge.ui.screens.booking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rehan.festforge.ui.components.FestForgeButton
import com.rehan.festforge.ui.components.FestForgeTextField
import com.rehan.festforge.viewmodel.BookingUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    bookingState: BookingUiState,
    onUpdateFields: (date: String, start: String, end: String, qty: Int, location: String, address: String, instructions: String) -> Unit,
    onProceedToSummary: () -> Unit,
    onBack: () -> Unit
) {
    var date by remember { mutableStateOf(bookingState.eventDate.ifEmpty { "15 Aug 2026" }) }
    var startTime by remember { mutableStateOf(bookingState.startTime.ifEmpty { "06:00 PM" }) }
    var endTime by remember { mutableStateOf(bookingState.endTime.ifEmpty { "11:00 PM" }) }
    var staffQty by remember { mutableStateOf(bookingState.staffQuantity.toString()) }
    var location by remember { mutableStateOf(bookingState.eventLocation.ifEmpty { "Grand Ballroom, St Regis" }) }
    var address by remember { mutableStateOf(bookingState.fullAddress.ifEmpty { "Senapati Bapat Marg, Lower Parel, Mumbai" }) }
    var instructions by remember { mutableStateOf(bookingState.specialInstructions) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Event Requirements", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Box(modifier = Modifier.padding(16.dp)) {
                    FestForgeButton(
                        text = "Review Booking Summary",
                        onClick = {
                            val qtyInt = staffQty.toIntOrNull() ?: 1
                            onUpdateFields(date, startTime, endTime, qtyInt, location, address, instructions)
                            onProceedToSummary()
                        },
                        enabled = date.isNotBlank() && startTime.isNotBlank() && address.isNotBlank()
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Booking for ${bookingState.selectedWorker?.name ?: "Selected Worker"}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Category: ${bookingState.selectedWorker?.category} • ₹${bookingState.selectedWorker?.hourlyRate?.toInt()}/hr",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            FestForgeTextField(
                value = date,
                onValueChange = { date = it },
                label = "Event Date",
                placeholder = "DD/MM/YYYY"
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FestForgeTextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = "Start Time",
                    placeholder = "e.g. 06:00 PM",
                    modifier = Modifier.weight(1f)
                )

                FestForgeTextField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    label = "End Time",
                    placeholder = "e.g. 11:00 PM",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            FestForgeTextField(
                value = staffQty,
                onValueChange = { staffQty = it },
                label = "Staff Quantity Needed",
                placeholder = "1"
            )

            Spacer(modifier = Modifier.height(14.dp))

            FestForgeTextField(
                value = location,
                onValueChange = { location = it },
                label = "Event Venue / Landmark",
                placeholder = "Hotel or Venue Name"
            )

            Spacer(modifier = Modifier.height(14.dp))

            FestForgeTextField(
                value = address,
                onValueChange = { address = it },
                label = "Full Address",
                placeholder = "Street address, area, city",
                singleLine = false
            )

            Spacer(modifier = Modifier.height(14.dp))

            FestForgeTextField(
                value = instructions,
                onValueChange = { instructions = it },
                label = "Special Instructions (Optional)",
                placeholder = "Dress code, specific shift notes...",
                singleLine = false
            )

            if (bookingState.errorMessage != null) {
                Text(
                    text = bookingState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}
