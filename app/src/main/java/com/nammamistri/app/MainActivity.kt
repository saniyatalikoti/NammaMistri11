package com.nammamistri.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.nammamistri.app.data.AppDatabase
import com.nammamistri.app.data.Attendance
import com.nammamistri.app.data.Expense
import com.nammamistri.app.data.Photo
import com.nammamistri.app.data.Project
import com.nammamistri.app.data.Worker
import com.nammamistri.app.ui.AppLanguage
import com.nammamistri.app.ui.AppText
import com.nammamistri.app.ui.screens.AppScreen
import com.nammamistri.app.ui.screens.ExpenseScreen
import com.nammamistri.app.ui.screens.Header
import com.nammamistri.app.ui.screens.NavigationTabs
import com.nammamistri.app.ui.screens.PhotoScreen
import com.nammamistri.app.ui.screens.ProjectScreen
import com.nammamistri.app.ui.screens.SummaryScreen
import com.nammamistri.app.ui.screens.WorkerScreen
import com.nammamistri.app.ui.theme.NammaMistriTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = AppDatabase.getDatabase(this)

        setContent {
            NammaMistriTheme {
                NammaMistriApp(database)
            }
        }
    }
}

@Composable
private fun NammaMistriApp(database: AppDatabase) {
    val scope = rememberCoroutineScope()
    var language by remember { mutableStateOf(AppLanguage.Kannada) }
    val text = AppText.of(language)
    var currentScreen by remember { mutableStateOf(AppScreen.Projects) }
    var projects by remember { mutableStateOf<List<Project>>(emptyList()) }
    var selectedProject by remember { mutableStateOf<Project?>(null) }
    var workers by remember { mutableStateOf<List<Worker>>(emptyList()) }
    var expenses by remember { mutableStateOf<List<Expense>>(emptyList()) }
    var photos by remember { mutableStateOf<List<Photo>>(emptyList()) }
    var attendance by remember { mutableStateOf<List<Attendance>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }

    fun loadProjects() {
        scope.launch {
            val loaded = withContext(Dispatchers.IO) {
                if (searchQuery.isBlank()) database.projectDao().getAll() else database.projectDao().searchByName(searchQuery)
            }
            withContext(Dispatchers.Main) {
                projects = loaded
                selectedProject = selectedProject?.let { selected ->
                    loaded.firstOrNull { it.id == selected.id } ?: selected
                }
            }
        }
    }

    fun loadProjectData(projectId: Long) {
        scope.launch {
            val loadedWorkers = withContext(Dispatchers.IO) { database.workerDao().getByProject(projectId) }
            val loadedExpenses = withContext(Dispatchers.IO) { database.expenseDao().getByProject(projectId) }
            val loadedPhotos = withContext(Dispatchers.IO) { database.photoDao().getByProject(projectId) }
            val loadedAttendance = withContext(Dispatchers.IO) { database.attendanceDao().getByProject(projectId) }
            val refreshedProject = withContext(Dispatchers.IO) { database.projectDao().getById(projectId) }
            withContext(Dispatchers.Main) {
                workers = loadedWorkers
                expenses = loadedExpenses
                photos = loadedPhotos
                attendance = loadedAttendance
                selectedProject = refreshedProject ?: selectedProject
            }
        }
    }

    fun refreshSelected() {
        selectedProject?.let { loadProjectData(it.id) }
        loadProjects()
    }

    LaunchedEffect(searchQuery) {
        val loaded = withContext(Dispatchers.IO) {
            if (searchQuery.isBlank()) database.projectDao().getAll() else database.projectDao().searchByName(searchQuery)
        }
        withContext(Dispatchers.Main) { projects = loaded }
    }

    LaunchedEffect(selectedProject?.id) {
        selectedProject?.let { project ->
            val loadedWorkers = withContext(Dispatchers.IO) { database.workerDao().getByProject(project.id) }
            val loadedExpenses = withContext(Dispatchers.IO) { database.expenseDao().getByProject(project.id) }
            val loadedPhotos = withContext(Dispatchers.IO) { database.photoDao().getByProject(project.id) }
            val loadedAttendance = withContext(Dispatchers.IO) { database.attendanceDao().getByProject(project.id) }
            withContext(Dispatchers.Main) {
                workers = loadedWorkers
                expenses = loadedExpenses
                photos = loadedPhotos
                attendance = loadedAttendance
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Header(text = text, language = language, onLanguageChange = { language = it })
            NavigationTabs(text = text, current = currentScreen, onNavigate = { currentScreen = it })
            when (currentScreen) {
                AppScreen.Projects -> ProjectScreen(
                    text = text,
                    projects = projects,
                    searchQuery = searchQuery,
                    onSearchChange = { searchQuery = it },
                    onAddProject = { name, totalDays ->
                        scope.launch {
                            val project = Project(name = name, totalDays = totalDays)
                            val id = withContext(Dispatchers.IO) { database.projectDao().insert(project) }
                            withContext(Dispatchers.Main) {
                                selectedProject = project.copy(id = id)
                                currentScreen = AppScreen.Summary
                            }
                            refreshSelected()
                        }
                    },
                    onUpdateProject = { project ->
                        scope.launch {
                            withContext(Dispatchers.IO) { database.projectDao().update(project) }
                            withContext(Dispatchers.Main) { selectedProject = project }
                            refreshSelected()
                        }
                    },
                    onDeleteProject = { project ->
                        scope.launch {
                            withContext(Dispatchers.IO) { database.projectDao().delete(project) }
                            withContext(Dispatchers.Main) {
                                if (selectedProject?.id == project.id) {
                                    selectedProject = null
                                    workers = emptyList()
                                    expenses = emptyList()
                                    photos = emptyList()
                                    attendance = emptyList()
                                }
                            }
                            loadProjects()
                        }
                    },
                    onOpenProject = { project, destination ->
                        selectedProject = project
                        currentScreen = destination
                    }
                )

                AppScreen.Workers -> WorkerScreen(
                    text = text,
                    project = selectedProject,
                    workers = workers,
                    attendance = attendance,
                    onAddWorker = { name, wage, advance, projectId ->
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                database.workerDao().insert(Worker(name = name, wage = wage, advance = advance, projectId = projectId))
                            }
                            loadProjectData(projectId)
                        }
                    },
                    onAttendanceUpdate = { workerId, projectId, daysWorked ->
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                val existing = database.attendanceDao().getByWorker(workerId, projectId)
                                if (existing == null) {
                                    database.attendanceDao().insert(Attendance(workerId = workerId, projectId = projectId, daysWorked = daysWorked))
                                } else {
                                    database.attendanceDao().update(existing.copy(daysWorked = daysWorked))
                                }
                            }
                            loadProjectData(projectId)
                        }
                    }
                )

                AppScreen.Expenses -> ExpenseScreen(
                    text = text,
                    project = selectedProject,
                    expenses = expenses,
                    onAddExpense = { title, amount, projectId ->
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                database.expenseDao().insert(Expense(title = title, amount = amount, projectId = projectId))
                            }
                            loadProjectData(projectId)
                        }
                    }
                )

                AppScreen.Photos -> PhotoScreen(
                    text = text,
                    project = selectedProject,
                    photos = photos,
                    onAddPhoto = { uri, projectId ->
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                database.photoDao().insert(Photo(uri = uri, projectId = projectId))
                            }
                            loadProjectData(projectId)
                        }
                    }
                )

                AppScreen.Summary -> SummaryScreen(
                    text = text,
                    project = selectedProject,
                    workers = workers,
                    expenses = expenses,
                    onUpdateProject = { project ->
                        scope.launch {
                            withContext(Dispatchers.IO) { database.projectDao().update(project) }
                            withContext(Dispatchers.Main) { selectedProject = project }
                            refreshSelected()
                        }
                    }
                )
            }
        }
    }
}
