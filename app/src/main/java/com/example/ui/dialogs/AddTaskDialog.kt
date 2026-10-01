package com.example.ui.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.SubjectEntity
import com.example.data.TaskEntity
import com.example.model.AppLanguage
import com.example.model.Strings
import com.example.model.TaskPriority
import com.example.model.TaskType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    initialTask: TaskEntity? = null,
    initialDate: String,
    subjects: List<SubjectEntity>,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (
        title: String,
        subject: String,
        description: String,
        priority: String,
        taskType: String,
        startDate: String,
        startTime: String,
        endDate: String,
        endTime: String,
        estimatedMinutes: Int,
        reminderMinutesBefore: Int
    ) -> Unit
) {
    var title by remember { mutableStateOf(initialTask?.title ?: "") }
    var selectedSubject by remember {
        mutableStateOf(
            initialTask?.subject ?: subjects.firstOrNull()?.name ?: "History"
        )
    }
    var description by remember { mutableStateOf(initialTask?.description ?: "") }
    var selectedPriority by remember {
        mutableStateOf(initialTask?.priority ?: "MEDIUM")
    }
    var selectedType by remember {
        mutableStateOf(initialTask?.taskType ?: "STUDY")
    }
    var startDate by remember { mutableStateOf(initialTask?.startDate ?: initialDate) }
    var startTime by remember { mutableStateOf(initialTask?.startTime ?: "08:00") }
    var endDate by remember { mutableStateOf(initialTask?.endDate ?: initialDate) }
    var endTime by remember { mutableStateOf(initialTask?.endTime ?: "09:30") }
    var estimatedMinutes by remember {
        mutableIntStateOf(initialTask?.estimatedMinutes ?: 90)
    }
    var reminderMinutes by remember {
        mutableIntStateOf(initialTask?.reminderMinutesBefore ?: 15)
    }

    var subjectMenuExpanded by remember { mutableStateOf(false) }
    var reminderMenuExpanded by remember { mutableStateOf(false) }

    val priorities = listOf(
        TaskPriority.LOW.name to Strings.get("priority_low", language),
        TaskPriority.MEDIUM.name to Strings.get("priority_medium", language),
        TaskPriority.HIGH.name to Strings.get("priority_high", language),
        TaskPriority.URGENT.name to Strings.get("priority_urgent", language)
    )

    val types = listOf(
        TaskType.STUDY.name to Strings.get("type_study", language),
        TaskType.REVISION.name to Strings.get("type_revision", language),
        TaskType.HOMEWORK.name to Strings.get("type_homework", language),
        TaskType.ASSIGNMENT.name to Strings.get("type_assignment", language),
        TaskType.EXAM_PREPARATION.name to Strings.get("type_exam_prep", language),
        TaskType.READING.name to Strings.get("type_reading", language),
        TaskType.EXERCISE.name to Strings.get("type_exercise", language),
        TaskType.PERSONAL_WORK.name to Strings.get("type_personal", language),
        TaskType.OTHER.name to Strings.get("type_other", language)
    )

    val reminderOptions = listOf(
        -1 to Strings.get("rem_none", language),
        5 to Strings.get("rem_5m", language),
        10 to Strings.get("rem_10m", language),
        15 to Strings.get("rem_15m", language),
        30 to Strings.get("rem_30m", language),
        60 to Strings.get("rem_1h", language)
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (initialTask == null) Strings.get("add_task_title", language)
                        else Strings.get("edit_task_title", language),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("dialog_close_button")) {
                        Icon(Icons.Default.Close, contentDescription = Strings.get("cancel", language))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Task Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(Strings.get("task_name", language)) },
                    placeholder = { Text(Strings.get("task_name_hint", language)) },
                    leadingIcon = { Icon(Icons.Default.Book, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("task_title_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Subject Dropdown
                ExposedDropdownMenuBox(
                    expanded = subjectMenuExpanded,
                    onExpandedChange = { subjectMenuExpanded = !subjectMenuExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedSubject,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(Strings.get("subject", language)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = subjectMenuExpanded) },
                        leadingIcon = { Icon(Icons.Default.Category, contentDescription = null) },
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
                                    selectedSubject = subj.name
                                    subjectMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Priority Row
                Text(
                    text = Strings.get("priority", language),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    priorities.forEach { (key, label) ->
                        FilterChip(
                            selected = selectedPriority == key,
                            onClick = { selectedPriority = key },
                            label = { Text(label, style = MaterialTheme.typography.bodySmall) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Task Type
                Text(
                    text = Strings.get("task_type", language),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState(), enabled = false),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val visibleTypes = types.take(4)
                    visibleTypes.forEach { (key, label) ->
                        FilterChip(
                            selected = selectedType == key,
                            onClick = { selectedType = key },
                            label = { Text(label, style = MaterialTheme.typography.bodySmall) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Time & Duration
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = { startTime = it },
                        label = { Text(Strings.get("start_time", language)) },
                        leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = endTime,
                        onValueChange = { endTime = it },
                        label = { Text(Strings.get("end_time", language)) },
                        leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        label = { Text(Strings.get("start_date", language)) },
                        leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = estimatedMinutes.toString(),
                        onValueChange = { estimatedMinutes = it.toIntOrNull() ?: 60 },
                        label = { Text(Strings.get("duration", language)) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Reminder Dropdown
                ExposedDropdownMenuBox(
                    expanded = reminderMenuExpanded,
                    onExpandedChange = { reminderMenuExpanded = !reminderMenuExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val currentReminderLabel = reminderOptions.firstOrNull { it.first == reminderMinutes }?.second
                        ?: Strings.get("rem_15m", language)

                    OutlinedTextField(
                        value = currentReminderLabel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(Strings.get("reminder", language)) },
                        leadingIcon = { Icon(Icons.Default.Alarm, contentDescription = null) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = reminderMenuExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = reminderMenuExpanded,
                        onDismissRequest = { reminderMenuExpanded = false }
                    ) {
                        reminderOptions.forEach { (mins, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    reminderMinutes = mins
                                    reminderMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(Strings.get("description", language)) },
                    leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(onClick = onDismiss) {
                        Text(Strings.get("cancel", language))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onSave(
                                    title.trim(),
                                    selectedSubject,
                                    description.trim(),
                                    selectedPriority,
                                    selectedType,
                                    startDate,
                                    startTime,
                                    endDate,
                                    endTime,
                                    estimatedMinutes,
                                    reminderMinutes
                                )
                                onDismiss()
                            }
                        },
                        enabled = title.isNotBlank(),
                        modifier = Modifier.testTag("save_task_button")
                    ) {
                        Text(Strings.get("save_task", language))
                    }
                }
            }
        }
    }
}
