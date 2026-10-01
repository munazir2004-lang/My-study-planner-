package com.example.ui.dialogs

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.StudySessionEntity
import com.example.data.TaskEntity
import com.example.model.AppLanguage
import com.example.model.PerformanceMetrics
import com.example.model.RemarkCalculator
import com.example.model.Strings

@Composable
fun EndOfDayReportDialog(
    dateStr: String,
    tasks: List<TaskEntity>,
    sessions: List<StudySessionEntity>,
    metrics: PerformanceMetrics,
    remark: String,
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val isHindi = language == AppLanguage.HINDI

    // Calculate most studied subject
    val subjectTimes = mutableMapOf<String, Int>()
    sessions.forEach { s ->
        subjectTimes[s.subject] = (subjectTimes[s.subject] ?: 0) + s.durationMinutes
    }
    tasks.filter { it.isCompleted }.forEach { t ->
        subjectTimes[t.subject] = (subjectTimes[t.subject] ?: 0) + t.actualMinutesSpent
    }
    val mostStudied = subjectTimes.maxByOrNull { it.value }
    val mostStudiedText = if (mostStudied != null && mostStudied.value > 0) {
        val h = mostStudied.value / 60
        val m = mostStudied.value % 60
        "${mostStudied.key} — ${if (h > 0) "${h}h " else ""}${m}m"
    } else {
        if (isHindi) "कोई डेटा नहीं" else "N/A"
    }

    val totalHours = metrics.totalStudyMinutes / 60
    val totalMins = metrics.totalStudyMinutes % 60
    val studyTimeStr = "${totalHours}h ${totalMins}m"

    val reportText = buildString {
        appendLine(Strings.get("daily_report_title", language))
        appendLine("📅 $dateStr")
        appendLine("────────────")
        appendLine("${Strings.get("planned_tasks", language)}: ${metrics.plannedTasks}")
        appendLine("${Strings.get("completed_tasks", language)}: ${metrics.completedTasks}")
        appendLine("${Strings.get("pending_tasks", language)}: ${metrics.pendingTasks}")
        appendLine("${Strings.get("completion_rate", language)}: ${metrics.completionPercentage}%")
        appendLine("${Strings.get("study_time", language)}: $studyTimeStr")
        appendLine("${Strings.get("most_studied_subject", language)}: $mostStudiedText")
        appendLine("────────────")
        appendLine("${Strings.get("today_remark", language)}: \"$remark\"")
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = Strings.get("daily_report_title", language),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = dateStr,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = Strings.get("cancel", language))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Matrix
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(Strings.get("planned_tasks", language), style = MaterialTheme.typography.bodyMedium)
                            Text("${metrics.plannedTasks}", fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(Strings.get("completed_tasks", language), style = MaterialTheme.typography.bodyMedium)
                            Text("${metrics.completedTasks}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(Strings.get("pending_tasks", language), style = MaterialTheme.typography.bodyMedium)
                            Text("${metrics.pendingTasks}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(Strings.get("completion_rate", language), style = MaterialTheme.typography.bodyMedium)
                            Text("${metrics.completionPercentage}%", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(Strings.get("study_time", language), style = MaterialTheme.typography.bodyMedium)
                            Text(studyTimeStr, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(Strings.get("most_studied_subject", language), style = MaterialTheme.typography.bodyMedium)
                            Text(mostStudiedText, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Remark Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.secondaryContainer
                                )
                            ),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = Strings.get("today_remark", language),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "\"$remark\"",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Bottom Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, reportText)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Study Report"))
                        }
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isHindi) "शेयर करें" else "Share")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onDismiss) {
                        Text(if (isHindi) "ठीक है" else "Close")
                    }
                }
            }
        }
    }
}
