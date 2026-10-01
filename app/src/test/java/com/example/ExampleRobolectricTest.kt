package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.AppLanguage
import com.example.model.PerformanceMetrics
import com.example.model.RemarkCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("My Study Planner", appName)
  }

  @Test
  fun `test 100 percent remark calculation`() {
    val metrics = PerformanceMetrics(
      plannedTasks = 8,
      completedTasks = 8,
      pendingTasks = 0,
      overdueTasks = 0,
      highPriorityCompleted = 3,
      totalStudyMinutes = 260,
      targetStudyMinutes = 300,
      currentStreak = 7
    )
    val remarkEnglish = RemarkCalculator.generateRemark(metrics, AppLanguage.ENGLISH)
    assertTrue(remarkEnglish.contains("Outstanding discipline"))

    val remarkHindi = RemarkCalculator.generateRemark(metrics, AppLanguage.HINDI)
    assertTrue(remarkHindi.contains("उत्कृष्ट"))
  }

  @Test
  fun `test Lucent and Current Affairs content available`() {
    val articles = com.example.data.LucentQuizRepository.currentAffairsArticles
    assertTrue(articles.isNotEmpty())
    assertTrue(articles.any { it.relatedQuestions.isNotEmpty() })

    val topics = com.example.data.LucentQuizRepository.lucentTopics
    assertTrue(topics.size >= 4)
    val historyTopic = topics.find { it.id == "hist" }
    assertTrue(historyTopic != null && historyTopic.subTopics.isNotEmpty())
  }
}
