package com.example

import android.app.Application
import com.example.data.AppDatabase
import com.example.data.AppSettingsEntity
import com.example.data.PlannerRepository
import com.example.data.StudySessionEntity
import com.example.data.SubjectEntity
import com.example.data.TaskEntity
import com.example.receiver.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class StudyPlannerApp : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var repository: PlannerRepository
        private set

    private val applicationScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getInstance(this)
        repository = PlannerRepository(database.plannerDao())

        NotificationHelper.createNotificationChannel(this)

        seedInitialDataIfNecessary()
    }

    private fun seedInitialDataIfNecessary() {
        applicationScope.launch {
            val existingSettings = repository.settings.firstOrNull()
            if (existingSettings == null) {
                repository.saveSettings(
                    AppSettingsEntity(
                        id = 1,
                        userName = "Munazir",
                        dailyStudyTargetHours = 5.0f,
                        dailyTaskTarget = 6,
                        weeklyStudyTargetHours = 30.0f,
                        language = "HI", // Default to Hindi as requested
                        themeMode = "SYSTEM"
                    )
                )
            }

            val existingSubjects = repository.allSubjects.firstOrNull()
            if (existingSubjects.isNullOrEmpty()) {
                val defaultSubjects = listOf(
                    SubjectEntity(name = "History", colorHex = "#EF4444", targetWeeklyHours = 6f),
                    SubjectEntity(name = "Political Science", colorHex = "#3B82F6", targetWeeklyHours = 5f),
                    SubjectEntity(name = "English", colorHex = "#10B981", targetWeeklyHours = 4f),
                    SubjectEntity(name = "BPSC Preparation", colorHex = "#F59E0B", targetWeeklyHours = 10f),
                    SubjectEntity(name = "Geography", colorHex = "#8B5CF6", targetWeeklyHours = 4f),
                    SubjectEntity(name = "Hindi", colorHex = "#EC4899", targetWeeklyHours = 3f),
                    SubjectEntity(name = "STET Preparation", colorHex = "#06B6D4", targetWeeklyHours = 5f)
                )
                database.plannerDao().insertSubjects(defaultSubjects)
            }

            val existingTasks = repository.allTasks.firstOrNull()
            val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            if (existingTasks.isNullOrEmpty()) {
                // Seed sample tasks matching user prompt flow
                val sampleTasks = listOf(
                    TaskEntity(
                        title = "History NCERT",
                        subject = "History",
                        description = "Chapter 4: Mughal Empire revision and notes",
                        priority = "HIGH",
                        taskType = "REVISION",
                        startDate = todayStr,
                        startTime = "07:00",
                        endDate = todayStr,
                        endTime = "08:30",
                        estimatedMinutes = 90,
                        reminderMinutesBefore = 15,
                        status = "COMPLETED",
                        isCompleted = true,
                        completionDate = todayStr,
                        completionTime = "08:30",
                        actualMinutesSpent = 90
                    ),
                    TaskEntity(
                        title = "Political Science",
                        subject = "Political Science",
                        description = "Indian Constitution: Fundamental Rights deep dive",
                        priority = "MEDIUM",
                        taskType = "STUDY",
                        startDate = todayStr,
                        startTime = "09:00",
                        endDate = todayStr,
                        endTime = "10:30",
                        estimatedMinutes = 90,
                        reminderMinutesBefore = 10,
                        status = "COMPLETED",
                        isCompleted = true,
                        completionDate = todayStr,
                        completionTime = "10:25",
                        actualMinutesSpent = 85
                    ),
                    TaskEntity(
                        title = "College Work / Homework",
                        subject = "English",
                        description = "Summary assignment submission",
                        priority = "MEDIUM",
                        taskType = "HOMEWORK",
                        startDate = todayStr,
                        startTime = "11:00",
                        endDate = todayStr,
                        endTime = "12:00",
                        estimatedMinutes = 60,
                        reminderMinutesBefore = -1,
                        status = "COMPLETED",
                        isCompleted = true,
                        completionDate = todayStr,
                        completionTime = "11:55",
                        actualMinutesSpent = 55
                    ),
                    TaskEntity(
                        title = "English Vocabulary & Reading",
                        subject = "English",
                        description = "Editorial analysis & 20 new vocabulary words",
                        priority = "MEDIUM",
                        taskType = "READING",
                        startDate = todayStr,
                        startTime = "16:00",
                        endDate = todayStr,
                        endTime = "17:00",
                        estimatedMinutes = 60,
                        reminderMinutesBefore = 15,
                        status = "PENDING",
                        isCompleted = false
                    ),
                    TaskEntity(
                        title = "BPSC Preparation Practice",
                        subject = "BPSC Preparation",
                        description = "Previous Year Questions practice set 2024",
                        priority = "URGENT",
                        taskType = "EXAM_PREPARATION",
                        startDate = todayStr,
                        startTime = "19:00",
                        endDate = todayStr,
                        endTime = "21:00",
                        estimatedMinutes = 120,
                        reminderMinutesBefore = 15,
                        status = "PENDING",
                        isCompleted = false
                    )
                )
                database.plannerDao().insertTasks(sampleTasks)

                // Also seed initial study sessions for today and yesterday to showcase timer & analytics
                val sampleSessions = listOf(
                    StudySessionEntity(
                        subject = "History",
                        durationMinutes = 80,
                        sessionType = "DEEP_STUDY_90",
                        notes = "Ancient Indian History timeline",
                        date = todayStr,
                        timestamp = System.currentTimeMillis() - 14400000
                    ),
                    StudySessionEntity(
                        subject = "Political Science",
                        durationMinutes = 50,
                        sessionType = "SESSION_50",
                        notes = "Articles 12 to 35 revision",
                        date = todayStr,
                        timestamp = System.currentTimeMillis() - 7200000
                    ),
                    StudySessionEntity(
                        subject = "English",
                        durationMinutes = 40,
                        sessionType = "CUSTOM",
                        notes = "Grammar rules and comprehension",
                        date = todayStr,
                        timestamp = System.currentTimeMillis() - 3600000
                    )
                )
                database.plannerDao().insertStudySessions(sampleSessions)
            }
        }
    }
}
