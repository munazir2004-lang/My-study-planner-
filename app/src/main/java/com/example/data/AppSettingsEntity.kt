package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey
    val id: Int = 1,
    val userName: String = "Munazir",
    val dailyStudyTargetHours: Float = 5.0f,
    val dailyTaskTarget: Int = 6,
    val weeklyStudyTargetHours: Float = 30.0f,
    val language: String = "HI", // "HI" or "EN"
    val themeMode: String = "SYSTEM", // "SYSTEM", "LIGHT", "DARK"
    val notificationsEnabled: Boolean = true,
    val customRemark100: String = "",
    val customRemark90: String = "",
    val customRemark75: String = "",
    val customRemark60: String = "",
    val customRemark40: String = "",
    val customRemark20: String = "",
    val customRemark0: String = ""
)
