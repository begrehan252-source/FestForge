package com.rehan.festforge.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rehan.festforge.data.model.WorkerProfile
import com.rehan.festforge.ui.components.FestForgeHeader
import com.rehan.festforge.ui.theme.GoldDark
import com.rehan.festforge.ui.theme.GoldPrimary

@Composable
fun AdminDashboardScreen(
    pendingVerifications: List<WorkerProfile>,
    allWorkersCount: Int,
    allUsersCount: Int,
    allBookingsCount: Int,
    totalRevenue: Double,
    onNavigateToVerifications: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToUsers: () -> Unit
) {
    Scaffold(
        topBar = {
            FestForgeHeader(
                title = "FestForge Admin",
                subtitle = "MANAGEMENT CONSOLE"
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
            // Metrics Summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Platform Revenue (Service Fee)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("₹${totalRevenue.toInt()}", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = GoldDark)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Users", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("$allUsersCount", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("Total Staff", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("$allWorkersCount", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("Total Bookings", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("$allBookingsCount", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Admin Controls", fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(12.dp))

            AdminActionCard(
                icon = Icons.Default.VerifiedUser,
                title = "Worker ID Verifications",
                subtitle = "${pendingVerifications.size} pending approval",
                badgeCount = pendingVerifications.size,
                onClick = onNavigateToVerifications
            )

            AdminActionCard(
                icon = Icons.Default.Category,
                title = "Category Management",
                subtitle = "Add, edit, or disable event staff categories",
                badgeCount = 0,
                onClick = onNavigateToCategories
            )

            AdminActionCard(
                icon = Icons.Default.Group,
                title = "User & Worker Management",
                subtitle = "View profiles, account permissions and suspension",
                badgeCount = 0,
                onClick = onNavigateToUsers
            )
        }
    }
}

@Composable
private fun AdminActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    badgeCount: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(28.dp))

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            if (badgeCount > 0) {
                Badge(containerColor = GoldDark) {
                    Text("$badgeCount", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
