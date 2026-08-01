package com.rehan.festforge.ui.screens.worker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rehan.festforge.ui.components.FestForgeButton
import com.rehan.festforge.ui.components.FestForgeTextField
import com.rehan.festforge.ui.theme.GoldPrimary
import com.rehan.festforge.viewmodel.WorkerOnboardingUiState

@Composable
fun WorkerRegistrationScreen(
    state: WorkerOnboardingUiState,
    onUpdateFields: (cat: String, skills: String, city: String, exp: String, rate: String, bio: String, docUri: String?) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Event Staff Onboarding",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Provide your skills and identity proof for verification",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        FestForgeTextField(
            value = state.category,
            onValueChange = { onUpdateFields(it, state.skills, state.city, state.experienceYears, state.hourlyRate, state.bio, state.verificationDocUri) },
            label = "Primary Category",
            placeholder = "Waiter / Bartender / Chef / Helper"
        )

        Spacer(modifier = Modifier.height(12.dp))

        FestForgeTextField(
            value = state.skills,
            onValueChange = { onUpdateFields(state.category, it, state.city, state.experienceYears, state.hourlyRate, state.bio, state.verificationDocUri) },
            label = "Key Skills (Comma Separated)",
            placeholder = "Banquet Service, Cocktails, Fine Dining"
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FestForgeTextField(
                value = state.experienceYears,
                onValueChange = { onUpdateFields(state.category, state.skills, state.city, it, state.hourlyRate, state.bio, state.verificationDocUri) },
                label = "Years Experience",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            FestForgeTextField(
                value = state.hourlyRate,
                onValueChange = { onUpdateFields(state.category, state.skills, state.city, state.experienceYears, it, state.bio, state.verificationDocUri) },
                label = "Hourly Rate (₹)",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        FestForgeTextField(
            value = state.city,
            onValueChange = { onUpdateFields(state.category, state.skills, it, state.experienceYears, state.hourlyRate, state.bio, state.verificationDocUri) },
            label = "Operating City",
            placeholder = "Mumbai"
        )

        Spacer(modifier = Modifier.height(12.dp))

        FestForgeTextField(
            value = state.bio,
            onValueChange = { onUpdateFields(state.category, state.skills, state.city, state.experienceYears, state.hourlyRate, it, state.verificationDocUri) },
            label = "Bio / Background",
            placeholder = "Brief summary of your catering & event background...",
            singleLine = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Aadhaar / Gov ID Verification", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text("Upload document proof for admin approval", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                onUpdateFields(state.category, state.skills, state.city, state.experienceYears, state.hourlyRate, state.bio, "https://storage.googleapis.com/festforge-docs/sample_aadhaar.png")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.UploadFile, contentDescription = null, tint = GoldPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (state.verificationDocUri != null) "Document Attached ✓" else "Attach Identity Proof (Aadhaar / PAN)")
        }

        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        FestForgeButton(
            text = "Submit for Admin Verification",
            onClick = onSubmit,
            isLoading = state.isLoading
        )
    }
}
