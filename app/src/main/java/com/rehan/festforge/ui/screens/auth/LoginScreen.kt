package com.rehan.festforge.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
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
fun LoginScreen(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    onSendOtp: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Fest",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Forge",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary
                )
            }

            Text(
                text = "Welcome to Event Staff Booking",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
            )

            Text(
                text = "Enter Mobile Number",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "We will send a 6-digit OTP to verify your account",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            FestForgeTextField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                label = "Mobile Number",
                placeholder = "9876543210",
                leadingIcon = {
                    Text(
                        text = "+91 ",
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = errorMessage != null,
                errorMessage = errorMessage
            )

            Spacer(modifier = Modifier.height(24.dp))

            FestForgeButton(
                text = "Send OTP",
                onClick = onSendOtp,
                isLoading = isLoading,
                enabled = phoneNumber.length == 10
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "By continuing, you agree to FestForge Terms of Service and Privacy Policy.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
