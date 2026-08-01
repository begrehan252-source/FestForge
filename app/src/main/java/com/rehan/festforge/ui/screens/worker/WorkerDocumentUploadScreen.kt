package com.rehan.festforge.ui.screens.worker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rehan.festforge.ui.components.FestForgeButton
import com.rehan.festforge.ui.theme.GoldPrimary
import com.rehan.festforge.ui.theme.StatusGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerDocumentUploadScreen(
    workerId: String,
    onDocumentUploaded: (frontUrl: String, backUrl: String) -> Unit,
    onBack: () -> Unit
) {
    var frontImageUri by remember { mutableStateOf<String?>(null) }
    var backImageUri by remember { mutableStateOf<String?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableStateOf(0f) }
    var isSubmitted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aadhaar Card Upload", fontWeight = FontWeight.Bold) },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(32.dp))
                    Column {
                        Text("Government Aadhaar Review", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(
                            "Upload clear physical copies of front and back sides of your Aadhaar card for Admin verification.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Document Upload Slots
            Text("Aadhaar Front Side *", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, if (frontImageUri != null) StatusGreen else MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                    .clickable {
                        // Attach sample front Aadhaar image copy URL
                        frontImageUri = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=400"
                    },
                contentAlignment = Alignment.Center
            ) {
                if (frontImageUri == null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Tap to Select Aadhaar Front Image", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Text("PNG, JPG up to 5MB (Firebase Storage)", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusGreen, modifier = Modifier.size(28.dp))
                        Column {
                            Text("Aadhaar Front Selected ✓", fontWeight = FontWeight.Bold, color = StatusGreen, fontSize = 13.sp)
                            Text("aadhaar_front_copy.jpg (Ready for Storage)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            Text("Aadhaar Back Side *", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, if (backImageUri != null) StatusGreen else MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                    .clickable {
                        // Attach sample back Aadhaar image copy URL
                        backImageUri = "https://images.unsplash.com/photo-1579546929518-9e396f3cc809?w=400"
                    },
                contentAlignment = Alignment.Center
            ) {
                if (backImageUri == null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Tap to Select Aadhaar Back Image", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Text("Contains Address & UIDAI Security Seal", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusGreen, modifier = Modifier.size(28.dp))
                        Column {
                            Text("Aadhaar Back Selected ✓", fontWeight = FontWeight.Bold, color = StatusGreen, fontSize = 13.sp)
                            Text("aadhaar_back_copy.jpg (Ready for Storage)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            if (isUploading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LinearProgressIndicator(
                        progress = { uploadProgress },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Uploading physical copies to Firebase Storage...",
                        fontSize = 12.sp,
                        color = GoldPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            if (isSubmitted) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusGreen)
                            Text("Status Tracker Updated: Document Pending Review", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Text(
                            "Your physical Aadhaar card images have been securely uploaded to Firebase Storage. Admins have been notified for review.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            FestForgeButton(
                text = if (isSubmitted) "Return to Dashboard" else "Upload to Firebase Storage & Submit",
                onClick = {
                    if (isSubmitted) {
                        onBack()
                    } else if (frontImageUri != null && backImageUri != null) {
                        isUploading = true
                        uploadProgress = 0.4f
                        // Simulate Firebase Storage upload
                        onDocumentUploaded(frontImageUri!!, backImageUri!!)
                        isUploading = false
                        uploadProgress = 1.0f
                        isSubmitted = true
                    }
                },
                enabled = isSubmitted || (frontImageUri != null && backImageUri != null)
            )
        }
    }
}
