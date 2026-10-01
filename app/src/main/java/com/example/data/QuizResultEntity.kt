package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val quizType: String, // "CURRENT_AFFAIRS", "LUCENT_GK"
    val topicName: String,
    val score: Int,
    val totalQuestions: Int,
    val timeTakenSeconds: Int,
    val date: String, // YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis()
) {
    val accuracyPercentage: Int
        get() = if (totalQuestions > 0) ((score.toFloat() / totalQuestions) * 100).toInt() else 0
}
