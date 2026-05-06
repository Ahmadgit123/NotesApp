# 📒 NotesApp — Kotlin + Jetpack Compose

**Task 1 | Android Development Roadmap**  
*Single Activity Notes App with ViewModel + In-Memory Persistence*

---

## 📱 What This App Does

| Feature | Details |
|---|---|
| 📋 List notes | LazyColumn with animated, color-coded cards |
| ➕ Add note | FAB → Dialog with Title + Content fields |
| ✏️ Edit note | Tap any card → pre-filled dialog |
| 🗑️ Delete note | Long-press card OR tap delete icon |
| 🎨 Visual variety | Each card gets a unique pastel color |
| ⏰ Timestamps | Auto-recorded on create/edit |

---

## 🏗️ Project Structure

```
app/src/main/java/com/ahmadziya/notesapp/
│
├── MainActivity.kt                    ← Entry point, sets Compose content
│
├── model/
│   └── Note.kt                        ← Data class (id, title, content, timestamp)
│
├── viewmodel/
│   └── NotesViewModel.kt              ← Business logic + UI state (ViewModel)
│
└── ui/
    ├── theme/
    │   ├── Color.kt                   ← App colors + card palette
    │   └── Theme.kt                   ← MaterialTheme setup
    ├── components/
    │   ├── NoteCard.kt                ← Individual note card composable
    │   └── AddEditNoteDialog.kt       ← Add/Edit dialog composable
    └── screens/
        └── NotesScreen.kt             ← Main screen (Scaffold + LazyColumn)
```

---

## 🚀 How to Run

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- Android SDK 35
- JDK 11+

### Steps

```bash
# 1. Clone the repo
git clone https://github.com/your-username/NotesApp.git
cd NotesApp

# 2. Open in Android Studio
# File → Open → select the NotesApp folder

# 3. Wait for Gradle sync to complete (first time takes 2-3 min)

# 4. Run on emulator
# Click ▶ Run or press Shift+F10
# Select a virtual device (Pixel 7, API 34 recommended)

# 5. Or build APK
# Build → Build Bundle(s) / APK(s) → Build APK(s)
```

---

## 🧠 Architecture (MVVM)

```
View (Compose UI)
    ↕ observes state / calls functions
ViewModel (NotesViewModel)
    ↕ reads/writes
Model (Note data class + in-memory list)
```

Think of it like a **restaurant**:
- **View** = Waiter (shows menu, takes order from customer)
- **ViewModel** = Manager (decides what to do, no UI logic)
- **Model** = Kitchen (holds the actual data/food)

---

## 🔧 Key Concepts Used

| Concept | Where Used | Why |
|---|---|---|
| `data class` | `Note.kt` | Immutable model with auto equals/hashCode |
| `ViewModel` | `NotesViewModel` | Survives screen rotation |
| `mutableStateListOf` | ViewModel | Compose observes list changes |
| `mutableStateOf` | ViewModel | Compose observes dialog open/close |
| `Scaffold` | NotesScreen | TopAppBar + FAB layout structure |
| `LazyColumn` | NotesScreen | Efficient recycling list (like RecyclerView) |
| `itemsIndexed` | NotesScreen | Get both index and item in LazyColumn |
| `combinedClickable` | NoteCard | Both click and long-click support |
| `remember` | Dialog | Local UI state that survives recomposition |
| `LaunchedEffect` | Dialog | Auto-focus title field on open |

---

## 🚀 Extension Ideas (Future Tasks)

### 1. Room Database (Bonus Task)
Replace in-memory list with Room for persistence across app restarts:

```kotlin
// Add to build.gradle.kts:
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// Create:
@Entity data class NoteEntity(...)
@Dao interface NoteDao { @Query, @Insert, @Delete }
@Database class NotesDatabase(...)
```

### 2. Search Feature
```kotlin
// In NotesViewModel:
var searchQuery = mutableStateOf("")
val filteredNotes get() = notes.filter {
    it.title.contains(searchQuery.value, ignoreCase = true) ||
    it.content.contains(searchQuery.value, ignoreCase = true)
}

// In NotesScreen — add SearchBar composable below TopAppBar
```

### 3. Categories / Labels
Add a `category: String` field to `Note` and filter by category with a chip row.

### 4. Share Note
```kotlin
val intent = Intent(Intent.ACTION_SEND).apply {
    type = "text/plain"
    putExtra(Intent.EXTRA_TEXT, "${note.title}\n\n${note.content}")
}
context.startActivity(Intent.createChooser(intent, "Share Note"))
```

### 5. Pin Important Notes
Add `isPinned: Boolean` to Note, show pinned notes at top with a 📌 icon.

---

## 📦 Dependencies

```
androidx.compose.bom          — Compose version management
material3                     — Material You UI components
lifecycle-viewmodel-compose   — viewModel() in Compose
material-icons-extended       — Add icon for FAB
```

---

*Built by Ahmad Ziya | Final Year CSE | AI-Based Android Development Roadmap*
