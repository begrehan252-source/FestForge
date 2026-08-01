package com.rehan.festforge.data.model

data class AppConfig(
    val platformFeePercent: Double = 5.0,
    val isCashOnEventAllowed: Boolean = true,
    val supportPhone: String = "+91 1800 123 4567",
    val supportEmail: String = "support@festforge.com"
)
