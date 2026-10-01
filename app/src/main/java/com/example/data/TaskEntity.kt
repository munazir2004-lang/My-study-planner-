package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val subject: String,
    val description: String = "",
    val priority: String = "MEDIUM", // LOW, MEDIUM, HIGH, URGENT
    val taskType: String = "STUDY",   // STUDY, REVISION, HOMEWORK, etc.
    val startDate: String,           // YYYY-MM-DD
    val startTime: String = "08:00", // HH:mm
    val endDate: String,             // YYYY-MM-DD
    val endTime: String = "09:00",   // HH:mm
    val estimatedMinutes: Int = 60,
    val reminderMinutesBefore: Int = -1, // -1: none, 5, 10, 15, 30, 60, etc.
    val status: String = "PLANNED", // PLANNED, IN_PROGRESS, COMPLETED, PENDING, OVERDUE, CANCELLED
    val isCompleted: Boolean = false,
    val completionDate: String? = null,
    val completionTime: String? = null,
    val actualMinutesSpent: Int = 0,
    val originalDate: String? = null, // Set if rescheduled
    val rescheduleCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
