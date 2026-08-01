package com.rehan.festforge.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rehan.festforge.ui.components.FestForgeButton
import com.rehan.festforge.ui.components.FestForgeTextField
import com.rehan.festforge.ui.theme.DarkBackground
import com.rehan.festforge.ui.theme.GoldPrimary

@Composable
fun OtpScreen(
    phoneNumber: String,
    otpCode: String,
    onOtpChange: (String) -> Unit,
    onVerifyOtp: () -> Unit,
    onResendOtp: () -> Unit,
    onChangeNumber: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    timerSeconds: Int
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Verify Mobile Number",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Enter 6-digit verification code sent to +91 $phoneNumber",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            FestForgeTextField(
                value = otpCode,
                onValueChange = onOtpChange,
                label = "OTP Code",
                placeholder = "6-digit OTP",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                isError = errorMessage != null,
                errorMessage = errorMessage
            )

            Spacer(modifier = Modifier.height(24.dp))

            FestForgeButton(
                text = "Verify & Proceed",
                onClick = onVerifyOtp,
                isLoading = isLoading,
                enabled = otpCode.length == 6
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Change Number",
                    fontSize = 13.sp,
                    color = GoldPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onChangeNumber() }
                )

                if (timerSeconds > 0) {
                    Text(
                        text = "Resend in ${timerSeconds}s",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = "Resend OTP",
                        fontSize = 13.sp,
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onResendOtp() }
                    )
                }
            }
        }
    }
}
