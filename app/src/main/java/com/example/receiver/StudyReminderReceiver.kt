package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class StudyReminderReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_TASK_ID = "extra_task_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_SUBJECT = "extra_subject"
        const val EXTRA_MINUTES_BEFORE = "extra_minutes_before"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra(EXTRA_TASK_ID, 0L)
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "Study Session"
        val subject = intent.getStringExtra(EXTRA_SUBJECT) ?: "Upcoming Task"
        val minutesBefore = intent.getIntExtra(EXTRA_MINUTES_BEFORE, 0)

        val message = if (minutesBefore > 0) {
            "Reminder: $title ($subject) starts in $minutesBefore minutes."
        } else {
            "Your $title ($subject) study session starts now!"
        }

        NotificationHelper.showNotification(
            context = context,
            notificationId = taskId.toInt().coerceAtLeast(1001),
            title = "📚 $title",
            message = message
        )
    }
}
