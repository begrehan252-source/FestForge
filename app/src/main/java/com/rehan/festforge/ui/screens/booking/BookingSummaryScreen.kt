package com.rehan.festforge.ui.screens.booking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rehan.festforge.ui.components.FestForgeButton
import com.rehan.festforge.ui.theme.GoldDark
import com.rehan.festforge.viewmodel.BookingUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingSummaryScreen(
    bookingState: BookingUiState,
    onConfirmBooking: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Booking Summary", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(elevation = 8.dp) {
                Box(modifier = Modifier.padding(16.dp)) {
                    FestForgeButton(
                        text = "Confirm & Request Staff",
                        onClick = onConfirmBooking,
                        isLoading = bookingState.isLoading
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Staff & Service", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "${bookingState.selectedWorker?.name} (${bookingState.selectedWorker?.category})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Event Details", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("📅 Date: ${bookingState.eventDate}", fontSize = 14.sp)
                    Text("⏰ Time: ${bookingState.startTime} to ${bookingState.endTime}", fontSize = 14.sp)
                    Text("👥 Staff Quantity: ${bookingState.staffQuantity}", fontSize = 14.sp)
                    Text("📍 Location: ${bookingState.eventLocation}", fontSize = 14.sp)
                    Text("🏠 Address: ${bookingState.fullAddress}", fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Payment Architecture
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Fare Breakdown", fontSize = 14.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Shift Charge (Est. 4 hrs)", fontSize = 13.sp)
                        Text("₹${bookingState.subtotal.toInt()}", fontSize = 13.sp)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Platform Service Charge (5%)", fontSize = 13.sp)
                        Text("₹${bookingState.serviceFee.toInt()}", fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Amount", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("₹${bookingState.totalAmount.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = GoldDark)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Payment Gateway info card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CreditCard, contentDescription = null, tint = GoldDark)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Payment Method: Cash / Pay at Venue (Gateway Integration Ready)",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
