package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_sessions")
data class StudySessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val subject: String,
    val durationMinutes: Int,
    val sessionType: String = "CUSTOM", // POMODORO_25, SESSION_50, DEEP_STUDY_90, CUSTOM
    val notes: String = "",
    val date: String, // YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis()
)
