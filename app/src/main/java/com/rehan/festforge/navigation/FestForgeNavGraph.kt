package com.rehan.festforge.navigation

import android.app.Activity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rehan.festforge.data.model.UserRole
import com.rehan.festforge.ui.screens.admin.*
import com.rehan.festforge.ui.screens.auth.*
import com.rehan.festforge.ui.screens.booking.*
import com.rehan.festforge.ui.screens.customer.*
import com.rehan.festforge.ui.screens.notifications.NotificationsScreen
import com.rehan.festforge.ui.screens.profile.ProfileScreen
import com.rehan.festforge.ui.screens.review.ReviewScreen
import com.rehan.festforge.ui.screens.settings.SettingsScreen
import com.rehan.festforge.ui.screens.splash.SplashScreen
import com.rehan.festforge.ui.screens.support.HelpSupportScreen
import com.rehan.festforge.ui.screens.worker.*
import com.rehan.festforge.viewmodel.*

@Composable
fun FestForgeNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    customerHomeViewModel: CustomerHomeViewModel,
    workerSearchViewModel: WorkerSearchViewModel,
    workerProfileViewModel: WorkerProfileViewModel,
    bookingViewModel: BookingViewModel,
    bookingHistoryViewModel: BookingHistoryViewModel,
    workerDashboardViewModel: WorkerDashboardViewModel,
    workerOnboardingViewModel: WorkerOnboardingViewModel,
    earningsViewModel: EarningsViewModel,
    reviewViewModel: ReviewViewModel,
    notificationViewModel: NotificationViewModel,
    adminViewModel: AdminViewModel,
    settingsViewModel: SettingsViewModel
) {
    val authState by authViewModel.uiState.collectAsState()
    val activity = LocalContext.current as Activity

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash.route
    ) {
        composable(NavRoutes.Splash.route) {
            SplashScreen(
                currentUser = authState.userProfile,
                isAuthenticated = authState.isAuthenticated,
                onNavigateToLogin = {
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(NavRoutes.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToCustomerHome = {
                    authState.userProfile?.let { customerHomeViewModel.loadData(it.id) }
                    navController.navigate(NavRoutes.CustomerHome.route) {
                        popUpTo(NavRoutes.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToWorkerDashboard = {
                    authState.userProfile?.let { workerDashboardViewModel.loadDashboard(it.id) }
                    navController.navigate(NavRoutes.WorkerDashboard.route) {
                        popUpTo(NavRoutes.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToAdminDashboard = {
                    adminViewModel.loadAdminData()
                    navController.navigate(NavRoutes.AdminDashboard.route) {
                        popUpTo(NavRoutes.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.Login.route) {
            LoginScreen(
                phoneNumber = authState.phoneNumber,
                onPhoneNumberChange = authViewModel::onPhoneNumberChange,
                onSendOtp = {
                    authViewModel.sendOtp(activity, {
                        navController.navigate(NavRoutes.Otp.route)
                    })
                },
                isLoading = authState.isLoading,
                errorMessage = authState.errorMessage
            )
        }

        composable(NavRoutes.Otp.route) {
            OtpScreen(
                phoneNumber = authState.phoneNumber,
                otpCode = authState.otpCode,
                onOtpChange = authViewModel::onOtpCodeChange,
                onVerifyOtp = {
                    authViewModel.verifyOtp { user ->
                        if (user != null) {
                            when (user.role) {
                                UserRole.CUSTOMER -> {
                                    customerHomeViewModel.loadData(user.id)
                                    navController.navigate(NavRoutes.CustomerHome.route) { popUpTo(NavRoutes.Login.route) { inclusive = true } }
                                }
                                UserRole.WORKER -> {
                                    workerDashboardViewModel.loadDashboard(user.id)
                                    navController.navigate(NavRoutes.WorkerDashboard.route) { popUpTo(NavRoutes.Login.route) { inclusive = true } }
                                }
                                UserRole.ADMIN -> {
                                    adminViewModel.loadAdminData()
                                    navController.navigate(NavRoutes.AdminDashboard.route) { popUpTo(NavRoutes.Login.route) { inclusive = true } }
                                }
                            }
                        } else {
                            navController.navigate(NavRoutes.Registration.route)
                        }
                    }
                },
                onResendOtp = { authViewModel.sendOtp(activity, {}, forceResend = true) },
                onChangeNumber = { navController.popBackStack() },
                isLoading = authState.isLoading,
                errorMessage = authState.errorMessage,
                timerSeconds = authState.timerSeconds
            )
        }

        composable(NavRoutes.Registration.route) {
            RegistrationScreen(
                onRegister = { name, city, role ->
                    authViewModel.registerUser(name, city, role) { newUser ->
                        when (role) {
                            UserRole.CUSTOMER -> {
                                customerHomeViewModel.loadData(newUser.id)
                                navController.navigate(NavRoutes.CustomerHome.route) { popUpTo(NavRoutes.Login.route) { inclusive = true } }
                            }
                            UserRole.WORKER -> {
                                navController.navigate(NavRoutes.WorkerRegistration.route) { popUpTo(NavRoutes.Login.route) { inclusive = true } }
                            }
                            UserRole.ADMIN -> {}
                        }
                    }
                },
                isLoading = authState.isLoading,
                errorMessage = authState.errorMessage
            )
        }

        composable(NavRoutes.CustomerHome.route) {
            val state by customerHomeViewModel.uiState.collectAsState()
            CustomerHomeScreen(
                userName = authState.userProfile?.name ?: "Customer",
                userCity = authState.userProfile?.city ?: "Mumbai",
                categories = state.categories,
                popularWorkers = state.popularWorkers,
                upcomingBooking = state.upcomingBooking,
                onNavigateToSearch = { navController.navigate(NavRoutes.WorkerSearch.route) },
                onNavigateToWorkerProfile = { workerId ->
                    workerProfileViewModel.loadWorkerProfile(workerId)
                    navController.navigate(NavRoutes.WorkerProfile.createRoute(workerId))
                },
                onNavigateToBookingDetails = { bookingId ->
                    navController.navigate(NavRoutes.BookingDetails.createRoute(bookingId))
                },
                onNavigateToNotifications = {
                    authState.userProfile?.let { notificationViewModel.loadNotifications(it.id) }
                    navController.navigate(NavRoutes.Notifications.route)
                }
            )
        }

        composable(NavRoutes.WorkerSearch.route) {
            val state by workerSearchViewModel.uiState.collectAsState()
            WorkerSearchScreen(
                workers = state.workers,
                isLoading = state.isLoading,
                filterState = state.filterState,
                onFilterChange = workerSearchViewModel::updateFilters,
                onSelectWorker = { workerId ->
                    workerProfileViewModel.loadWorkerProfile(workerId)
                    navController.navigate(NavRoutes.WorkerProfile.createRoute(workerId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.WorkerProfile.route,
            arguments = listOf(navArgument("workerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val workerId = backStackEntry.arguments?.getString("workerId") ?: ""
            val state by workerProfileViewModel.uiState.collectAsState()

            WorkerProfileScreen(
                worker = state.worker,
                reviews = state.reviews,
                isLoading = state.isLoading,
                onBookNow = {
                    state.worker?.let { bookingViewModel.setWorker(it) }
                    navController.navigate(NavRoutes.Booking.createRoute(workerId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.Booking.route,
            arguments = listOf(navArgument("workerId") { type = NavType.StringType })
        ) {
            val bookingState by bookingViewModel.uiState.collectAsState()
            BookingScreen(
                bookingState = bookingState,
                onUpdateFields = bookingViewModel::updateBookingFields,
                onProceedToSummary = { navController.navigate(NavRoutes.BookingSummary.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.BookingSummary.route) {
            val bookingState by bookingViewModel.uiState.collectAsState()
            BookingSummaryScreen(
                bookingState = bookingState,
                onConfirmBooking = {
                    bookingViewModel.confirmBooking(
                        customerId = authState.userProfile?.id.orEmpty(),
                        customerName = authState.userProfile?.name ?: "Customer",
                        customerPhone = authState.userProfile?.phone.orEmpty()
                    ) { bookingId ->
                        navController.navigate(NavRoutes.BookingSuccess.createRoute(bookingId)) {
                            popUpTo(NavRoutes.CustomerHome.route)
                        }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.BookingSuccess.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
            BookingSuccessScreen(
                bookingId = bookingId,
                onGoToBookings = {
                    authState.userProfile?.let { bookingHistoryViewModel.loadBookings(it.id, false) }
                    navController.navigate(NavRoutes.BookingHistory.route) {
                        popUpTo(NavRoutes.CustomerHome.route)
                    }
                }
            )
        }

        composable(NavRoutes.BookingHistory.route) {
            val state by bookingHistoryViewModel.uiState.collectAsState()
            BookingHistoryScreen(
                bookings = state.filteredBookings,
                selectedTab = state.selectedTab,
                isLoading = state.isLoading,
                onTabSelected = bookingHistoryViewModel::selectTab,
                onSelectBooking = { id -> navController.navigate(NavRoutes.BookingDetails.createRoute(id)) }
            )
        }

        composable(
            route = NavRoutes.BookingDetails.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
            BookingDetailsScreen(
                booking = null,
                isLoading = false,
                onRateWorker = { bId, wId -> navController.navigate(NavRoutes.Review.createRoute(bId, wId)) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.WorkerDashboard.route) {
            val state by workerDashboardViewModel.uiState.collectAsState()
            WorkerDashboardScreen(
                profile = state.workerProfile,
                pendingRequests = state.pendingRequests,
                todayJobs = state.todayJobs,
                isAvailable = state.isAvailable,
                todayEarnings = state.todayEarnings,
                isLoading = state.isLoading,
                onToggleAvailability = { isAvail ->
                    authState.userProfile?.let { workerDashboardViewModel.toggleAvailability(it.id, isAvail) }
                },
                onViewRequest = { bId -> navController.navigate(NavRoutes.WorkerRequest.createRoute(bId)) },
                onViewEarnings = {
                    authState.userProfile?.let { earningsViewModel.loadEarnings(it.id) }
                    navController.navigate(NavRoutes.Earnings.route)
                },
                onNavigateToNotifications = {
                    authState.userProfile?.let { notificationViewModel.loadNotifications(it.id) }
                    navController.navigate(NavRoutes.Notifications.route)
                }
            )
        }

        composable(
            route = NavRoutes.WorkerRequest.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
            WorkerRequestScreen(
                booking = null,
                onAccept = {
                    authState.userProfile?.let { workerDashboardViewModel.respondToRequest(bookingId, true, it.id) }
                    navController.popBackStack()
                },
                onReject = {
                    authState.userProfile?.let { workerDashboardViewModel.respondToRequest(bookingId, false, it.id) }
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.WorkerRegistration.route) {
            val state by workerOnboardingViewModel.uiState.collectAsState()
            WorkerRegistrationScreen(
                state = state,
                onUpdateFields = workerOnboardingViewModel::updateFields,
                onSubmit = {
                    authState.userProfile?.let {
                        workerOnboardingViewModel.submitOnboarding(it.id, it.name, it.phone) {
                            workerDashboardViewModel.loadDashboard(it.id)
                            navController.navigate(NavRoutes.WorkerDashboard.route) { popUpTo(NavRoutes.Login.route) { inclusive = true } }
                        }
                    }
                }
            )
        }

        composable(
            route = NavRoutes.WorkerDocumentUpload.route,
            arguments = listOf(navArgument("workerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val workerId = backStackEntry.arguments?.getString("workerId") ?: ""
            WorkerDocumentUploadScreen(
                workerId = workerId,
                onDocumentUploaded = { frontUrl, backUrl ->
                    // Set document state to Document Pending Review
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.Earnings.route) {
            val state by earningsViewModel.uiState.collectAsState()
            EarningsScreen(
                todayEarnings = state.todayEarnings,
                weeklyEarnings = state.weeklyEarnings,
                totalEarnings = state.totalEarnings,
                completedJobsCount = state.completedJobsCount,
                completedBookings = state.completedBookings,
                isLoading = state.isLoading,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.Review.route,
            arguments = listOf(
                navArgument("bookingId") { type = NavType.StringType },
                navArgument("workerId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
            val workerId = backStackEntry.arguments?.getString("workerId") ?: ""
            val state by reviewViewModel.uiState.collectAsState()

            ReviewScreen(
                rating = state.rating,
                comment = state.comment,
                isLoading = state.isLoading,
                errorMessage = state.errorMessage,
                onRatingChange = reviewViewModel::updateRating,
                onCommentChange = reviewViewModel::updateComment,
                onSubmit = {
                    reviewViewModel.submitReview(
                        bookingId = bookingId,
                        customerId = authState.userProfile?.id ?: "",
                        customerName = authState.userProfile?.name ?: "Customer",
                        workerId = workerId,
                        onSuccess = { navController.popBackStack() }
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.Notifications.route) {
            val state by notificationViewModel.uiState.collectAsState()
            NotificationsScreen(
                notifications = state.notifications,
                isLoading = state.isLoading,
                onMarkRead = { id -> authState.userProfile?.let { notificationViewModel.markRead(id, it.id) } },
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.Profile.route) {
            ProfileScreen(
                user = authState.userProfile,
                onNavigateToBookings = { navController.navigate(NavRoutes.BookingHistory.route) },
                onNavigateToSettings = { navController.navigate(NavRoutes.Settings.route) },
                onNavigateToHelp = { navController.navigate(NavRoutes.HelpSupport.route) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.Settings.route) {
            val state by settingsViewModel.uiState.collectAsState()
            SettingsScreen(
                themeMode = state.themeMode,
                notificationsEnabled = state.notificationsEnabled,
                onSetThemeMode = settingsViewModel::setThemeMode,
                onSetNotificationsEnabled = settingsViewModel::setNotificationsEnabled,
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.HelpSupport.route) {
            HelpSupportScreen(onBack = { navController.popBackStack() })
        }

        composable(NavRoutes.AdminDashboard.route) {
            val state by adminViewModel.uiState.collectAsState()
            AdminDashboardScreen(
                pendingVerifications = state.pendingVerifications,
                allWorkersCount = state.allWorkers.size,
                allUsersCount = state.allUsers.size,
                allBookingsCount = state.allBookings.size,
                totalRevenue = state.totalRevenue,
                onNavigateToVerifications = { navController.navigate(NavRoutes.AdminVerification.route) },
                onNavigateToCategories = { navController.navigate(NavRoutes.AdminCategory.route) },
                onNavigateToUsers = { navController.navigate(NavRoutes.AdminUserManagement.route) }
            )
        }

        composable(NavRoutes.AdminVerification.route) {
            val state by adminViewModel.uiState.collectAsState()
            AdminVerificationScreen(
                pendingWorkers = state.pendingVerifications,
                isLoading = state.isLoading,
                onApprove = adminViewModel::approveWorker,
                onReject = adminViewModel::rejectWorker,
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.AdminCategory.route) {
            val state by adminViewModel.uiState.collectAsState()
            AdminCategoryScreen(
                categories = state.categories,
                onAddCategory = adminViewModel::addCategory,
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.AdminUserManagement.route) {
            val state by adminViewModel.uiState.collectAsState()
            AdminUserManagementScreen(
                users = state.allUsers,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
