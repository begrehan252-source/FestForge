package com.rehan.festforge.ui.screens.support

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help & Support", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("Frequently Asked Questions", fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(12.dp))

            FaqItem("How do I book staff?", "Browse categories or use search, pick a verified worker, select event shift details and confirm.")
            FaqItem("How are workers verified?", "All workers submit government ID proof (Aadhaar/PAN) which is reviewed by FestForge Admin before listing.")
            FaqItem("When do I pay?", "Payments are settled directly per event shift agreement or online upon booking confirmation.")

            Spacer(modifier = Modifier.height(24.dp))

            Text("Contact Customer Support", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("📞 Phone: +91 1800 123 4567", fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
            Text("✉️ Email: support@festforge.com", fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
private fun FaqItem(question: String, answer: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(question, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(answer, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))
        }
    }
}
