<div align="center">

<h1>🏗️ NammaMistri</h1>

<p><strong>Construction Work Made Simple</strong></p>

<p><em>NammaMistri is a modern Android application built with Kotlin and Jetpack Compose that helps contractors and site managers track construction projects, manage workers, monitor expenses, and calculate material costs — all in one place.</em></p>

<br/>

<img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
<img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" />
<img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" />
<img src="https://img.shields.io/badge/Database-Room%20SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white" />
<img src="https://img.shields.io/badge/Architecture-MVVM-FF6F00?style=for-the-badge" />
<img src="https://img.shields.io/badge/Status-Active-00C853?style=for-the-badge" />
<img src="https://img.shields.io/badge/Min%20SDK-24-blue?style=for-the-badge" />

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#️-tech-stack)
- [Architecture](#️-architecture)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Dependencies](#-dependencies)
- [Installation & Setup](#️-installation--setup)
- [Database Schema](#️-database-schema)
- [Summary Computation](#-summary-computation)
- [Screenshots](#-screenshots)
- [Future Enhancements](#-future-enhancements)
- [Contributing](#-contributing)
- [License](#-license)
- [Author](#-author)

---

## 📱 Overview

**NammaMistri** (meaning *"Our Craftsman"* in Kannada) is an offline-first construction site management Android application built for contractors, site supervisors, and homeowners.

The app provides a unified platform to manage every aspect of a construction project — from tracking daily worker attendance to computing final project costs — all without requiring an internet connection.

---

## ✨ Features

### 📁 Project Management
- Create and track multiple construction projects
- Set project location, start date, and current status
- Switch between ongoing, paused, and completed states

### 👷 Worker Management
- Add workers with role, daily wage, and contact details
- Log days worked and advance payments per worker
- View net payable amount after advance deduction

### 🧱 Material Calculator
- Estimate material quantities with unit-wise tracking
- Input price per unit for auto cost calculation
- Supports bags, cubic ft, sq ft, and custom units

### 💸 Expense Tracker
- Record and categorise miscellaneous site expenses
- Date-wise expense history per project
- Categories include transport, tools, labour, and misc

### 📸 Photo Management
- Capture and attach progress photos to any project
- Add captions and date stamps to each photo
- Browse photo history per project

### 📊 Summary Dashboard
- View total labour cost, material cost, and expenses
- Grand total computation across all cost heads
- Per-project financial overview at a glance

### 🌐 English / Kannada Support
- Full bilingual UI for local contractor accessibility

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 1.9.22 |
| UI Toolkit | Jetpack Compose (Material 3) |
| Architecture | MVVM + Repository Pattern |
| Local Database | Room (SQLite) 2.6.1 |
| Async / Concurrency | Kotlin Coroutines 1.7.3 + Flow |
| Image Loading | Coil (Compose extension) 2.6.0 |
| Navigation | Jetpack Navigation Compose 2.7.7 |
| Build System | Gradle 8.2+ (Kotlin DSL) |
| IDE | Android Studio Hedgehog+ |

---

## 🏗️ Architecture

NammaMistri follows the **MVVM (Model–View–ViewModel)** pattern recommended by Google, combined with the **Repository Pattern** for clean separation between the UI and data layers.

```
UI Layer (Compose Screens)
        │
        ▼
ViewModel (StateFlow / LiveData)
        │
        ▼
Repository (single source of truth)
        │
        ▼
Room Database (DAOs → SQLite)
```

**Data flow:** Screens observe `StateFlow` from ViewModels. ViewModels delegate all data operations to Repositories, which abstract Room DAO calls. This keeps UI code clean and testable, and the entire stack works fully offline.

---

## 📂 Project Structure

```
NammaMistri/
│
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/saniya/nammamistri/
│   │       │   │
│   │       │   ├── MainActivity.kt                   # App entry point, NavHost setup
│   │       │   │
│   │       │   ├── data/
│   │       │   │   ├── model/                        # Room entity data classes
│   │       │   │   │   ├── Project.kt
│   │       │   │   │   ├── Worker.kt
│   │       │   │   │   ├── Material.kt
│   │       │   │   │   ├── Expense.kt
│   │       │   │   │   └── Photo.kt
│   │       │   │   │
│   │       │   │   ├── dao/                          # Room DAO interfaces
│   │       │   │   │   ├── ProjectDao.kt
│   │       │   │   │   ├── WorkerDao.kt
│   │       │   │   │   ├── MaterialDao.kt
│   │       │   │   │   ├── ExpenseDao.kt
│   │       │   │   │   └── PhotoDao.kt
│   │       │   │   │
│   │       │   │   ├── repository/                   # Data access layer
│   │       │   │   │   ├── ProjectRepository.kt
│   │       │   │   │   ├── WorkerRepository.kt
│   │       │   │   │   ├── MaterialRepository.kt
│   │       │   │   │   ├── ExpenseRepository.kt
│   │       │   │   │   └── PhotoRepository.kt
│   │       │   │   │
│   │       │   │   └── AppDatabase.kt                # Room database instance
│   │       │   │
│   │       │   ├── ui/
│   │       │   │   ├── theme/
│   │       │   │   │   ├── Color.kt
│   │       │   │   │   ├── Theme.kt
│   │       │   │   │   └── Type.kt
│   │       │   │   │
│   │       │   │   ├── screens/                      # Composable screens
│   │       │   │   │   ├── HomeScreen.kt
│   │       │   │   │   ├── ProjectScreen.kt
│   │       │   │   │   ├── WorkerScreen.kt
│   │       │   │   │   ├── MaterialScreen.kt
│   │       │   │   │   ├── ExpenseScreen.kt
│   │       │   │   │   ├── PhotoScreen.kt
│   │       │   │   │   └── SummaryScreen.kt
│   │       │   │   │
│   │       │   │   └── viewmodel/                    # ViewModels per feature
│   │       │   │       ├── ProjectViewModel.kt
│   │       │   │       ├── WorkerViewModel.kt
│   │       │   │       ├── MaterialViewModel.kt
│   │       │   │       └── ExpenseViewModel.kt
│   │       │   │
│   │       │   └── utils/                            # Helpers & constants
│   │       │       ├── Constants.kt
│   │       │       ├── CurrencyFormatter.kt
│   │       │       ├── DateUtils.kt
│   │       │       └── NavRoutes.kt
│   │       │
│   │       └── res/
│   │           ├── drawable/
│   │           ├── mipmap/
│   │           └── values/
│   │               ├── strings.xml
│   │               └── colors.xml
│   │
│   └── build.gradle.kts                              # App-level Gradle config
│
├── gradle/
│   └── libs.versions.toml                            # Centralized dependency versions
├── build.gradle.kts                                  # Project-level Gradle config
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

---

## ✅ Prerequisites

Before you begin, ensure you have the following installed:

| Tool | Minimum Version | Download |
|---|---|---|
| Android Studio | Hedgehog (2023.1.1) | [Download](https://developer.android.com/studio) |
| JDK | 17 | Bundled with Android Studio |
| Android SDK | API 24 (min) / API 34 (target) | Via SDK Manager |
| Kotlin | 1.9.x | Bundled with Android Studio |
| Gradle | 8.2+ | Auto-downloaded via wrapper |
| Git | Latest | [Download](https://git-scm.com) |

---

## 📦 Dependencies

Below is a reference for all major libraries used. Versions are managed centrally in `gradle/libs.versions.toml`.

### Core Android
```toml
[versions]
kotlin                 = "1.9.22"
agp                    = "8.2.2"
core-ktx               = "1.12.0"
lifecycle              = "2.7.0"
activity-compose       = "1.8.2"

[libraries]
androidx-core-ktx        = { module = "androidx.core:core-ktx",                         version.ref = "core-ktx" }
lifecycle-runtime-ktx    = { module = "androidx.lifecycle:lifecycle-runtime-ktx",       version.ref = "lifecycle" }
lifecycle-viewmodel      = { module = "androidx.lifecycle:lifecycle-viewmodel-compose", version.ref = "lifecycle" }
activity-compose         = { module = "androidx.activity:activity-compose",             version.ref = "activity-compose" }
```

### Jetpack Compose (BOM)
```toml
[versions]
compose-bom = "2024.02.00"

[libraries]
compose-bom             = { module = "androidx.compose:compose-bom",           version.ref = "compose-bom" }
compose-ui              = { module = "androidx.compose.ui:ui" }
compose-ui-graphics     = { module = "androidx.compose.ui:ui-graphics" }
compose-ui-tooling      = { module = "androidx.compose.ui:ui-tooling-preview" }
compose-material3       = { module = "androidx.compose.material3:material3" }
compose-icons-extended  = { module = "androidx.compose.material:material-icons-extended" }
```

### Navigation
```toml
[versions]
navigation-compose = "2.7.7"

[libraries]
navigation-compose = { module = "androidx.navigation:navigation-compose", version.ref = "navigation-compose" }
```

### Room
```toml
[versions]
room = "2.6.1"

[libraries]
room-runtime  = { module = "androidx.room:room-runtime",  version.ref = "room" }
room-ktx      = { module = "androidx.room:room-ktx",      version.ref = "room" }
room-compiler = { module = "androidx.room:room-compiler", version.ref = "room" }  # ksp
```

### Coroutines
```toml
[versions]
coroutines = "1.7.3"

[libraries]
coroutines-core    = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-core",    version.ref = "coroutines" }
coroutines-android = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-android", version.ref = "coroutines" }
```

### Image Loading
```toml
[versions]
coil = "2.6.0"

[libraries]
coil-compose = { module = "io.coil-kt:coil-compose", version.ref = "coil" }
```

### `app/build.gradle.kts` — Dependencies Block
```kotlin
dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.activity.compose)

    // Compose BOM
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.material3)
    implementation(libs.compose.icons.extended)

    // Navigation
    implementation(libs.navigation.compose)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Image Loading
    implementation(libs.coil.compose)

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
```

---

## ⚙️ Installation & Setup

### Step 1 — Clone the Repository
```bash
git clone https://github.com/saniyatalikoti/namm_mistri_saniyatalikoti.git
cd namm_mistri_saniyatalikoti
```

### Step 2 — Open in Android Studio
1. Launch **Android Studio**
2. Select **File → Open** and choose the `NammaMistri/` folder
3. Wait for the IDE to index the project

### Step 3 — Sync Gradle
Click **"Sync Now"** in the notification bar, or run:
```bash
./gradlew build
```

### Step 4 — Run the App
- Connect a physical Android device (API 24+) via USB with Developer Options enabled, **or**
- Start an Android Virtual Device (AVD) from **Device Manager → Create Device**
- Press **Run ▶** (Shift + F10)

---

## 🗄️ Database Schema

NammaMistri uses **Room** over SQLite with five entity tables. All relationships are linked via `projectId` as a foreign key.

> **ID convention:** All tables use `autoGenerate = true` integer primary keys. Related tables reference the parent `projects.id` field.

---

### `projects` table

```kotlin
@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,           // Project title
    val location: String,       // Site address or area
    val startDate: String,      // ISO date string "YYYY-MM-DD"
    val status: String          // "ongoing" | "completed" | "paused"
)
```

---

### `workers` table

```kotlin
@Entity(tableName = "workers")
data class Worker(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val projectId: Int,         // FK → projects.id
    val name: String,           // Worker full name
    val role: String,           // e.g. "Mason", "Electrician", "Plumber"
    val wage: Double,           // Daily wage in ₹
    val daysWorked: Int,        // Total days present on site
    val advance: Double         // Advance amount already paid out
)
```

---

### `materials` table

```kotlin
@Entity(tableName = "materials")
data class Material(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val projectId: Int,         // FK → projects.id
    val name: String,           // e.g. "Cement", "Sand", "Steel"
    val quantity: Double,       // Amount used
    val unit: String,           // "bags" | "cubic ft" | "sq ft" | "kg" etc.
    val pricePerUnit: Double    // Cost per unit in ₹
)
```

---

### `expenses` table

```kotlin
@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val projectId: Int,         // FK → projects.id
    val description: String,    // Short description of the expense
    val amount: Double,         // Amount in ₹
    val date: String,           // ISO date string "YYYY-MM-DD"
    val category: String        // "transport" | "tools" | "labour" | "misc"
)
```

---

### `photos` table

```kotlin
@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val projectId: Int,         // FK → projects.id
    val filePath: String,       // Absolute path to image on device storage
    val caption: String,        // Short description of the photo
    val dateTaken: String       // ISO date string "YYYY-MM-DD"
)
```

---

### Table Relationships

```
projects
    │
    ├──► workers   (projectId FK)
    ├──► materials (projectId FK)
    ├──► expenses  (projectId FK)
    └──► photos    (projectId FK)
```

---

## 📊 Summary Computation

The summary dashboard aggregates all three cost heads into a single project financial report:

```kotlin
data class ProjectSummary(
    val totalWorkers: Int,
    val labourCost: Double,        // Net after advance deductions
    val totalMaterialCost: Double, // Quantity × price per unit
    val totalExpenses: Double,     // Sum of all miscellaneous expenses
    val grandTotal: Double         // labourCost + materialCost + expenses
)

fun computeSummary(
    workers: List<Worker>,
    materials: List<Material>,
    expenses: List<Expense>
): ProjectSummary {
    val totalWorkers = workers.size

    // Net labour cost after deducting advances paid
    val labourCost = workers.sumOf { (it.wage * it.daysWorked) - it.advance }

    // Total material cost from quantity × unit price
    val totalMaterialCost = materials.sumOf { it.quantity * it.pricePerUnit }

    // Sum of all miscellaneous site expenses
    val totalExpenses = expenses.sumOf { it.amount }

    // Grand total across all cost heads
    val grandTotal = labourCost + totalMaterialCost + totalExpenses

    return ProjectSummary(
        totalWorkers = totalWorkers,
        labourCost = labourCost,
        totalMaterialCost = totalMaterialCost,
        totalExpenses = totalExpenses,
        grandTotal = grandTotal
    )
}
```

---

## 📸 Screenshots

<div align="center">

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/eb9806a9-e103-4895-8c1b-36cbecc9969c" width="220"/></td>
    <td><img src="https://github.com/user-attachments/assets/93eae877-fbc3-4ae4-9b5c-a7562cbcb8f3" width="220"/></td>
    <td><img src="https://github.com/user-attachments/assets/fbd2f5e3-c3e0-4129-b8f7-f96584de6302" width="220"/></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/ad52d7d4-3a30-4f55-9674-42f576116b92" width="220"/></td>
    <td><img src="https://github.com/user-attachments/assets/54c4f5d6-8841-4432-ae38-45679160670f" width="220"/></td>
    <td><img src="https://github.com/user-attachments/assets/16ac1c2a-0c1e-4c76-9097-ae3c4d47d6b5" width="220"/></td>
  </tr>
</table>

</div>

---

## 📈 Future Enhancements

| Feature | Description |
|---|---|
| 🔔 Push Notifications | Reminders for pending worker payments and project milestones |
| ☁️ Cloud Backup | Firebase Firestore sync for cross-device data access |
| 📍 Location Tagging | Attach GPS coordinates to project sites and photos |
| 📄 PDF Report Export | Generate printable cost summaries and attendance sheets |
| 🌐 Multi-language Support | Kannada, Hindi, Tamil, and English localisation |
| 📊 Analytics Dashboard | Visual charts for cost trends and worker productivity |
| 👷 Contractor Profiles | Shareable worker profile cards with QR codes |
| 💳 Payment Tracking | UPI/cash payment logs with receipt generation |

---

## 🤝 Contributing

Contributions are welcome and appreciated!

1. **Fork** the repository
2. **Create** a feature branch
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. **Commit** your changes with a descriptive message
   ```bash
   git commit -m "feat: add PDF report export"
   ```
4. **Push** to your branch
   ```bash
   git push origin feature/your-feature-name
   ```
5. **Open** a Pull Request against `main`

Please follow the existing code style (Kotlin conventions, Compose best practices) and include a brief description of your changes in the PR.

---

## 📄 License

This project is developed for educational and learning purposes. All rights reserved by the author.

---

## 👩‍💻 Author

<div align="center">

### Saniya Talikoti
**BE in Computer Science Engineering**

Android Developer · Kotlin Learner · Construction Tech Enthusiast

[![GitHub](https://img.shields.io/badge/GitHub-saniyatalikoti-181717?style=for-the-badge&logo=github)](https://github.com/saniyatalikoti)

</div>

---

<div align="center">
  <sub>If you found this project helpful, please consider giving it a ⭐ on GitHub!</sub>
</div>
