package com.rehan.festforge.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rehan.festforge.data.model.Booking
import com.rehan.festforge.data.model.Category
import com.rehan.festforge.data.model.WorkerProfile
import com.rehan.festforge.ui.components.BookingCard
import com.rehan.festforge.ui.components.FestForgeHeader
import com.rehan.festforge.ui.components.WorkerCard
import com.rehan.festforge.ui.theme.GoldDark
import com.rehan.festforge.ui.theme.GoldPrimary

@Composable
fun CustomerHomeScreen(
    userName: String,
    userCity: String,
    categories: List<Category>,
    popularWorkers: List<WorkerProfile>,
    upcomingBooking: Booking?,
    onNavigateToSearch: (categoryName: String?) -> Unit,
    onNavigateToWorkerProfile: (workerId: String) -> Unit,
    onNavigateToBookingDetails: (bookingId: String) -> Unit,
    onNavigateToNotifications: () -> Unit
) {
    Scaffold(
        topBar = {
            FestForgeHeader(
                title = "FestForge",
                subtitle = "📍 $userCity",
                actions = {
                    IconButton(onClick = onNavigateToNotifications) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = GoldPrimary
                        )
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
            Text(
                text = "Hello, $userName 👋",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Book verified waiters, bartenders & chefs for your events",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Search Bar Trigger
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToSearch(null) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = GoldPrimary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Search waiters, bartenders, chefs...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            }

            // Upcoming Booking Card if active
            if (upcomingBooking != null) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Upcoming Event",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                BookingCard(
                    booking = upcomingBooking,
                    onClick = { onNavigateToBookingDetails(upcomingBooking.id) }
                )
            }

            // Categories
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Event Staff Categories",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "View All",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GoldDark,
                    modifier = Modifier.clickable { onNavigateToSearch(null) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    Card(
                        modifier = Modifier
                            .width(110.dp)
                            .clickable { onNavigateToSearch(category.name) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(GoldPrimary.copy(alpha = 0.2f), shape = RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = category.name.take(2).uppercase(),
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary,
                                    fontSize = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = category.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Available & Featured Workers
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Featured Event Staff",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (popularWorkers.isEmpty()) {
                    Text(
                        text = "No staff available in your area right now.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                } else {
                    popularWorkers.forEach { worker ->
                        WorkerCard(
                            worker = worker,
                            onClick = { onNavigateToWorkerProfile(worker.id) }
                        )
                    }
                }
            }
        }
    }
}
