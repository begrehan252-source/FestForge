package com.rehan.festforge.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rehan.festforge.data.model.WorkerProfile
import com.rehan.festforge.ui.theme.StatusGreen
import com.rehan.festforge.ui.theme.StatusRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminVerificationScreen(
    pendingWorkers: List<WorkerProfile>,
    isLoading: Boolean,
    onApprove: (workerId: String) -> Unit,
    onReject: (workerId: String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pending Worker Verifications", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (pendingWorkers.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No pending worker verification requests.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pendingWorkers) { worker ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(worker.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("${worker.category} • ${worker.city} • ${worker.phone}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Experience: ${worker.experienceYears} Yrs • Rate: ₹${worker.hourlyRate.toInt()}/hr", fontSize = 13.sp)

                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Document URL: ${worker.verificationDocUrl.ifEmpty { "Attached Identity File" }}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedButton(
                                    onClick = { onReject(worker.id) },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusRed)
                                ) {
                                    Text("Reject", fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = { onApprove(worker.id) },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = StatusGreen)
                                ) {
                                    Text("Approve ✓", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
