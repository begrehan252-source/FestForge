package com.rehan.festforge.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rehan.festforge.data.model.User
import com.rehan.festforge.data.model.UserRole
import com.rehan.festforge.ui.theme.DarkBackground
import com.rehan.festforge.ui.theme.GoldPrimary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    currentUser: User?,
    isAuthenticated: Boolean,
    onNavigateToLogin: () -> Unit,
    onNavigateToCustomerHome: () -> Unit,
    onNavigateToWorkerDashboard: () -> Unit,
    onNavigateToAdminDashboard: () -> Unit
) {
    LaunchedEffect(key1 = isAuthenticated, key2 = currentUser) {
        delay(1500) // Brief premium branding delay
        if (!isAuthenticated) {
            onNavigateToLogin()
        } else {
            when (currentUser?.role) {
                UserRole.CUSTOMER -> onNavigateToCustomerHome()
                UserRole.WORKER -> onNavigateToWorkerDashboard()
                UserRole.ADMIN -> onNavigateToAdminDashboard()
                null -> onNavigateToLogin()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Fest",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Forge",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "EVENT STAFF BOOKING PLATFORM",
                fontSize = 12.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.SemiBold,
                color = GoldPrimary.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            CircularProgressIndicator(
                color = GoldPrimary,
                strokeWidth = 3.dp,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
