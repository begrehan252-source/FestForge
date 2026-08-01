package com.rehan.festforge.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rehan.festforge.data.model.UserRole
import com.rehan.festforge.ui.components.FestForgeButton
import com.rehan.festforge.ui.components.FestForgeTextField
import com.rehan.festforge.ui.theme.DarkBackground
import com.rehan.festforge.ui.theme.GoldPrimary

@Composable
fun RegistrationScreen(
    onRegister: (name: String, city: String, role: UserRole) -> Unit,
    isLoading: Boolean,
    errorMessage: String?
) {
    var name by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Mumbai") }
    var selectedRole by remember { mutableStateOf(UserRole.CUSTOMER) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Complete Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Tell us about yourself to complete registration",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            FestForgeTextField(
                value = name,
                onValueChange = { name = it },
                label = "Full Name",
                placeholder = "e.g. Rahul Sharma"
            )

            Spacer(modifier = Modifier.height(16.dp))

            FestForgeTextField(
                value = city,
                onValueChange = { city = it },
                label = "City",
                placeholder = "e.g. Mumbai, Delhi, Bengaluru"
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "I am joining as",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { selectedRole = UserRole.CUSTOMER },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (selectedRole == UserRole.CUSTOMER) GoldPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = "Customer\n(Book Staff)",
                        fontWeight = FontWeight.Bold,
                        color = if (selectedRole == UserRole.CUSTOMER) GoldPrimary else MaterialTheme.colorScheme.onSurface
                    )
                }

                OutlinedButton(
                    onClick = { selectedRole = UserRole.WORKER },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (selectedRole == UserRole.WORKER) GoldPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = "Event Staff\n(Provide Service)",
                        fontWeight = FontWeight.Bold,
                        color = if (selectedRole == UserRole.WORKER) GoldPrimary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            FestForgeButton(
                text = "Complete Registration",
                onClick = { onRegister(name, city, selectedRole) },
                isLoading = isLoading,
                enabled = name.isNotBlank() && city.isNotBlank()
            )
        }
    }
}
