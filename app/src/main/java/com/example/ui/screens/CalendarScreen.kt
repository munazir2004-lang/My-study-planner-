package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TaskEntity
import com.example.model.AppLanguage
import com.example.model.PerformanceMetrics
import com.example.model.RemarkCalculator
import com.example.model.Strings
import com.example.ui.dialogs.RescheduleDialog
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
fun CalendarScreen(
    viewModel: PlannerViewModel
) {
    val language by viewModel.currentLanguage.collectAsState()
    val allTasks by viewModel.allTasks.collectAsState()
    val allSessions by viewModel.allStudySessions.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedTasks by viewModel.selectedDateTasks.collectAsState()
    val settings by viewModel.settings.collectAsState()

    var taskToReschedule by remember { mutableStateOf<TaskEntity?>(null) }

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val monthYearFormat = SimpleDateFormat("MMMM yyyy", if (language == AppLanguage.HINDI) Locale("hi", "IN") else Locale.ENGLISH)

    // Current viewing month
    var currentMonthCal by remember {
        val cal = Calendar.getInstance()
        try {
            cal.time = sdf.parse(selectedDate) ?: Date()
        } catch (e: Exception) {
            cal.time = Date()
        }
        cal.set(Calendar.DAY_OF_MONTH, 1)
        mutableStateOf(cal)
    }

    val daysInMonth = currentMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = (currentMonthCal.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY + 7) % 7 // Monday = 0

    val weekDays = if (language == AppLanguage.HINDI) {
        listOf("सोम", "मंगल", "बुध", "गुरु", "शुक्र", "शनि", "रवि")
    } else {
        listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    }

    // Selected Date Performance Metrics
    val selPlanned = selectedTasks.size
    val selCompleted = selectedTasks.count { it.isCompleted }
    val selPending = selPlanned - selCompleted
    val selOverdue = selectedTasks.count { !it.isCompleted && (it.status == "OVERDUE" || it.startDate < viewModel.todayDateString) }
    val selSessions = allSessions.filter { it.date == selectedDate }
    val selStudyMinutes = selSessions.sumOf { it.durationMinutes } + selectedTasks.filter { it.isCompleted }.sumOf { it.actualMinutesSpent }
    val selHours = selStudyMinutes / 60
    val selMins = selStudyMinutes % 60
    val selTimeStr = "${selHours}h ${selMins}m"
    val selPct = if (selPlanned > 0) ((selCompleted.toFloat() / selPlanned) * 100).toInt() else 0

    val selMetrics = PerformanceMetrics(
        plannedTasks = selPlanned,
        completedTasks = selCompleted,
        pendingTasks = selPending,
        overdueTasks = selOverdue,
        highPriorityCompleted = selectedTasks.count { it.isCompleted && (it.priority == "HIGH" || it.priority == "URGENT") },
        totalStudyMinutes = selStudyMinutes,
        targetStudyMinutes = (settings.dailyStudyTargetHours * 60).toInt(),
        currentStreak = 1
    )

    val selRemark = RemarkCalculator.generateRemark(
        metrics = selMetrics,
        language = language,
        customRemark100 = settings.customRemark100,
        customRemark90 = settings.customRemark90,
        customRemark75 = settings.customRemark75,
        customRemark60 = settings.customRemark60,
        customRemark40 = settings.customRemark40,
        customRemark20 = settings.customRemark20,
        customRemark0 = settings.customRemark0
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("calendar_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Month Selector Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            val newCal = currentMonthCal.clone() as Calendar
                            newCal.add(Calendar.MONTH, -1)
                            currentMonthCal = newCal
                        }) {
                            Icon(Icons.Default.ChevronLeft, contentDescription = "Prev Month")
                        }

                        Text(
                            text = monthYearFormat.format(currentMonthCal.time),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        IconButton(onClick = {
                            val newCal = currentMonthCal.clone() as Calendar
                            newCal.add(Calendar.MONTH, 1)
                            currentMonthCal = newCal
                        }) {
                            Icon(Icons.Default.ChevronRight, contentDescription = "Next Month")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Days of week row
                    Row(modifier = Modifier.fillMaxWidth()) {
                        weekDays.forEach { d ->
                            Text(
                                text = d,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Calendar Grid (6 rows max)
                    val totalCells = firstDayOfWeek + daysInMonth
                    val rows = (totalCells + 6) / 7

                    for (r in 0 until rows) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            for (c in 0..6) {
                                val cellIndex = r * 7 + c
                                val dayNum = cellIndex - firstDayOfWeek + 1

                                if (dayNum in 1..daysInMonth) {
                                    val cellCal = currentMonthCal.clone() as Calendar
                                    cellCal.set(Calendar.DAY_OF_MONTH, dayNum)
                                    val dateKey = sdf.format(cellCal.time)
                                    val isSelected = dateKey == selectedDate
                                    val isToday = dateKey == viewModel.todayDateString

                                    // Tasks on this date
                                    val dayTasks = allTasks.filter { it.startDate == dateKey }
                                    val dayPlanned = dayTasks.size
                                    val dayCompleted = dayTasks.count { it.isCompleted }
                                    val dayPct = if (dayPlanned > 0) ((dayCompleted.toFloat() / dayPlanned) * 100).toInt() else 0

                                    val productivityDotColor = when {
                                        dayPlanned == 0 -> null
                                        dayPct >= 75 -> StatusSuccess
                                        dayPct >= 40 -> AmberAccent
                                        else -> StatusError
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .padding(2.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(
                                                when {
                                                    isSelected -> MaterialTheme.colorScheme.primaryContainer
                                                    isToday -> MaterialTheme.colorScheme.surfaceVariant
                                                    else -> Color.Transparent
                                                }
                                            )
                                            .clickable { viewModel.selectDate(dateKey) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "$dayNum",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                            )

                                            // Productivity Indicator Dot
                                            productivityDotColor?.let { dotColor ->
                                                Box(
                                                    modifier = Modifier
                                                        .size(5.dp)
                                                        .background(dotColor, CircleShape)
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Productivity Legend
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LegendDot(StatusSuccess, if (language == AppLanguage.HINDI) "उच्च (75%+)" else "High (75%+)")
                        Spacer(modifier = Modifier.width(16.dp))
                        LegendDot(AmberAccent, if (language == AppLanguage.HINDI) "मध्यम (40-74%)" else "Medium")
                        Spacer(modifier = Modifier.width(16.dp))
                        LegendDot(StatusError, if (language == AppLanguage.HINDI) "कम / छूटे" else "Low / Overdue")
                    }
                }
            }
        }

        // Selected Date Summary Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📅 $selectedDate",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(Strings.get("planned_tasks", language), style = MaterialTheme.typography.labelSmall)
                            Text("$selPlanned", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        }
                        Column {
                            Text(Strings.get("completed_tasks", language), style = MaterialTheme.typography.labelSmall)
                            Text("$selCompleted", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = StatusSuccess)
                        }
                        Column {
                            Text(Strings.get("pending_tasks", language), style = MaterialTheme.typography.labelSmall)
                            Text("$selPending", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = AmberAccent)
                        }
                        Column {
                            Text(Strings.get("completion_rate", language), style = MaterialTheme.typography.labelSmall)
                            Text("$selPct%", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                        }
                        Column {
                            Text(Strings.get("study_time", language), style = MaterialTheme.typography.labelSmall)
                            Text(selTimeStr, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Daily Remark
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "\"$selRemark\"",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        // Tasks on Selected Date
        item {
            Text(
                text = "${Strings.get("nav_tasks", language)} ($selPlanned)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (selectedTasks.isEmpty()) {
            item {
                Text(
                    text = if (language == AppLanguage.HINDI) "इस तिथि के लिए कोई कार्य नहीं मिला" else "No tasks for this date",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(selectedTasks, key = { it.id }) { task ->
                TaskCardItem(
                    task = task,
                    language = language,
                    onToggleComplete = { viewModel.toggleTaskCompletion(task) },
                    onReschedule = { taskToReschedule = task },
                    onEdit = {}
                )
            }
        }

        item { Spacer(modifier = Modifier.height(60.dp)) }
    }

    taskToReschedule?.let { task ->
        RescheduleDialog(
            task = task,
            language = language,
            onDismiss = { taskToReschedule = null },
            onReschedule = { newDate, newStartTime, newEndTime ->
                viewModel.rescheduleTask(task, newDate, newStartTime, newEndTime)
                taskToReschedule = null
            }
        )
    }
}

@Composable
private fun LegendDot(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, style = MaterialTheme.typography.labelSmall)
    }
}
