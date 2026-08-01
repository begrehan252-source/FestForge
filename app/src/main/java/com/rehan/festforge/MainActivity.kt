package com.rehan.festforge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.rehan.festforge.navigation.FestForgeNavGraph
import com.rehan.festforge.ui.theme.FestForgeTheme
import com.rehan.festforge.viewmodel.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as FestForgeApplication
        val userPreferences = app.userPreferences

        val authViewModel = AuthViewModel()
        val customerHomeViewModel = CustomerHomeViewModel()
        val workerSearchViewModel = WorkerSearchViewModel()
        val workerProfileViewModel = WorkerProfileViewModel()
        val bookingViewModel = BookingViewModel()
        val bookingHistoryViewModel = BookingHistoryViewModel()
        val workerDashboardViewModel = WorkerDashboardViewModel()
        val workerOnboardingViewModel = WorkerOnboardingViewModel()
        val earningsViewModel = EarningsViewModel()
        val reviewViewModel = ReviewViewModel()
        val notificationViewModel = NotificationViewModel()
        val adminViewModel = AdminViewModel()
        val settingsViewModel = SettingsViewModel(userPreferences)

        setContent {
            FestForgeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    FestForgeNavGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        customerHomeViewModel = customerHomeViewModel,
                        workerSearchViewModel = workerSearchViewModel,
                        workerProfileViewModel = workerProfileViewModel,
                        bookingViewModel = bookingViewModel,
                        bookingHistoryViewModel = bookingHistoryViewModel,
                        workerDashboardViewModel = workerDashboardViewModel,
                        workerOnboardingViewModel = workerOnboardingViewModel,
                        earningsViewModel = earningsViewModel,
                        reviewViewModel = reviewViewModel,
                        notificationViewModel = notificationViewModel,
                        adminViewModel = adminViewModel,
                        settingsViewModel = settingsViewModel
                    )
                }
            }
        }
    }
}
