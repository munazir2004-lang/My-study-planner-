package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.Strings
import com.example.ui.dialogs.WeeklyReportDialog
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanSecondary
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusSuccess
import com.example.ui.viewmodel.PlannerViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun AnalyticsScreen(
    viewModel: PlannerViewModel
) {
    val language by viewModel.currentLanguage.collectAsState()
    val allTasks by viewModel.allTasks.collectAsState()
    val allSessions by viewModel.allStudySessions.collectAsState()
    val subjects by viewModel.allSubjects.collectAsState()
    val streak by viewModel.streakStats.collectAsState()
    val settings by viewModel.settings.collectAsState()

    var showWeeklyReportDialog by remember { mutableStateOf(false) }

    val isHindi = language == AppLanguage.HINDI

    // Total stats
    val totalPlanned = allTasks.size.coerceAtLeast(1)
    val totalCompleted = allTasks.count { it.isCompleted }
    val totalPending = allTasks.count { !it.isCompleted }
    val avgCompletion = ((totalCompleted.toFloat() / totalPlanned) * 100).toInt()

    val totalStudyMinutes = allSessions.sumOf { it.durationMinutes } + allTasks.filter { it.isCompleted }.sumOf { it.actualMinutesSpent }
    val totalHours = totalStudyMinutes / 60
    val totalMins = totalStudyMinutes % 60
    val totalStudyTimeStr = "${totalHours}h ${totalMins}m"

    // Subject breakdown
    val subjectStats = remember(allTasks, allSessions, subjects) {
        subjects.map { subj ->
            val subjTasks = allTasks.filter { it.subject.equals(subj.name, ignoreCase = true) }
            val completed = subjTasks.count { it.isCompleted }
            val pending = subjTasks.count { !it.isCompleted }
            val taskTime = subjTasks.filter { it.isCompleted }.sumOf { it.actualMinutesSpent }
            val sessionTime = allSessions.filter { it.subject.equals(subj.name, ignoreCase = true) }.sumOf { it.durationMinutes }
            val totalM = taskTime + sessionTime
            val pct = if (subjTasks.isNotEmpty()) ((completed.toFloat() / subjTasks.size) * 100).toInt() else 0
            SubjectStat(
                name = subj.name,
                totalMinutes = totalM,
                completedTasks = completed,
                pendingTasks = pending,
                completionPct = pct,
                colorHex = subj.colorHex
            )
        }.sortedByDescending { it.totalMinutes }
    }

    val mostStudied = subjectStats.firstOrNull { it.totalMinutes > 0 }

    // Last 7 days study hours for bar chart
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dayFormat = SimpleDateFormat("EEE", if (isHindi) Locale("hi", "IN") else Locale.ENGLISH)
    val last7Days = remember(allTasks, allSessions) {
        val list = mutableListOf<DayStat>()
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -6)
        for (i in 0..6) {
            val dStr = sdf.format(cal.time)
            val dLabel = dayFormat.format(cal.time)
            val daySessions = allSessions.filter { it.date == dStr }.sumOf { it.durationMinutes }
            val dayTaskMins = allTasks.filter { it.startDate == dStr && it.isCompleted }.sumOf { it.actualMinutesSpent }
            val mins = daySessions + dayTaskMins
            list.add(DayStat(dateStr = dStr, dayLabel = dLabel, minutes = mins))
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        list
    }
    val maxMinutesInWeek = (last7Days.maxOfOrNull { it.minutes } ?: 180).coerceAtLeast(60)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("analytics_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Study Streak Hero Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f)
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .background(AmberAccent, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "🔥 ${streak.first} ${Strings.get("days_streak", language)}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                                Text(
                                    text = if (isHindi) "सर्वश्रेष्ठ स्ट्रीक: ${streak.second} दिन" else "Longest Streak: ${streak.second} Days",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Button(
                            onClick = { showWeeklyReportDialog = true },
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(Strings.get("weekly_report_title", language), style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isHindi) "शाबाश! अपनी स्ट्रीक बनाए रखने के लिए आज का अध्ययन लक्ष्य पूरा करें।" else "Keep it up! Complete today's targets to keep your streak alive.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }

        // Summary Matrix Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = Strings.get("study_time", language),
                        value = totalStudyTimeStr,
                        subtitle = if (isHindi) "कुल अध्ययन" else "Total Study",
                        icon = Icons.Default.Schedule,
                        color = MaterialTheme.colorScheme.primary
                    )
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = Strings.get("completion_rate", language),
                        value = "$avgCompletion%",
                        subtitle = "${totalCompleted}/${totalPlanned} ${Strings.get("completed_tasks", language)}",
                        icon = Icons.Default.CheckCircle,
                        color = StatusSuccess
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = Strings.get("most_studied_subject", language),
                        value = mostStudied?.name ?: "N/A",
                        subtitle = if (mostStudied != null) "${mostStudied.totalMinutes / 60}h ${mostStudied.totalMinutes % 60}m" else "-",
                        icon = Icons.Default.TrendingUp,
                        color = CyanSecondary
                    )
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = Strings.get("pending_tasks", language),
                        value = "$totalPending",
                        subtitle = if (isHindi) "शेष कार्य" else "To Complete",
                        icon = Icons.Default.PieChart,
                        color = AmberAccent
                    )
                }
            }
        }

        // Daily Study Hours Chart (Last 7 Days)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isHindi) "दैनिक अध्ययन के घंटे (पिछले 7 दिन)" else "Daily Study Hours (Last 7 Days)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        last7Days.forEach { stat ->
                            val heightFraction = (stat.minutes.toFloat() / maxMinutesInWeek).coerceIn(0.08f, 1f)
                            val barHours = stat.minutes / 60f

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = if (stat.minutes > 0) "${String.format("%.1f", barHours)}h" else "0",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(22.dp)
                                        .fillMaxHeight(heightFraction)
                                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(
                                                    MaterialTheme.colorScheme.primary,
                                                    CyanSecondary
                                                )
                                            )
                                        )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = stat.dayLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // Subject-Wise Tracking Breakdown
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isHindi) "विषय-वार अध्ययन और कार्य विश्लेषण" else "Subject-wise Tracking & Breakdown",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    subjectStats.forEach { subj ->
                        val h = subj.totalMinutes / 60
                        val m = subj.totalMinutes % 60
                        val timeStr = "${h}h ${m}m"

                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(
                                                try {
                                                    Color(android.graphics.Color.parseColor(subj.colorHex))
                                                } catch (e: Exception) {
                                                    MaterialTheme.colorScheme.primary
                                                },
                                                CircleShape
                                            )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = subj.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Text(
                                    text = "$timeStr • ${subj.completedTasks} पूर्ण (${subj.completionPct}%)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            LinearProgressIndicator(
                                progress = { (subj.completionPct / 100f).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(60.dp)) }
    }

    if (showWeeklyReportDialog) {
        WeeklyReportDialog(
            tasks = allTasks,
            sessions = allSessions,
            currentStreak = streak.first,
            language = language,
            onDismiss = { showWeeklyReportDialog = false }
        )
    }
}

private data class DayStat(
    val dateStr: String,
    val dayLabel: String,
    val minutes: Int
)

private data class SubjectStat(
    val name: String,
    val totalMinutes: Int,
    val completedTasks: Int,
    val pendingTasks: Int,
    val completionPct: Int,
    val colorHex: String
)

@Composable
private fun MetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
