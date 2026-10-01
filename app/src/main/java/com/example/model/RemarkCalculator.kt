package com.example.model

data class PerformanceMetrics(
    val plannedTasks: Int,
    val completedTasks: Int,
    val pendingTasks: Int,
    val overdueTasks: Int,
    val highPriorityCompleted: Int,
    val totalStudyMinutes: Int,
    val targetStudyMinutes: Int,
    val currentStreak: Int
) {
    val completionPercentage: Int
        get() = if (plannedTasks > 0) ((completedTasks.toFloat() / plannedTasks) * 100).toInt() else 0
}

object RemarkCalculator {

    fun generateRemark(
        metrics: PerformanceMetrics,
        language: AppLanguage,
        customRemark100: String = "",
        customRemark90: String = "",
        customRemark75: String = "",
        customRemark60: String = "",
        customRemark40: String = "",
        customRemark20: String = "",
        customRemark0: String = ""
    ): String {
        val pct = metrics.completionPercentage
        val hours = metrics.totalStudyMinutes / 60
        val mins = metrics.totalStudyMinutes % 60
        val timeStr = if (hours > 0) "${hours}h ${mins}m" else "${mins}m"

        val isHindi = language == AppLanguage.HINDI

        // Check custom overrides first
        when {
            pct == 100 && customRemark100.isNotBlank() -> return customRemark100
            pct in 90..99 && customRemark90.isNotBlank() -> return customRemark90
            pct in 75..89 && customRemark75.isNotBlank() -> return customRemark75
            pct in 60..74 && customRemark60.isNotBlank() -> return customRemark60
            pct in 40..59 && customRemark40.isNotBlank() -> return customRemark40
            pct in 20..39 && customRemark20.isNotBlank() -> return customRemark20
            pct == 0 && customRemark0.isNotBlank() -> return customRemark0
        }

        // Smart dynamic calculation
        if (metrics.plannedTasks == 0 && metrics.totalStudyMinutes == 0) {
            return if (isHindi) {
                "आज की शुरुआत करें! अपने लक्ष्यों को पूरा करने के लिए नए कार्य जोड़ें।"
            } else {
                "Ready to start! Add tasks and set your goals for today."
            }
        }

        if (metrics.plannedTasks == 0 && metrics.totalStudyMinutes > 0) {
            return if (isHindi) {
                "शानदार! आपने बिना नियोजित कार्यों के भी $timeStr अध्ययन पूरा किया।"
            } else {
                "Great initiative! You completed $timeStr of study session."
            }
        }

        val streakText = if (metrics.currentStreak > 1) {
            if (isHindi) " (${metrics.currentStreak} दिनों की स्ट्रीक जारी! 🔥)"
            else " (${metrics.currentStreak}-day streak alive! 🔥)"
        } else ""

        return when {
            pct >= 100 -> {
                if (isHindi) {
                    "उत्कृष्ट! आपने आज अपने सभी ${metrics.plannedTasks} नियोजित कार्य पूरे किए और $timeStr अध्ययन समय बनाए रखा। अद्भुत अनुशासन!$streakText"
                } else {
                    "Excellent! You completed all ${metrics.plannedTasks} planned tasks today with $timeStr study time. Outstanding discipline!$streakText"
                }
            }
            pct in 90..99 -> {
                if (isHindi) {
                    "शानदार! योजना का लगभग सब कुछ पूरा हो गया (${metrics.completedTasks}/${metrics.plannedTasks})। आप लक्ष्य के बिल्कुल करीब हैं!$streakText"
                } else {
                    "Excellent! Almost everything planned was completed (${metrics.completedTasks}/${metrics.plannedTasks}) with $timeStr of focus!$streakText"
                }
            }
            pct in 75..89 -> {
                if (isHindi) {
                    "बहुत अच्छा! आप मजबूत प्रगति कर रहे हैं। ${metrics.completedTasks} कार्य पूरे हुए और $timeStr पढ़ाई हुई। ऐसे ही आगे बढ़ते रहें!"
                } else {
                    "Very Good! Strong progress with ${metrics.completedTasks}/${metrics.plannedTasks} tasks and $timeStr study time. Keep going!"
                }
            }
            pct in 60..74 -> {
                if (metrics.overdueTasks > 0) {
                    if (isHindi) {
                        "अच्छा कार्य! ${metrics.completedTasks} कार्य पूरे हुए, लेकिन ${metrics.overdueTasks} कार्य छूट गए हैं। कल प्राथमिकता देकर पूरा करें।"
                    } else {
                        "Good work! You completed ${metrics.completedTasks} of ${metrics.plannedTasks} tasks. Try to finish the ${metrics.overdueTasks} remaining priority tasks tomorrow."
                    }
                } else {
                    if (isHindi) {
                        "अच्छा! आपने अपनी योजना का अधिकांश हिस्सा पूरा कर लिया ($pct%)। कल और बेहतर करने का प्रयास करें।"
                    } else {
                        "Good! You completed most of your plan ($pct%). Try to push even higher tomorrow."
                    }
                }
            }
            pct in 40..59 -> {
                if (isHindi) {
                    "सुधार जारी रखें! आपने प्रगति की (${metrics.completedTasks}/${metrics.plannedTasks}), लेकिन योजना पर अधिक ध्यान देने की आवश्यकता है।"
                } else {
                    "Keep Improving! You made progress with ${metrics.completedTasks} tasks, but there is good room for consistency tomorrow."
                }
            }
            pct in 20..39 -> {
                if (isHindi) {
                    "हिम्मत न हारें! कल नए सिरे से शुरुआत करें और समय पर कार्यों को पूरा करें।"
                } else {
                    "Don't Give Up! Tomorrow is a fresh opportunity to start early and conquer more tasks."
                }
            }
            pct in 1..19 -> {
                if (isHindi) {
                    "शुरुआत हो चुकी है! छोटी प्रगति भी मायने रखती है। कल अधिक फ़ोकस के साथ आगे बढ़ें।"
                } else {
                    "Let's Get Started! Even small progress counts. Aim for a stronger session tomorrow."
                }
            }
            else -> {
                if (isHindi) {
                    "आज कोई कार्य पूरा नहीं हुआ। कल एक नया अवसर है, दृढ़ संकल्प के साथ शुरुआत करें!"
                } else {
                    "No tasks completed today. Tomorrow is a fresh opportunity to rise and shine!"
                }
            }
        }
    }
}
