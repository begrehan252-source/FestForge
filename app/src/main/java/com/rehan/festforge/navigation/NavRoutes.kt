package com.rehan.festforge.navigation

sealed class NavRoutes(val route: String) {
    object Splash : NavRoutes("splash")
    object Login : NavRoutes("login")
    object Otp : NavRoutes("otp")
    object Registration : NavRoutes("registration")

    // Customer
    object CustomerHome : NavRoutes("customer_home")
    object WorkerSearch : NavRoutes("worker_search")
    object WorkerProfile : NavRoutes("worker_profile/{workerId}") {
        fun createRoute(workerId: String) = "worker_profile/$workerId"
    }

    // Booking
    object Booking : NavRoutes("booking/{workerId}") {
        fun createRoute(workerId: String) = "booking/$workerId"
    }
    object BookingSummary : NavRoutes("booking_summary")
    object BookingSuccess : NavRoutes("booking_success/{bookingId}") {
        fun createRoute(bookingId: String) = "booking_success/$bookingId"
    }
    object BookingDetails : NavRoutes("booking_details/{bookingId}") {
        fun createRoute(bookingId: String) = "booking_details/$bookingId"
    }
    object BookingHistory : NavRoutes("booking_history")

    // Worker
    object WorkerDashboard : NavRoutes("worker_dashboard")
    object WorkerRequest : NavRoutes("worker_request/{bookingId}") {
        fun createRoute(bookingId: String) = "worker_request/$bookingId"
    }
    object WorkerRegistration : NavRoutes("worker_registration")
    object WorkerDocumentUpload : NavRoutes("worker_document_upload/{workerId}") {
        fun createRoute(workerId: String) = "worker_document_upload/$workerId"
    }
    object Earnings : NavRoutes("earnings")

    // Shared
    object Review : NavRoutes("review/{bookingId}/{workerId}") {
        fun createRoute(bookingId: String, workerId: String) = "review/$bookingId/$workerId"
    }
    object Notifications : NavRoutes("notifications")
    object Profile : NavRoutes("profile")
    object Settings : NavRoutes("settings")
    object HelpSupport : NavRoutes("help_support")

    // Admin
    object AdminDashboard : NavRoutes("admin_dashboard")
    object AdminVerification : NavRoutes("admin_verification")
    object AdminCategory : NavRoutes("admin_category")
    object AdminUserManagement : NavRoutes("admin_user_management")
}
