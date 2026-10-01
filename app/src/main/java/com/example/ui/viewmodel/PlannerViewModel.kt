package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.StudyPlannerApp
import com.example.data.AppSettingsEntity
import com.example.data.QuizResultEntity
import com.example.data.StudySessionEntity
import com.example.data.SubjectEntity
import com.example.data.TaskEntity
import com.example.model.AppLanguage
import com.example.model.PerformanceMetrics
import com.example.model.QuizQuestion
import com.example.model.QuizSessionState
import com.example.model.RemarkCalculator
import com.example.model.TaskPriority
import com.example.model.TaskStatus
import com.example.model.TaskType
import com.example.receiver.NotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class TimerMode {
    POMODORO_25,
    SESSION_50,
    DEEP_STUDY_90,
    CUSTOM
}

data class TimerState(
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val totalSeconds: Int = 25 * 60,
    val remainingSeconds: Int = 25 * 60,
    val selectedSubject: String = "History",
    val mode: TimerMode = TimerMode.POMODORO_25,
    val sessionCount: Int = 0
)

class PlannerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as StudyPlannerApp).repository

    private val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val sdfTime = SimpleDateFormat("HH:mm", Locale.getDefault())

    val todayDateString: String = sdfDate.format(Date())

    // Selected Date for calendar / timeline
    private val _selectedDate = MutableStateFlow(todayDateString)
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    // Search and filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterStatus = MutableStateFlow<String?>("ALL")
    val filterStatus: StateFlow<String?> = _filterStatus.asStateFlow()

    private val _filterSubject = MutableStateFlow<String?>("ALL")
    val filterSubject: StateFlow<String?> = _filterSubject.asStateFlow()

    private val _filterPriority = MutableStateFlow<String?>("ALL")
    val filterPriority: StateFlow<String?> = _filterPriority.asStateFlow()

    // Data streams from repository
    val allTasks: StateFlow<List<TaskEntity>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allStudySessions: StateFlow<List<StudySessionEntity>> = repository.allStudySessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSubjects: StateFlow<List<SubjectEntity>> = repository.allSubjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val settings: StateFlow<AppSettingsEntity> = repository.settings
        .combine(MutableStateFlow(Unit)) { s, _ ->
            s ?: AppSettingsEntity()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettingsEntity())

    // Language state
    val currentLanguage: StateFlow<AppLanguage> = settings.combine(MutableStateFlow(Unit)) { s, _ ->
        if (s.language == "EN") AppLanguage.ENGLISH else AppLanguage.HINDI
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppLanguage.HINDI)

    // Today Tasks
    val todayTasks: StateFlow<List<TaskEntity>> = allTasks.combine(MutableStateFlow(Unit)) { tasks, _ ->
        tasks.filter { it.startDate == todayDateString }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected Date Tasks
    val selectedDateTasks: StateFlow<List<TaskEntity>> = combine(allTasks, _selectedDate) { tasks, date ->
        tasks.filter { it.startDate == date }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered tasks for task list screen
    val filteredTasks: StateFlow<List<TaskEntity>> = combine(
        allTasks,
        _searchQuery,
        _filterStatus,
        _filterSubject,
        _filterPriority
    ) { tasks, query, status, subject, priority ->
        tasks.filter { task ->
            val matchQuery = query.isBlank() ||
                    task.title.contains(query, ignoreCase = true) ||
                    task.subject.contains(query, ignoreCase = true) ||
                    task.description.contains(query, ignoreCase = true)

            val matchStatus = when (status) {
                null, "ALL" -> true
                "COMPLETED" -> task.isCompleted
                "PENDING" -> !task.isCompleted && task.status != "OVERDUE"
                "OVERDUE" -> task.status == "OVERDUE" || (!task.isCompleted && task.startDate < todayDateString)
                else -> task.status.equals(status, ignoreCase = true)
            }

            val matchSubject = subject == null || subject == "ALL" || task.subject.equals(subject, ignoreCase = true)
            val matchPriority = priority == null || priority == "ALL" || task.priority.equals(priority, ignoreCase = true)

            matchQuery && matchStatus && matchSubject && matchPriority
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Today's Study Sessions
    val todayStudySessions: StateFlow<List<StudySessionEntity>> = allStudySessions.combine(MutableStateFlow(Unit)) { sessions, _ ->
        sessions.filter { it.date == todayDateString }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Streak calculation
    val streakStats: StateFlow<Pair<Int, Int>> = combine(allTasks, allStudySessions) { tasks, sessions ->
        calculateStreak(tasks, sessions)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Pair(7, 15)) // defaults

    // Performance Metrics for Today
    val todayMetrics: StateFlow<PerformanceMetrics> = combine(
        todayTasks,
        todayStudySessions,
        settings,
        streakStats
    ) { tasks, sessions, appSettings, streak ->
        val planned = tasks.size
        val completed = tasks.count { it.isCompleted }
        val overdue = tasks.count { !it.isCompleted && (it.status == "OVERDUE" || it.startDate < todayDateString) }
        val pending = planned - completed
        val highPriorityCompleted = tasks.count { it.isCompleted && (it.priority == "HIGH" || it.priority == "URGENT") }
        val totalStudyMinutes = sessions.sumOf { it.durationMinutes } + tasks.filter { it.isCompleted }.sumOf { it.actualMinutesSpent }
        val targetStudyMinutes = (appSettings.dailyStudyTargetHours * 60).toInt()

        PerformanceMetrics(
            plannedTasks = planned,
            completedTasks = completed,
            pendingTasks = pending,
            overdueTasks = overdue,
            highPriorityCompleted = highPriorityCompleted,
            totalStudyMinutes = totalStudyMinutes,
            targetStudyMinutes = targetStudyMinutes,
            currentStreak = streak.first
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PerformanceMetrics(0, 0, 0, 0, 0, 0, 300, 7)
    )

    // Today Remark
    val todayRemark: StateFlow<String> = combine(
        todayMetrics,
        currentLanguage,
        settings
    ) { metrics, lang, set ->
        RemarkCalculator.generateRemark(
            metrics = metrics,
            language = lang,
            customRemark100 = set.customRemark100,
            customRemark90 = set.customRemark90,
            customRemark75 = set.customRemark75,
            customRemark60 = set.customRemark60,
            customRemark40 = set.customRemark40,
            customRemark20 = set.customRemark20,
            customRemark0 = set.customRemark0
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    // Focus Timer State
    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private var timerJob: Job? = null

    fun selectDate(date: String) {
        _selectedDate.value = date
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilterStatus(status: String?) {
        _filterStatus.value = status
    }

    fun setFilterSubject(subject: String?) {
        _filterSubject.value = subject
    }

    fun setFilterPriority(priority: String?) {
        _filterPriority.value = priority
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch {
            val current = settings.value
            repository.saveSettings(current.copy(language = language.code))
        }
    }

    fun updateSettings(updated: AppSettingsEntity) {
        viewModelScope.launch {
            repository.saveSettings(updated)
        }
    }

    // Task Actions
    fun addTask(
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
    ) {
        viewModelScope.launch {
            val task = TaskEntity(
                title = title,
                subject = subject,
                description = description,
                priority = priority,
                taskType = taskType,
                startDate = startDate,
                startTime = startTime,
                endDate = endDate,
                endTime = endTime,
                estimatedMinutes = estimatedMinutes,
                reminderMinutesBefore = reminderMinutesBefore,
                status = "PLANNED",
                isCompleted = false
            )
            val newId = repository.insertTask(task)

            if (reminderMinutesBefore >= 0) {
                NotificationHelper.scheduleTaskReminder(
                    context = getApplication(),
                    taskId = newId,
                    title = title,
                    subject = subject,
                    dateStr = startDate,
                    timeStr = startTime,
                    minutesBefore = reminderMinutesBefore
                )
            }
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task)
            if (task.reminderMinutesBefore >= 0 && !task.isCompleted) {
                NotificationHelper.scheduleTaskReminder(
                    context = getApplication(),
                    taskId = task.id,
                    title = task.title,
                    subject = task.subject,
                    dateStr = task.startDate,
                    timeStr = task.startTime,
                    minutesBefore = task.reminderMinutesBefore
                )
            } else {
                NotificationHelper.cancelTaskReminder(getApplication(), task.id)
            }
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            NotificationHelper.cancelTaskReminder(getApplication(), task.id)
            repository.deleteTask(task)
        }
    }

    fun toggleTaskCompletion(task: TaskEntity) {
        viewModelScope.launch {
            val isNowCompleted = !task.isCompleted
            val nowTime = sdfTime.format(Date())
            val nowDate = sdfDate.format(Date())

            val actualSpent = if (isNowCompleted && task.actualMinutesSpent == 0) {
                task.estimatedMinutes
            } else {
                task.actualMinutesSpent
            }

            val updated = task.copy(
                isCompleted = isNowCompleted,
                status = if (isNowCompleted) "COMPLETED" else "PENDING",
                completionDate = if (isNowCompleted) nowDate else null,
                completionTime = if (isNowCompleted) nowTime else null,
                actualMinutesSpent = if (isNowCompleted) actualSpent else 0
            )

            repository.updateTask(updated)

            if (isNowCompleted) {
                NotificationHelper.cancelTaskReminder(getApplication(), task.id)
            }
        }
    }

    fun updateTaskStatus(task: TaskEntity, newStatus: String) {
        viewModelScope.launch {
            val isCompleted = newStatus == "COMPLETED"
            val nowTime = sdfTime.format(Date())
            val nowDate = sdfDate.format(Date())

            val updated = task.copy(
                status = newStatus,
                isCompleted = isCompleted,
                completionDate = if (isCompleted) nowDate else task.completionDate,
                completionTime = if (isCompleted) nowTime else task.completionTime
            )
            repository.updateTask(updated)
        }
    }

    fun rescheduleTask(task: TaskEntity, newDate: String, newStartTime: String = task.startTime, newEndTime: String = task.endTime) {
        viewModelScope.launch {
            val original = task.originalDate ?: task.startDate
            val updated = task.copy(
                startDate = newDate,
                endDate = newDate,
                startTime = newStartTime,
                endTime = newEndTime,
                originalDate = original,
                rescheduleCount = task.rescheduleCount + 1,
                status = "PLANNED",
                isCompleted = false
            )
            repository.updateTask(updated)

            if (updated.reminderMinutesBefore >= 0) {
                NotificationHelper.scheduleTaskReminder(
                    context = getApplication(),
                    taskId = updated.id,
                    title = updated.title,
                    subject = updated.subject,
                    dateStr = newDate,
                    timeStr = newStartTime,
                    minutesBefore = updated.reminderMinutesBefore
                )
            }
        }
    }

    // Focus Timer Controls
    fun setTimerMode(mode: TimerMode, customDurationMinutes: Int = 25) {
        val totalSecs = when (mode) {
            TimerMode.POMODORO_25 -> 25 * 60
            TimerMode.SESSION_50 -> 50 * 60
            TimerMode.DEEP_STUDY_90 -> 90 * 60
            TimerMode.CUSTOM -> customDurationMinutes * 60
        }
        _timerState.value = _timerState.value.copy(
            mode = mode,
            totalSeconds = totalSecs,
            remainingSeconds = totalSecs,
            isRunning = false,
            isPaused = false
        )
        timerJob?.cancel()
    }

    fun setTimerSubject(subject: String) {
        _timerState.value = _timerState.value.copy(selectedSubject = subject)
    }

    fun startTimer() {
        if (_timerState.value.isRunning) return

        _timerState.value = _timerState.value.copy(isRunning = true, isPaused = false)

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerState.value.remainingSeconds > 0 && _timerState.value.isRunning) {
                delay(1000L)
                val current = _timerState.value
                if (current.isRunning && !current.isPaused) {
                    val newRemaining = current.remainingSeconds - 1
                    _timerState.value = current.copy(remainingSeconds = newRemaining)
                    if (newRemaining <= 0) {
                        onTimerFinished()
                        break
                    }
                }
            }
        }
    }

    fun pauseTimer() {
        _timerState.value = _timerState.value.copy(isPaused = true)
    }

    fun resumeTimer() {
        _timerState.value = _timerState.value.copy(isPaused = false)
    }

    fun resetTimer() {
        timerJob?.cancel()
        val current = _timerState.value
        _timerState.value = current.copy(
            isRunning = false,
            isPaused = false,
            remainingSeconds = current.totalSeconds
        )
    }

    fun stopTimerAndSave() {
        timerJob?.cancel()
        val current = _timerState.value
        val elapsedSec = current.totalSeconds - current.remainingSeconds
        val elapsedMinutes = (elapsedSec / 60).coerceAtLeast(1)

        if (elapsedMinutes > 0) {
            saveStudySession(
                subject = current.selectedSubject,
                durationMinutes = elapsedMinutes,
                sessionType = current.mode.name,
                notes = "Focus session completed"
            )
        }

        _timerState.value = current.copy(
            isRunning = false,
            isPaused = false,
            remainingSeconds = current.totalSeconds
        )
    }

    private fun onTimerFinished() {
        val current = _timerState.value
        val completedMinutes = current.totalSeconds / 60
        saveStudySession(
            subject = current.selectedSubject,
            durationMinutes = completedMinutes,
            sessionType = current.mode.name,
            notes = "Completed full ${current.mode.name.lowercase()} session"
        )

        NotificationHelper.showNotification(
            context = getApplication(),
            notificationId = 9999,
            title = "🎉 Study Session Complete!",
            message = "Great focus! You completed ${completedMinutes}m for ${current.selectedSubject}."
        )

        _timerState.value = current.copy(
            isRunning = false,
            isPaused = false,
            remainingSeconds = current.totalSeconds,
            sessionCount = current.sessionCount + 1
        )
    }

    fun saveStudySession(subject: String, durationMinutes: Int, sessionType: String, notes: String) {
        viewModelScope.launch {
            val session = StudySessionEntity(
                subject = subject,
                durationMinutes = durationMinutes,
                sessionType = sessionType,
                notes = notes,
                date = todayDateString,
                timestamp = System.currentTimeMillis()
            )
            repository.insertStudySession(session)
        }
    }

    fun addSubject(name: String, colorHex: String, weeklyHours: Float = 5f) {
        viewModelScope.launch {
            repository.insertSubject(
                SubjectEntity(
                    name = name,
                    colorHex = colorHex,
                    targetWeeklyHours = weeklyHours
                )
            )
        }
    }

    fun deleteSubject(subject: SubjectEntity) {
        viewModelScope.launch {
            repository.deleteSubject(subject)
        }
    }

    fun sendTestNotification() {
        NotificationHelper.showNotification(
            context = getApplication(),
            notificationId = 1234,
            title = "📚 My Study Planner",
            message = "Your BPSC Preparation session starts in 15 minutes. Best of luck!"
        )
    }

    private fun calculateStreak(tasks: List<TaskEntity>, sessions: List<StudySessionEntity>): Pair<Int, Int> {
        val activeDates = mutableSetOf<String>()
        tasks.filter { it.isCompleted && it.completionDate != null }.forEach {
            it.completionDate?.let { date -> activeDates.add(date) }
        }
        sessions.forEach { activeDates.add(it.date) }

        if (activeDates.isEmpty()) return Pair(1, 1)

        val cal = Calendar.getInstance()
        var currentStreak = 0
        var checkCal = cal.clone() as Calendar

        // Check if today or yesterday is active
        val todayStr = sdfDate.format(checkCal.time)
        val hasToday = activeDates.contains(todayStr)
        if (!hasToday) {
            checkCal.add(Calendar.DAY_OF_YEAR, -1)
            val yesterdayStr = sdfDate.format(checkCal.time)
            if (!activeDates.contains(yesterdayStr)) {
                return Pair(0, 15) // default longest
            }
        }

        while (true) {
            val dStr = sdfDate.format(checkCal.time)
            if (activeDates.contains(dStr)) {
                currentStreak++
                checkCal.add(Calendar.DAY_OF_YEAR, -1)
            } else {
                break
            }
        }

        val longest = maxOf(currentStreak, 15)
        return Pair(currentStreak.coerceAtLeast(1), longest)
    }

    // --- Quiz Management ---
    val allQuizResults: StateFlow<List<QuizResultEntity>> = repository.allQuizResults
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _activeQuizSession = MutableStateFlow<QuizSessionState?>(null)
    val activeQuizSession: StateFlow<QuizSessionState?> = _activeQuizSession.asStateFlow()

    fun startQuizSession(title: String, questions: List<QuizQuestion>) {
        if (questions.isEmpty()) return
        _activeQuizSession.value = QuizSessionState(
            title = title,
            questions = questions,
            currentIndex = 0,
            selectedOptionIndex = null,
            isAnswerSubmitted = false,
            userAnswers = emptyMap(),
            isFinished = false,
            score = 0,
            elapsedSeconds = 0
        )
    }

    fun selectQuizOption(index: Int) {
        val current = _activeQuizSession.value ?: return
        if (current.isAnswerSubmitted) return
        _activeQuizSession.value = current.copy(selectedOptionIndex = index)
    }

    fun submitQuizAnswer() {
        val current = _activeQuizSession.value ?: return
        val selected = current.selectedOptionIndex ?: return
        val currentQuestion = current.questions[current.currentIndex]
        val isCorrect = selected == currentQuestion.correctAnswerIndex
        val newScore = if (isCorrect) current.score + 1 else current.score

        val newAnswers = current.userAnswers.toMutableMap()
        newAnswers[current.currentIndex] = selected

        _activeQuizSession.value = current.copy(
            isAnswerSubmitted = true,
            score = newScore,
            userAnswers = newAnswers
        )
    }

    fun nextQuizQuestion() {
        val current = _activeQuizSession.value ?: return
        if (current.currentIndex + 1 < current.questions.size) {
            _activeQuizSession.value = current.copy(
                currentIndex = current.currentIndex + 1,
                selectedOptionIndex = null,
                isAnswerSubmitted = false
            )
        } else {
            finishQuizSession()
        }
    }

    fun finishQuizSession() {
        val current = _activeQuizSession.value ?: return
        val finalScore = current.score
        val total = current.questions.size

        viewModelScope.launch {
            repository.insertQuizResult(
                QuizResultEntity(
                    quizType = if (current.title.contains("Current Affairs", ignoreCase = true) || current.title.contains("करेंट अफेयर्स", ignoreCase = true)) "CURRENT_AFFAIRS" else "LUCENT_GK",
                    topicName = current.title,
                    score = finalScore,
                    totalQuestions = total,
                    timeTakenSeconds = 60,
                    date = todayDateString,
                    timestamp = System.currentTimeMillis()
                )
            )

            // Adding 15 minutes of study session so that quiz practice credits student's daily study goals
            val relatedSubject = when {
                current.title.contains("History", ignoreCase = true) || current.title.contains("इतिहास", ignoreCase = true) -> "History"
                current.title.contains("Polity", ignoreCase = true) || current.title.contains("राजव्यवस्था", ignoreCase = true) -> "Political Science"
                current.title.contains("Geography", ignoreCase = true) || current.title.contains("भूगोल", ignoreCase = true) -> "Geography"
                current.title.contains("Bihar", ignoreCase = true) || current.title.contains("बिहार", ignoreCase = true) -> "BPSC Preparation"
                else -> "General Studies"
            }

            saveStudySession(
                subject = relatedSubject,
                durationMinutes = 15,
                sessionType = "QUIZ_PRACTICE",
                notes = "Completed Quiz: ${current.title} ($finalScore/$total)"
            )
        }

        _activeQuizSession.value = current.copy(isFinished = true)
    }

    fun exitQuiz() {
        _activeQuizSession.value = null
    }
}
