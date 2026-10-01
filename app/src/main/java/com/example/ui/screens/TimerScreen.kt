package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.Strings
import com.example.ui.theme.CyanSecondary
import com.example.ui.theme.StatusSuccess
import com.example.ui.viewmodel.PlannerViewModel
import com.example.ui.viewmodel.TimerMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    viewModel: PlannerViewModel
) {
    val language by viewModel.currentLanguage.collectAsState()
    val timerState by viewModel.timerState.collectAsState()
    val subjects by viewModel.allSubjects.collectAsState()
    val todaySessions by viewModel.todayStudySessions.collectAsState()

    var showCustomDialog by remember { mutableStateOf(false) }
    var customMinutes by remember { mutableFloatStateOf(30f) }
    var subjectMenuExpanded by remember { mutableStateOf(false) }

    val minutes = timerState.remainingSeconds / 60
    val seconds = timerState.remainingSeconds % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    val progress = if (timerState.totalSeconds > 0) {
        timerState.remainingSeconds.toFloat() / timerState.totalSeconds
    } else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "timerProgress")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("timer_screen")
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Preset Chips Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = timerState.mode == TimerMode.POMODORO_25,
                    onClick = { viewModel.setTimerMode(TimerMode.POMODORO_25) },
                    label = { Text("25m") }
                )
                FilterChip(
                    selected = timerState.mode == TimerMode.SESSION_50,
                    onClick = { viewModel.setTimerMode(TimerMode.SESSION_50) },
                    label = { Text("50m") }
                )
                FilterChip(
                    selected = timerState.mode == TimerMode.DEEP_STUDY_90,
                    onClick = { viewModel.setTimerMode(TimerMode.DEEP_STUDY_90) },
                    label = { Text("90m") }
                )
                FilterChip(
                    selected = timerState.mode == TimerMode.CUSTOM,
                    onClick = { showCustomDialog = true },
                    label = { Text(Strings.get("timer_custom", language)) }
                )
            }
        }

        // Subject Selector
        item {
            ExposedDropdownMenuBox(
                expanded = subjectMenuExpanded,
                onExpandedChange = { if (!timerState.isRunning) subjectMenuExpanded = !subjectMenuExpanded },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                OutlinedTextField(
                    value = timerState.selectedSubject,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(Strings.get("timer_select_subject", language)) },
                    leadingIcon = { Icon(Icons.Default.Category, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = subjectMenuExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = subjectMenuExpanded,
                    onDismissRequest = { subjectMenuExpanded = false }
                ) {
                    subjects.forEach { subj ->
                        DropdownMenuItem(
                            text = { Text(subj.name) },
                            onClick = {
                                viewModel.setTimerSubject(subj.name)
                                subjectMenuExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // Circular Timer Display
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(260.dp)
            ) {
                // Background Track
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.size(260.dp),
                    strokeWidth = 14.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                // Active Countdown Arc
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size(260.dp),
                    strokeWidth = 14.dp,
                    color = if (timerState.isRunning) MaterialTheme.colorScheme.primary else CyanSecondary
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = timerState.selectedSubject,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Timer Control Buttons
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reset Button
                IconButton(
                    onClick = { viewModel.resetTimer() },
                    modifier = Modifier
                        .size(52.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = Strings.get("timer_reset", language),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Main Play / Pause Button
                Button(
                    onClick = {
                        if (timerState.isRunning && !timerState.isPaused) {
                            viewModel.pauseTimer()
                        } else if (timerState.isPaused) {
                            viewModel.resumeTimer()
                        } else {
                            viewModel.startTimer()
                        }
                    },
                    modifier = Modifier
                        .size(72.dp)
                        .testTag("timer_play_pause_button"),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (timerState.isRunning && !timerState.isPaused)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        if (timerState.isRunning && !timerState.isPaused) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (timerState.isRunning && !timerState.isPaused) Strings.get("timer_pause", language) else Strings.get("timer_start", language),
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Stop & Save Button
                IconButton(
                    onClick = { viewModel.stopTimerAndSave() },
                    modifier = Modifier
                        .size(52.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                ) {
                    Icon(
                        Icons.Default.Stop,
                        contentDescription = Strings.get("timer_stop", language),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

        // Recent Sessions Section Header
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.History,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = Strings.get("timer_recent_sessions", language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "${todaySessions.size} ${if (language == AppLanguage.HINDI) "सत्र आज" else "sessions today"}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Sessions List
        if (todaySessions.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "आज कोई अध्ययन सत्र दर्ज नहीं हुआ है" else "No sessions recorded today",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(todaySessions, key = { it.id }) { session ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = session.subject,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            if (session.notes.isNotBlank()) {
                                Text(
                                    text = session.notes,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        val h = session.durationMinutes / 60
                        val m = session.durationMinutes % 60
                        val durStr = if (h > 0) "${h}h ${m}m" else "${m}m"

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = StatusSuccess.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = durStr,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = StatusSuccess,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(60.dp)) }
    }

    // Custom Duration Dialog
    if (showCustomDialog) {
        AlertDialog(
            onDismissRequest = { showCustomDialog = false },
            title = { Text(Strings.get("timer_custom", language)) },
            text = {
                Column {
                    Text("${customMinutes.toInt()} ${if (language == AppLanguage.HINDI) "मिनट" else "minutes"}")
                    Slider(
                        value = customMinutes,
                        onValueChange = { customMinutes = it },
                        valueRange = 5f..180f,
                        steps = 34
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.setTimerMode(TimerMode.CUSTOM, customMinutes.toInt())
                        showCustomDialog = false
                    }
                ) {
                    Text(if (language == AppLanguage.HINDI) "लागू करें" else "Set")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomDialog = false }) {
                    Text(Strings.get("cancel", language))
                }
            }
        )
    }
}
