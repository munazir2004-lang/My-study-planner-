package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LucentQuizRepository
import com.example.data.QuizResultEntity
import com.example.model.AppLanguage
import com.example.model.CurrentAffairsArticle
import com.example.model.LucentTopic
import com.example.model.QuizQuestion
import com.example.model.QuizSessionState
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanSecondary
import com.example.ui.theme.PriorityHigh
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusSuccess
import com.example.ui.viewmodel.PlannerViewModel

import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyQuizScreen(
    viewModel: PlannerViewModel
) {
    val language by viewModel.currentLanguage.collectAsState()
    val activeSession by viewModel.activeQuizSession.collectAsState()
    val quizResults by viewModel.allQuizResults.collectAsState()

    val isHindi = language == AppLanguage.HINDI

    if (activeSession != null) {
        // Active Quiz Player
        QuizPlayerView(
            session = activeSession!!,
            language = language,
            onSelectOption = { viewModel.selectQuizOption(it) },
            onSubmitAnswer = { viewModel.submitQuizAnswer() },
            onNextQuestion = { viewModel.nextQuizQuestion() },
            onExit = { viewModel.exitQuiz() },
            onRetry = {
                val qList = activeSession!!.questions
                val title = activeSession!!.title
                viewModel.startQuizSession(title, qList)
            }
        )
    } else {
        // Main Hub: 3 Tabs (Current Affairs, Lucent Topic Quiz, Scores)
        var selectedTab by remember { mutableIntStateOf(0) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .testTag("study_quiz_screen")
        ) {
            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (isHindi) "📰 करेंट अफेयर्स" else "📰 Current Affairs",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = if (isHindi) "📚 ल्यूसेंट GK क्विज़" else "📚 Lucent GK",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Text(
                            text = if (isHindi) "📊 स्कोर व इतिहास" else "📊 My Scores",
                            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1
                        )
                    }
                )
            }

            when (selectedTab) {
                0 -> CurrentAffairsTab(
                    language = language,
                    onStartQuiz = { title, questions ->
                        viewModel.startQuizSession(title, questions)
                    }
                )
                1 -> LucentTopicsTab(
                    language = language,
                    onStartQuiz = { title, questions ->
                        viewModel.startQuizSession(title, questions)
                    }
                )
                2 -> QuizHistoryTab(
                    results = quizResults,
                    language = language
                )
            }
        }
    }
}

// ==========================================
// Tab 1: Current Affairs & Related Quizzes
// ==========================================
@Composable
private fun CurrentAffairsTab(
    language: AppLanguage,
    onStartQuiz: (String, List<QuizQuestion>) -> Unit
) {
    val isHindi = language == AppLanguage.HINDI
    val articles = LucentQuizRepository.currentAffairsArticles

    var selectedCategory by remember { mutableStateOf("ALL") }
    val categories = listOf("ALL", "Bihar Special", "Sci-Tech", "Economy", "Sports")

    val filteredArticles = remember(selectedCategory) {
        if (selectedCategory == "ALL") articles
        else articles.filter { it.category == selectedCategory }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Newspaper, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isHindi) "दैनिक समसामयिकी एवं संबंधित क्विज़" else "Daily Current Affairs & Quizzes",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isHindi) "समाचार पढ़ें और तुरंत 1-क्लिक में क्विज़ देकर रिवीजन करें" else "Read verified updates & revise with attached quiz MCQs",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = {
                            Text(
                                if (cat == "ALL") (if (isHindi) "सभी" else "All") else cat
                            )
                        }
                    )
                }
            }
        }

        // Articles List
        items(filteredArticles, key = { it.id }) { article ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = article.category,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Text(
                            text = article.date,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (isHindi) article.titleHindi else article.titleEnglish,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (isHindi) article.summaryHindi else article.summaryEnglish,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )

                    if (article.relatedQuestions.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                val quizTitle = if (isHindi) "करेंट अफेयर्स: ${article.titleHindi}" else "Current Affairs: ${article.titleEnglish}"
                                onStartQuiz(quizTitle, article.relatedQuestions)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isHindi) "इस समाचार पर क्विज़ दें (${article.relatedQuestions.size} प्रश्न) ➔"
                                else "Take Related Quiz (${article.relatedQuestions.size} Questions) ➔"
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(60.dp)) }
    }
}

// ==========================================
// Tab 2: Lucent Topic-Wise GK Quizzes
// ==========================================
@Composable
private fun LucentTopicsTab(
    language: AppLanguage,
    onStartQuiz: (String, List<QuizQuestion>) -> Unit
) {
    val isHindi = language == AppLanguage.HINDI
    val topics = LucentQuizRepository.lucentTopics

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(AmberAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.MenuBook, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isHindi) "ल्यूसेंट सामान्य ज्ञान (Lucent GK) क्विज़" else "Lucent GK Topic-Wise Quiz",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = if (isHindi) "इतिहास, संविधान, भूगोल, विज्ञान और बिहार विशेष के महत्वपूर्ण प्रश्न"
                            else "High-yield topics: History, Polity, Geography, Science & Bihar Special",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        items(topics, key = { it.id }) { topic ->
            TopicCard(
                topic = topic,
                language = language,
                onStartSubTopicQuiz = { subTopic ->
                    val quizTitle = if (isHindi) "${topic.nameHindi} - ${subTopic.titleHindi}" else "${topic.nameEnglish} - ${subTopic.titleEnglish}"
                    onStartQuiz(quizTitle, subTopic.questions)
                }
            )
        }

        item { Spacer(modifier = Modifier.height(60.dp)) }
    }
}

@Composable
private fun TopicCard(
    topic: LucentTopic,
    language: AppLanguage,
    onStartSubTopicQuiz: (com.example.model.LucentSubTopic) -> Unit
) {
    val isHindi = language == AppLanguage.HINDI
    val icon = when (topic.iconName) {
        "history" -> Icons.Default.History
        "gavel" -> Icons.Default.Gavel
        "public" -> Icons.Default.Public
        "biotech" -> Icons.Default.Biotech
        "school" -> Icons.Default.School
        else -> Icons.Default.MenuBook
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            try {
                                Color(android.graphics.Color.parseColor(topic.colorHex)).copy(alpha = 0.15f)
                            } catch (e: Exception) {
                                MaterialTheme.colorScheme.primaryContainer
                            },
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = try {
                            Color(android.graphics.Color.parseColor(topic.colorHex))
                        } catch (e: Exception) {
                            MaterialTheme.colorScheme.primary
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = if (isHindi) topic.nameHindi else topic.nameEnglish,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Subtopics
            topic.subTopics.forEach { sub ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isHindi) sub.titleHindi else sub.titleEnglish,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${sub.questions.size} ${if (isHindi) "महत्वपूर्ण प्रश्न" else "MCQs"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = { onStartSubTopicQuiz(sub) },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isHindi) "क्विज़ शुरू करें" else "Start")
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// Tab 3: Quiz History & Past Scores
// ==========================================
@Composable
private fun QuizHistoryTab(
    results: List<QuizResultEntity>,
    language: AppLanguage
) {
    val isHindi = language == AppLanguage.HINDI

    val totalAttempted = results.size
    val totalScore = results.sumOf { it.score }
    val totalQuestions = results.sumOf { it.totalQuestions }
    val avgAccuracy = if (totalQuestions > 0) ((totalScore.toFloat() / totalQuestions) * 100).toInt() else 0

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Stats Overview Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isHindi) "आपकी क्विज़ प्रगति" else "Your Quiz Performance",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(if (isHindi) "कुल क्विज़" else "Quizzes Taken", style = MaterialTheme.typography.labelSmall)
                            Text("$totalAttempted", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                        }
                        Column {
                            Text(if (isHindi) "औसत सटीकता" else "Avg Accuracy", style = MaterialTheme.typography.labelSmall)
                            Text("$avgAccuracy%", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, color = StatusSuccess)
                        }
                        Column {
                            Text(if (isHindi) "कुल अंक" else "Total Points", style = MaterialTheme.typography.labelSmall)
                            Text("$totalScore / $totalQuestions", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = if (isHindi) "हाल के परिणाम (${results.size})" else "Recent Attempts (${results.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (results.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isHindi) "अभी तक कोई क्विज़ नहीं दिया गया है। ल्यूसेंट या करेंट अफेयर्स क्विज़ हल करें!"
                        else "No quiz attempts yet. Solve Lucent or Current Affairs quizzes to see your records!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(results, key = { it.id }) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.topicName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("📅 ${item.date}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (item.accuracyPercentage >= 70) StatusSuccess.copy(alpha = 0.15f) else AmberAccent.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "${item.score}/${item.totalQuestions} (${item.accuracyPercentage}%)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (item.accuracyPercentage >= 70) StatusSuccess else AmberAccent,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(60.dp)) }
    }
}

// ==========================================
// Interactive Quiz Player
// ==========================================
@Composable
private fun QuizPlayerView(
    session: QuizSessionState,
    language: AppLanguage,
    onSelectOption: (Int) -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextQuestion: () -> Unit,
    onExit: () -> Unit,
    onRetry: () -> Unit
) {
    val isHindi = language == AppLanguage.HINDI
    BackHandler { onExit() }

    if (session.isFinished) {
        // Result Screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (isHindi) "क्विज़ पूर्ण! 🎉" else "Quiz Completed! 🎉",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = session.title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val pct = if (session.questions.isNotEmpty()) ((session.score.toFloat() / session.questions.size) * 100).toInt() else 0
                    Text(
                        text = "${session.score} / ${session.questions.size}",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "$pct% ${if (isHindi) "सटीकता (Accuracy)" else "Accuracy"}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (pct >= 70) StatusSuccess else AmberAccent
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = StatusSuccess.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = if (isHindi) "✓ +15 मिनट अध्ययन समय आपके दैनिक लक्ष्य में जोड़ा गया!"
                            else "✓ +15 minutes focus study time credited to your daily goal!",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = StatusSuccess,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onRetry,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isHindi) "पुनः प्रयास" else "Retry")
                        }

                        Button(
                            onClick = onExit,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(if (isHindi) "वापस जाएं" else "Done")
                        }
                    }
                }
            }
        }
    } else {
        // Active Question View
        val currentQ = session.questions[session.currentIndex]
        val totalQ = session.questions.size

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onExit) {
                    Icon(Icons.Default.Close, contentDescription = "Exit Quiz")
                }

                Text(
                    text = "${if (isHindi) "प्रश्न" else "Question"} ${session.currentIndex + 1} / $totalQ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = "Score: ${session.score}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { (session.currentIndex + 1).toFloat() / totalQ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Question Box
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "${currentQ.topic} • ${currentQ.subTopic}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (isHindi) currentQ.questionHindi else currentQ.questionEnglish,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 24.sp
                            )
                        }
                    }
                }

                // Options
                val options = if (isHindi) currentQ.optionsHindi else currentQ.optionsEnglish
                items(options.indices.toList(), key = { it }) { optIndex ->
                    val optText = options[optIndex]
                    val isSelected = session.selectedOptionIndex == optIndex
                    val isCorrect = optIndex == currentQ.correctAnswerIndex

                    val borderColor = when {
                        session.isAnswerSubmitted && isCorrect -> StatusSuccess
                        session.isAnswerSubmitted && isSelected && !isCorrect -> StatusError
                        isSelected -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.outlineVariant
                    }

                    val containerColor = when {
                        session.isAnswerSubmitted && isCorrect -> StatusSuccess.copy(alpha = 0.15f)
                        session.isAnswerSubmitted && isSelected && !isCorrect -> StatusError.copy(alpha = 0.15f)
                        isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                        else -> MaterialTheme.colorScheme.surface
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
                            .clickable(enabled = !session.isAnswerSubmitted) {
                                onSelectOption(optIndex)
                            },
                        shape = RoundedCornerShape(16.dp),
                        color = containerColor,
                        tonalElevation = 1.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val optionLabel = when (optIndex) {
                                0 -> "A"
                                1 -> "B"
                                2 -> "C"
                                else -> "D"
                            }

                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(
                                        if (isSelected || (session.isAnswerSubmitted && isCorrect))
                                            borderColor
                                        else
                                            MaterialTheme.colorScheme.surfaceVariant,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = optionLabel,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected || (session.isAnswerSubmitted && isCorrect)) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = optText,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.weight(1f)
                            )

                            if (session.isAnswerSubmitted) {
                                if (isCorrect) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = StatusSuccess)
                                } else if (isSelected) {
                                    Icon(Icons.Default.Close, contentDescription = "Wrong", tint = StatusError)
                                }
                            }
                        }
                    }
                }

                // Explanation
                if (session.isAnswerSubmitted) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (isHindi) "व्याख्या (Explanation):" else "Explanation:",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isHindi) currentQ.explanationHindi else currentQ.explanationEnglish,
                                    style = MaterialTheme.typography.bodyMedium,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Action Button
            if (!session.isAnswerSubmitted) {
                Button(
                    onClick = onSubmitAnswer,
                    enabled = session.selectedOptionIndex != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(if (isHindi) "उत्तर जांचें (Check Answer)" else "Check Answer")
                }
            } else {
                Button(
                    onClick = onNextQuestion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        if (session.currentIndex + 1 < totalQ)
                            (if (isHindi) "अगला प्रश्न ➔" else "Next Question ➔")
                        else
                            (if (isHindi) "समाप्त करें और परिणाम देखें ➔" else "Finish & View Results ➔")
                    )
                }
            }
        }
    }
}
