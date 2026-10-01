package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.SubjectEntity
import com.example.model.AppLanguage
import com.example.model.Strings
import com.example.ui.viewmodel.PlannerViewModel

@Composable
fun SettingsScreen(
    viewModel: PlannerViewModel
) {
    val language by viewModel.currentLanguage.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val subjects by viewModel.allSubjects.collectAsState()

    var userName by remember(settings) { mutableStateOf(settings.userName) }
    var dailyStudyHours by remember(settings) { mutableFloatStateOf(settings.dailyStudyTargetHours) }
    var dailyTasks by remember(settings) { mutableIntStateOf(settings.dailyTaskTarget) }
    var weeklyStudyHours by remember(settings) { mutableFloatStateOf(settings.weeklyStudyTargetHours) }
    var notifEnabled by remember(settings) { mutableStateOf(settings.notificationsEnabled) }

    // Custom remarks
    var rem100 by remember(settings) { mutableStateOf(settings.customRemark100) }
    var rem90 by remember(settings) { mutableStateOf(settings.customRemark90) }
    var rem75 by remember(settings) { mutableStateOf(settings.customRemark75) }
    var rem60 by remember(settings) { mutableStateOf(settings.customRemark60) }
    var rem40 by remember(settings) { mutableStateOf(settings.customRemark40) }
    var rem0 by remember(settings) { mutableStateOf(settings.customRemark0) }

    var showAddSubjectDialog by remember { mutableStateOf(false) }
    var newSubjectName by remember { mutableStateOf("") }
    var newSubjectColor by remember { mutableStateOf("#4F46E5") }

    var savedFeedback by remember { mutableStateOf(false) }

    val isHindi = language == AppLanguage.HINDI

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("settings_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Language & Profile Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Language,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = Strings.get("language_option", language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FilterChip(
                            selected = language == AppLanguage.HINDI,
                            onClick = { viewModel.setLanguage(AppLanguage.HINDI) },
                            label = { Text("हिन्दी (Hindi)") }
                        )
                        FilterChip(
                            selected = language == AppLanguage.ENGLISH,
                            onClick = { viewModel.setLanguage(AppLanguage.ENGLISH) },
                            label = { Text("English") }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                    Spacer(modifier = Modifier.height(14.dp))

                    // User Name
                    OutlinedTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text(if (isHindi) "विद्यार्थी का नाम" else "Student Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        }

        // Daily & Weekly Targets
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.TrackChanges,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isHindi) "अध्ययन एवं कार्य लक्ष्य (Study Goals)" else "Study & Task Targets",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = dailyStudyHours.toString(),
                        onValueChange = { dailyStudyHours = it.toFloatOrNull() ?: dailyStudyHours },
                        label = { Text(Strings.get("daily_target_hours", language)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = dailyTasks.toString(),
                        onValueChange = { dailyTasks = it.toIntOrNull() ?: dailyTasks },
                        label = { Text(Strings.get("daily_target_tasks", language)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = weeklyStudyHours.toString(),
                        onValueChange = { weeklyStudyHours = it.toFloatOrNull() ?: weeklyStudyHours },
                        label = { Text(Strings.get("weekly_target_hours", language)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        }

        // Notifications & Reminders Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (isHindi) "अनुस्मारक सूचनाएं (Notifications)" else "Notifications",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Switch(
                            checked = notifEnabled,
                            onCheckedChange = { notifEnabled = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = Strings.get("notification_permission_tip", language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.sendTestNotification() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isHindi) "टेस्ट नोटिफिकेशन भेजें" else "Send Test Notification")
                    }
                }
            }
        }

        // Subjects Management
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHindi) "विषय प्रबंधन (Subjects)" else "Subjects Management",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Button(onClick = { showAddSubjectDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isHindi) "जोड़ें" else "Add")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    subjects.forEach { subj ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .background(
                                            try {
                                                Color(android.graphics.Color.parseColor(subj.colorHex))
                                            } catch (e: Exception) {
                                                MaterialTheme.colorScheme.primary
                                            },
                                            CircleShape
                                        )
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(subj.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                            }

                            IconButton(
                                onClick = { viewModel.deleteSubject(subj) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Custom Remarks Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = Strings.get("custom_remarks_title", language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isHindi) "विभिन्न प्रतिशत श्रेणियों के लिए अपनी पसंदीदा टिप्पणी सेट करें:" else "Set your custom remark for different completion rates:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = rem100,
                        onValueChange = { rem100 = it },
                        label = { Text("100% (Full Complete)") },
                        placeholder = { Text("Excellent! You completed your entire plan...") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = rem90,
                        onValueChange = { rem90 = it },
                        label = { Text("90–99%") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = rem75,
                        onValueChange = { rem75 = it },
                        label = { Text("75–89%") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = rem60,
                        onValueChange = { rem60 = it },
                        label = { Text("60–74%") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = rem40,
                        onValueChange = { rem40 = it },
                        label = { Text("40–59%") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = rem0,
                        onValueChange = { rem0 = it },
                        label = { Text("0% (No tasks)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Save All Settings Button
        item {
            Button(
                onClick = {
                    viewModel.updateSettings(
                        settings.copy(
                            userName = userName,
                            dailyStudyTargetHours = dailyStudyHours,
                            dailyTaskTarget = dailyTasks,
                            weeklyStudyTargetHours = weeklyStudyHours,
                            notificationsEnabled = notifEnabled,
                            customRemark100 = rem100,
                            customRemark90 = rem90,
                            customRemark75 = rem75,
                            customRemark60 = rem60,
                            customRemark40 = rem40,
                            customRemark0 = rem0
                        )
                    )
                    savedFeedback = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("save_settings_button")
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (savedFeedback) (if (isHindi) "सेटिंग्स सहेजी गईं ✓" else "Saved! ✓") else Strings.get("save_settings", language))
            }
        }

        item { Spacer(modifier = Modifier.height(60.dp)) }
    }

    // Add Subject Dialog
    if (showAddSubjectDialog) {
        AlertDialog(
            onDismissRequest = { showAddSubjectDialog = false },
            title = { Text(if (isHindi) "नया विषय जोड़ें" else "Add New Subject") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newSubjectName,
                        onValueChange = { newSubjectName = it },
                        label = { Text(Strings.get("subject", language)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(if (isHindi) "रंग चुनें" else "Pick Color")
                    Spacer(modifier = Modifier.height(6.dp))
                    val colors = listOf("#EF4444", "#F59E0B", "#10B981", "#3B82F6", "#8B5CF6", "#EC4899", "#06B6D4")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        colors.forEach { c ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color(android.graphics.Color.parseColor(c)), CircleShape)
                                    .clickable { newSubjectColor = c }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newSubjectName.isNotBlank()) {
                            viewModel.addSubject(newSubjectName.trim(), newSubjectColor)
                            newSubjectName = ""
                            showAddSubjectDialog = false
                        }
                    },
                    enabled = newSubjectName.isNotBlank()
                ) {
                    Text(if (isHindi) "सहेजें" else "Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddSubjectDialog = false }) {
                    Text(Strings.get("cancel", language))
                }
            }
        )
    }
}
