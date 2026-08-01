package com.rehan.festforge.ui.screens.worker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rehan.festforge.data.model.Booking
import com.rehan.festforge.ui.components.FestForgeButton
import com.rehan.festforge.ui.theme.GoldDark
import com.rehan.festforge.ui.theme.StatusGreen
import com.rehan.festforge.ui.theme.StatusRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerRequestScreen(
    booking: Booking?,
    onAccept: (bookingId: String) -> Unit,
    onReject: (bookingId: String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Job Request", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (booking == null) {
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
                Text(
                    text = "Event Request #${booking.id}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Customer Name", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(booking.customerName, fontSize = 16.sp, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Schedule & Shift", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("📅 Date: ${booking.eventDate}", fontSize = 14.sp)
                        Text("⏰ Time: ${booking.startTime} - ${booking.endTime}", fontSize = 14.sp)

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Event Venue", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${booking.eventLocation}\n${booking.fullAddress}", fontSize = 14.sp)

                        if (booking.specialInstructions.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Instructions", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(booking.specialInstructions, fontSize = 13.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Expected Worker Payout", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("₹${booking.subtotalAmount.toInt()}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = GoldDark)
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { onReject(booking.id) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusRed)
                    ) {
                        Text("Reject", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onAccept(booking.id) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = StatusGreen)
                    ) {
                        Text("Accept Job", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
