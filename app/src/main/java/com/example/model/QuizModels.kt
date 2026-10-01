package com.example.model

data class QuizQuestion(
    val id: String,
    val topic: String,
    val subTopic: String,
    val questionHindi: String,
    val questionEnglish: String,
    val optionsHindi: List<String>,
    val optionsEnglish: List<String>,
    val correctAnswerIndex: Int,
    val explanationHindi: String,
    val explanationEnglish: String
)

data class CurrentAffairsArticle(
    val id: String,
    val titleHindi: String,
    val titleEnglish: String,
    val summaryHindi: String,
    val summaryEnglish: String,
    val category: String, // National, International, Economy, Sci-Tech, Sports, Bihar Special
    val date: String,
    val readTimeMinutes: Int = 2,
    val relatedQuestions: List<QuizQuestion> = emptyList()
)

data class LucentTopic(
    val id: String,
    val nameHindi: String,
    val nameEnglish: String,
    val iconName: String,
    val colorHex: String,
    val subTopics: List<LucentSubTopic>
)

data class LucentSubTopic(
    val id: String,
    val titleHindi: String,
    val titleEnglish: String,
    val questionCount: Int,
    val questions: List<QuizQuestion>
)

data class QuizSessionState(
    val title: String,
    val questions: List<QuizQuestion>,
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val userAnswers: Map<Int, Int> = emptyMap(), // questionIndex -> selectedOption
    val isFinished: Boolean = false,
    val score: Int = 0,
    val elapsedSeconds: Int = 0
)
