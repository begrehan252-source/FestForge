package com.rehan.festforge.ui.screens.booking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.rehan.festforge.data.model.Booking
import com.rehan.festforge.data.model.BookingStatus
import com.rehan.festforge.ui.components.FestForgeButton
import com.rehan.festforge.ui.components.StatusChip
import com.rehan.festforge.ui.theme.GoldDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailsScreen(
    booking: Booking?,
    isLoading: Boolean,
    onRateWorker: (bookingId: String, workerId: String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Booking Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading || booking == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = booking.id,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    StatusChip(status = booking.bookingStatus)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Category", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(booking.category, fontSize = 16.sp, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Assigned Worker", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(booking.workerName, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Event Schedule", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("📅 ${booking.eventDate} (${booking.startTime} - ${booking.endTime})", fontSize = 14.sp)

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Staff Quantity", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${booking.staffQuantity} Staff Member(s)", fontSize = 14.sp)

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Venue Address", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${booking.eventLocation} - ${booking.fullAddress}", fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Total Amount", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("₹${booking.totalAmount.toInt()}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = GoldDark)
                        Text("Payment Status: ${booking.paymentStatus}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                if (booking.bookingStatus == BookingStatus.COMPLETED) {
                    Spacer(modifier = Modifier.height(24.dp))
                    FestForgeButton(
                        text = "Rate & Review Worker",
                        onClick = { onRateWorker(booking.id, booking.workerId) }
                    )
                }
            }
        }
    }
}
