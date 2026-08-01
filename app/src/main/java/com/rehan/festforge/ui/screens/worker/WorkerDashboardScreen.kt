package com.rehan.festforge.ui.screens.worker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rehan.festforge.data.model.Booking
import com.rehan.festforge.data.model.WorkerProfile
import com.rehan.festforge.ui.components.BookingCard
import com.rehan.festforge.ui.components.FestForgeHeader
import com.rehan.festforge.ui.theme.GoldDark
import com.rehan.festforge.ui.theme.GoldPrimary

@Composable
fun WorkerDashboardScreen(
    profile: WorkerProfile?,
    pendingRequests: List<Booking>,
    todayJobs: List<Booking>,
    isAvailable: Boolean,
    todayEarnings: Double,
    isLoading: Boolean,
    onToggleAvailability: (Boolean) -> Unit,
    onViewRequest: (bookingId: String) -> Unit,
    onViewEarnings: () -> Unit,
    onNavigateToNotifications: () -> Unit
) {
    Scaffold(
        topBar = {
            FestForgeHeader(
                title = "Worker Portal",
                subtitle = profile?.category ?: "EVENT STAFF",
                actions = {
                    IconButton(onClick = onNavigateToNotifications) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = GoldPrimary)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Availability Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (isAvailable) "Status: Available for Work" else "Status: Offline",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isAvailable) "Receiving new booking requests" else "Not receiving requests",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = isAvailable,
                        onCheckedChange = onToggleAvailability,
                        colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Earnings Quick Summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Today's Earnings", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("₹${todayEarnings.toInt()}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = GoldDark)
                    }
                    Button(
                        onClick = onViewEarnings,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                    ) {
                        Text("View Earnings", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // New Job Requests
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "New Booking Requests (${pendingRequests.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (pendingRequests.isEmpty()) {
                Text(
                    text = "No pending booking requests.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            } else {
                pendingRequests.forEach { request ->
                    BookingCard(
                        booking = request,
                        onClick = { onViewRequest(request.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Active/Today Jobs
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Upcoming & Active Jobs (${todayJobs.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (todayJobs.isEmpty()) {
                Text(
                    text = "No active jobs assigned today.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            } else {
                todayJobs.forEach { job ->
                    BookingCard(
                        booking = job,
                        onClick = { onViewRequest(job.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
