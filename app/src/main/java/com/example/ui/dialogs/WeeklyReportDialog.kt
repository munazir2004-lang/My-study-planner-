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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.StudySessionEntity
import com.example.data.TaskEntity
import com.example.model.AppLanguage
import com.example.model.Strings

@Composable
fun WeeklyReportDialog(
    tasks: List<TaskEntity>,
    sessions: List<StudySessionEntity>,
    currentStreak: Int,
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val isHindi = language == AppLanguage.HINDI

    val totalPlanned = tasks.size.coerceAtLeast(1)
    val totalCompleted = tasks.count { it.isCompleted }
    val avgPct = ((totalCompleted.toFloat() / totalPlanned) * 100).toInt()

    val totalStudyMinutes = sessions.sumOf { it.durationMinutes } + tasks.filter { it.isCompleted }.sumOf { it.actualMinutesSpent }
    val h = totalStudyMinutes / 60
    val m = totalStudyMinutes % 60
    val studyTimeStr = "${h}h ${m}m"

    val weeklyRemark = when {
        avgPct >= 85 -> if (isHindi) "इस सप्ताह आपकी निरंतरता असाधारण रही! अपनी निर्धारित समय-सारणी का पालन ऐसे ही जारी रखें।"
        else "Excellent consistency this week. Continue following your planned schedule."
        avgPct >= 70 -> if (isHindi) "अच्छा साप्ताहिक प्रदर्शन! आपने अधिकांश लक्ष्य प्राप्त किए। अगले सप्ताह और सुधार करें।"
        else "Great weekly performance! You achieved most goals. Push higher next week."
        else -> if (isHindi) "प्रयास जारी रखें! अगले सप्ताह दैनिक योजना के प्रति अधिक प्रतिबद्ध रहें।"
        else "Keep moving forward! Stay more committed to your daily plan next week."
    }

    val shareText = buildString {
        appendLine(Strings.get("weekly_report_title", language))
        appendLine("────────────")
        appendLine("${Strings.get("study_time", language)}: $studyTimeStr")
        appendLine("${Strings.get("planned_tasks", language)}: $totalPlanned")
        appendLine("${Strings.get("completed_tasks", language)}: $totalCompleted")
        appendLine("${Strings.get("completion_rate", language)}: $avgPct%")
        appendLine("${Strings.get("study_streak", language)}: $currentStreak ${if (isHindi) "दिन" else "Days"}")
        appendLine("────────────")
        appendLine("\"$weeklyRemark\"")
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(MaterialTheme.colorScheme.tertiaryContainer, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = Strings.get("weekly_report_title", language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = Strings.get("cancel", language))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(Strings.get("study_time", language), style = MaterialTheme.typography.bodyMedium)
                            Text(studyTimeStr, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(Strings.get("planned_tasks", language), style = MaterialTheme.typography.bodyMedium)
                            Text("$totalPlanned", fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(Strings.get("completed_tasks", language), style = MaterialTheme.typography.bodyMedium)
                            Text("$totalCompleted", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(Strings.get("completion_rate", language), style = MaterialTheme.typography.bodyMedium)
                            Text("$avgPct%", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(Strings.get("study_streak", language), style = MaterialTheme.typography.bodyMedium)
                            Text("🔥 $currentStreak ${if (isHindi) "दिन" else "Days"}", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.tertiaryContainer
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
                            text = "\"$weeklyRemark\"",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    OutlinedButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Weekly Report"))
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
