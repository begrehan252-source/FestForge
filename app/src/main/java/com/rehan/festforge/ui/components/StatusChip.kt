package com.rehan.festforge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rehan.festforge.data.model.BookingStatus
import com.rehan.festforge.ui.theme.StatusBlue
import com.rehan.festforge.ui.theme.StatusGreen
import com.rehan.festforge.ui.theme.StatusOrange
import com.rehan.festforge.ui.theme.StatusRed

@Composable
fun StatusChip(status: BookingStatus) {
    val (bgColor, textColor, label) = when (status) {
        BookingStatus.PENDING -> Triple(StatusOrange.copy(alpha = 0.15f), StatusOrange, "PENDING")
        BookingStatus.ACCEPTED -> Triple(StatusBlue.copy(alpha = 0.15f), StatusBlue, "ACCEPTED")
        BookingStatus.IN_PROGRESS -> Triple(StatusBlue.copy(alpha = 0.25f), StatusBlue, "IN PROGRESS")
        BookingStatus.COMPLETED -> Triple(StatusGreen.copy(alpha = 0.15f), StatusGreen, "COMPLETED")
        BookingStatus.CANCELLED -> Triple(StatusRed.copy(alpha = 0.15f), StatusRed, "CANCELLED")
    }

    Text(
        text = label,
        color = textColor,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(bgColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}
