package com.example.ui.screens

import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.TaskEntity
import com.example.model.AppLanguage
import com.example.model.Strings
import com.example.ui.dialogs.AddTaskDialog
import com.example.ui.dialogs.RescheduleDialog
import com.example.ui.viewmodel.PlannerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    viewModel: PlannerViewModel
) {
    val language by viewModel.currentLanguage.collectAsState()
    val tasks by viewModel.filteredTasks.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filterStatus by viewModel.filterStatus.collectAsState()
    val filterSubject by viewModel.filterSubject.collectAsState()
    val filterPriority by viewModel.filterPriority.collectAsState()
    val subjects by viewModel.allSubjects.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<TaskEntity?>(null) }
    var taskToReschedule by remember { mutableStateOf<TaskEntity?>(null) }
    var taskToDelete by remember { mutableStateOf<TaskEntity?>(null) }

    val statusFilters = listOf(
        "ALL" to Strings.get("filter_all", language),
        "PENDING" to Strings.get("filter_pending", language),
        "COMPLETED" to Strings.get("filter_completed", language),
        "OVERDUE" to Strings.get("filter_overdue", language)
    )

    val priorityFilters = listOf(
        "ALL" to Strings.get("filter_all", language),
        "LOW" to Strings.get("priority_low", language),
        "MEDIUM" to Strings.get("priority_medium", language),
        "HIGH" to Strings.get("priority_high", language),
        "URGENT" to Strings.get("priority_urgent", language)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text(Strings.get("search_hint", language)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_tasks_input"),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Status Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                statusFilters.forEach { (key, label) ->
                    FilterChip(
                        selected = filterStatus == key,
                        onClick = { viewModel.setFilterStatus(key) },
                        label = { Text(label) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Subject Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = filterSubject == "ALL" || filterSubject == null,
                    onClick = { viewModel.setFilterSubject("ALL") },
                    label = { Text(if (language == AppLanguage.HINDI) "सभी विषय" else "All Subjects") }
                )
                subjects.forEach { subj ->
                    FilterChip(
                        selected = filterSubject == subj.name,
                        onClick = { viewModel.setFilterSubject(subj.name) },
                        label = { Text(subj.name) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tasks List
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 72.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "कोई कार्य नहीं मिला" else "No tasks found",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
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
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(tasks, key = { it.id }) { task ->
                        DetailedTaskCard(
                            task = task,
                            language = language,
                            onToggleComplete = { viewModel.toggleTaskCompletion(task) },
                            onEdit = { taskToEdit = task },
                            onReschedule = { taskToReschedule = task },
                            onDelete = { taskToDelete = task }
                        )
                    }
                }
            }
        }

        // Add Task Floating Action Button
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .testTag("add_task_fab"),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, contentDescription = Strings.get("action_add_task", language))
        }
    }

    // Add Task Dialog
    if (showAddDialog) {
        AddTaskDialog(
            initialDate = viewModel.todayDateString,
            subjects = subjects,
            language = language,
            onDismiss = { showAddDialog = false },
            onSave = { title, subj, desc, prio, type, sDate, sTime, eDate, eTime, estMins, remMins ->
                viewModel.addTask(title, subj, desc, prio, type, sDate, sTime, eDate, eTime, estMins, remMins)
            }
        )
    }

    // Edit Task Dialog
    taskToEdit?.let { task ->
        AddTaskDialog(
            initialTask = task,
            initialDate = task.startDate,
            subjects = subjects,
            language = language,
            onDismiss = { taskToEdit = null },
            onSave = { title, subj, desc, prio, type, sDate, sTime, eDate, eTime, estMins, remMins ->
                viewModel.updateTask(
                    task.copy(
                        title = title,
                        subject = subj,
                        description = desc,
                        priority = prio,
                        taskType = type,
                        startDate = sDate,
                        startTime = sTime,
                        endDate = eDate,
                        endTime = eTime,
                        estimatedMinutes = estMins,
                        reminderMinutesBefore = remMins
                    )
                )
                taskToEdit = null
            }
        )
    }

    // Reschedule Dialog
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

    // Delete Confirmation Dialog
    taskToDelete?.let { task ->
        AlertDialog(
            onDismissRequest = { taskToDelete = null },
            title = { Text(Strings.get("delete", language)) },
            text = { Text(if (language == AppLanguage.HINDI) "क्या आप वाकई \"${task.title}\" को हटाना चाहते हैं?" else "Are you sure you want to delete \"${task.title}\"?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteTask(task)
                        taskToDelete = null
                    }
                ) {
                    Text(Strings.get("delete", language))
                }
            },
            dismissButton = {
                TextButton(onClick = { taskToDelete = null }) {
                    Text(Strings.get("cancel", language))
                }
            }
        )
    }
}

@Composable
private fun DetailedTaskCard(
    task: TaskEntity,
    language: AppLanguage,
    onToggleComplete: () -> Unit,
    onEdit: () -> Unit,
    onReschedule: () -> Unit,
    onDelete: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Box {
        TaskCardItem(
            task = task,
            language = language,
            onToggleComplete = onToggleComplete,
            onReschedule = onReschedule,
            onEdit = onEdit,
            onDelete = onDelete
        )

        // Dropdown menu button
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
        ) {
            IconButton(onClick = { menuExpanded = true }, modifier = Modifier.size(32.dp)) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "Options",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(Strings.get("edit_task_title", language)) },
                    onClick = {
                        menuExpanded = false
                        onEdit()
                    },
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                if (!task.isCompleted) {
                    DropdownMenuItem(
                        text = { Text(Strings.get("reschedule", language)) },
                        onClick = {
                            menuExpanded = false
                            onReschedule()
                        },
                        leadingIcon = { Icon(Icons.Default.EditCalendar, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                }
                DropdownMenuItem(
                    text = { Text(Strings.get("delete", language), color = MaterialTheme.colorScheme.error) },
                    onClick = {
                        menuExpanded = false
                        onDelete()
                    },
                    leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp)) }
                )
            }
        }
    }
}
