package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.TaskEntity
import com.example.model.AppLanguage
import com.example.model.Strings
import com.example.ui.dialogs.AddTaskDialog
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
fun TimelineScreen(
    viewModel: PlannerViewModel
) {
    val language by viewModel.currentLanguage.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val tasks by viewModel.selectedDateTasks.collectAsState()
    val subjects by viewModel.allSubjects.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var taskToReschedule by remember { mutableStateOf<TaskEntity?>(null) }

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val sdfDisplay = SimpleDateFormat("EEEE, d MMMM yyyy", if (language == AppLanguage.HINDI) Locale("hi", "IN") else Locale.ENGLISH)

    val currentDateObj = try {
        sdf.parse(selectedDate) ?: Date()
    } catch (e: Exception) {
        Date()
    }
    val dateDisplayString = sdfDisplay.format(currentDateObj)

    val isToday = selectedDate == viewModel.todayDateString

    // Sorted chronologically by start time
    val sortedTasks = remember(tasks) {
        tasks.sortedBy { it.startTime }
    }

    val nowTimeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("timeline_screen")
    ) {
        // Date Selector Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        val cal = Calendar.getInstance().apply {
                            time = currentDateObj
                            add(Calendar.DAY_OF_YEAR, -1)
                        }
                        viewModel.selectDate(sdf.format(cal.time))
                    }
                ) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Previous Day")
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        viewModel.selectDate(viewModel.todayDateString)
                    }
                ) {
                    Text(
                        text = if (isToday) (if (language == AppLanguage.HINDI) "आज (Today)" else "Today") else dateDisplayString,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (isToday) {
                        Text(
                            text = dateDisplayString,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = {
                        val cal = Calendar.getInstance().apply {
                            time = currentDateObj
                            add(Calendar.DAY_OF_YEAR, 1)
                        }
                        viewModel.selectDate(sdf.format(cal.time))
                    }
                ) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Next Day")
                }
            }
        }

        // Timeline Content
        if (sortedTasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (language == AppLanguage.HINDI) "इस दिन के लिए कोई समय-सारणी नहीं है" else "No schedule planned for this day",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(Strings.get("action_add_task", language))
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(sortedTasks, key = { it.id }) { task ->
                    // Determine visual status: Completed, Running, Upcoming, Pending, Overdue
                    val (statusLabel, statusColor, statusIcon) = when {
                        task.isCompleted -> Triple(
                            if (language == AppLanguage.HINDI) "पूर्ण" else "Completed",
                            StatusSuccess,
                            Icons.Default.Check
                        )
                        isToday && task.startTime <= nowTimeStr && nowTimeStr <= task.endTime -> Triple(
                            if (language == AppLanguage.HINDI) "प्रगति में" else "Running",
                            CyanSecondary,
                            Icons.Default.PlayArrow
                        )
                        isToday && nowTimeStr > task.endTime -> Triple(
                            if (language == AppLanguage.HINDI) "छूट गया (Overdue)" else "Overdue",
                            StatusError,
                            Icons.Default.Warning
                        )
                        selectedDate < viewModel.todayDateString -> Triple(
                            if (language == AppLanguage.HINDI) "छूट गया (Overdue)" else "Overdue",
                            StatusError,
                            Icons.Default.Warning
                        )
                        isToday && nowTimeStr < task.startTime -> Triple(
                            if (language == AppLanguage.HINDI) "आगामी" else "Upcoming",
                            MaterialTheme.colorScheme.primary,
                            Icons.Default.Schedule
                        )
                        else -> Triple(
                            if (language == AppLanguage.HINDI) "लंबित" else "Pending",
                            AmberAccent,
                            Icons.Default.HourglassTop
                        )
                    }

                    TimelineNodeItem(
                        task = task,
                        statusLabel = statusLabel,
                        statusColor = statusColor,
                        statusIcon = statusIcon,
                        language = language,
                        onToggleComplete = { viewModel.toggleTaskCompletion(task) },
                        onReschedule = { taskToReschedule = task }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddTaskDialog(
            initialDate = selectedDate,
            subjects = subjects,
            language = language,
            onDismiss = { showAddDialog = false },
            onSave = { title, subj, desc, prio, type, sDate, sTime, eDate, eTime, estMins, remMins ->
                viewModel.addTask(title, subj, desc, prio, type, sDate, sTime, eDate, eTime, estMins, remMins)
            }
        )
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
private fun TimelineNodeItem(
    task: TaskEntity,
    statusLabel: String,
    statusColor: Color,
    statusIcon: ImageVector,
    language: AppLanguage,
    onToggleComplete: () -> Unit,
    onReschedule: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        // Left Column: Time & Timeline vertical line
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(68.dp)
        ) {
            Text(
                text = task.startTime,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = task.endTime,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Node Circle
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(statusColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    statusIcon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }

            // Connecting vertical line
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Right Column: Task Card
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (task.isCompleted)
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                else
                    MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(if (task.isCompleted) 0.dp else 2.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.Bold
                    )

                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = { onToggleComplete() },
                        colors = CheckboxDefaults.colors(checkedColor = StatusSuccess)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = task.subject,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    // Status Pill
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = statusColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = statusLabel,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = statusColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                if (task.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (!task.isCompleted && statusColor == StatusError) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "पुनर्निर्धारित करें →",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onReschedule() }
                    )
                }
            }
        }
    }
}
