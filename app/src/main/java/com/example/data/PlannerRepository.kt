package com.example.data

import kotlinx.coroutines.flow.Flow

class PlannerRepository(private val dao: PlannerDao) {

    // Tasks
    val allTasks: Flow<List<TaskEntity>> = dao.getAllTasks()

    fun getTasksByDate(date: String): Flow<List<TaskEntity>> = dao.getTasksByDate(date)

    fun getTasksBetweenDates(startDate: String, endDate: String): Flow<List<TaskEntity>> =
        dao.getTasksBetweenDates(startDate, endDate)

    suspend fun getTaskById(id: Long): TaskEntity? = dao.getTaskById(id)

    suspend fun insertTask(task: TaskEntity): Long = dao.insertTask(task)

    suspend fun updateTask(task: TaskEntity) = dao.updateTask(task)

    suspend fun deleteTask(task: TaskEntity) = dao.deleteTask(task)

    suspend fun deleteTaskById(id: Long) = dao.deleteTaskById(id)

    // Study Sessions
    val allStudySessions: Flow<List<StudySessionEntity>> = dao.getAllStudySessions()

    fun getStudySessionsByDate(date: String): Flow<List<StudySessionEntity>> =
        dao.getStudySessionsByDate(date)

    fun getStudySessionsBetweenDates(startDate: String, endDate: String): Flow<List<StudySessionEntity>> =
        dao.getStudySessionsBetweenDates(startDate, endDate)

    suspend fun insertStudySession(session: StudySessionEntity): Long =
        dao.insertStudySession(session)

    suspend fun deleteStudySession(session: StudySessionEntity) =
        dao.deleteStudySession(session)

    // Subjects
    val allSubjects: Flow<List<SubjectEntity>> = dao.getAllSubjects()

    suspend fun insertSubject(subject: SubjectEntity): Long = dao.insertSubject(subject)

    suspend fun deleteSubject(subject: SubjectEntity) = dao.deleteSubject(subject)

    // Settings
    val settings: Flow<AppSettingsEntity?> = dao.getSettings()

    suspend fun saveSettings(settings: AppSettingsEntity) = dao.saveSettings(settings)

    // Quiz Results
    val allQuizResults: Flow<List<QuizResultEntity>> = dao.getAllQuizResults()

    suspend fun insertQuizResult(result: QuizResultEntity): Long = dao.insertQuizResult(result)
}
