package com.example.model

enum class TaskPriority(val key: String, val level: Int) {
    LOW("LOW", 1),
    MEDIUM("MEDIUM", 2),
    HIGH("HIGH", 3),
    URGENT("URGENT", 4);

    companion object {
        fun fromString(value: String): TaskPriority {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: MEDIUM
        }
    }
}

enum class TaskType(val key: String) {
    STUDY("STUDY"),
    REVISION("REVISION"),
    HOMEWORK("HOMEWORK"),
    ASSIGNMENT("ASSIGNMENT"),
    EXAM_PREPARATION("EXAM_PREPARATION"),
    READING("READING"),
    EXERCISE("EXERCISE"),
    PERSONAL_WORK("PERSONAL_WORK"),
    OTHER("OTHER");

    companion object {
        fun fromString(value: String): TaskType {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: STUDY
        }
    }
}

enum class TaskStatus(val key: String) {
    PLANNED("PLANNED"),
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETED("COMPLETED"),
    PENDING("PENDING"),
    OVERDUE("OVERDUE"),
    CANCELLED("CANCELLED");

    companion object {
        fun fromString(value: String): TaskStatus {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: PLANNED
        }
    }
}
