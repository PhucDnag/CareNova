# CareNova

CareNova is an Android healthcare app built for both patients and doctors. It supports role-based sign in, appointment booking and management, medical records, reminders, notifications, profile management, language switching, and a simple AI-assisted workflow through an API key.

## Main features

- Role-based login for **Patient** and **Doctor**
- Home dashboard with services and shortcuts
- Appointment booking and appointment detail views
- Doctor workspace for examining patients, viewing records, and managing treatment plans
- Patient profile with:
  - personal information
  - medical history
  - reminder setup
  - dark mode
  - language switch
  - about us and feedback actions
  - logout
- Notifications screen for updates and reminders
- Vietnamese and English localization
- AI integration using a Groq API key stored in Gradle properties

## Tech stack

- **Language:** Java
- **UI:** Android XML layouts
- **Build system:** Gradle Kotlin DSL
- **Min SDK:** 24
- **Target SDK:** 36
- **Libraries used:**
  - AndroidX AppCompat
  - Material Components
  - ConstraintLayout
  - Glide
  - Jsoup
  - OkHttp
  - org.json

## Project structure

```text
app/
├── src/main/
│   ├── java/com/example/btl_android/
│   │   ├── Adapter/            # RecyclerView adapters
│   │   ├── Database/           # SQLite database helper and persistence logic
│   │   ├── Object/             # Data models
│   │   ├── LoginActivity.java  # Entry screen
│   │   ├── MainActivity1.java  # Main app container
│   │   └── ...                 # Activities, fragments, utilities
│   ├── res/
│   │   ├── layout/             # XML screens
│   │   ├── drawable/           # Icons, backgrounds, images
│   │   ├── mipmap*/            # App launcher icons
│   │   ├── values/             # English strings, colors, themes
│   │   └── values-vi/          # Vietnamese strings
│   └── AndroidManifest.xml
├── build.gradle.kts
└── proguard-rules.pro
```

## How the app works

### 1. Sign in and role selection
The app starts from `LoginActivity`. Users choose whether they are a patient or a doctor. Each role routes to a different set of screens and features.

### 2. Localization and theme
The app supports English and Vietnamese through Android resource folders:
- `res/values/strings.xml`
- `res/values-vi/strings.xml`

Language switching is handled with `AppCompatDelegate.setApplicationLocales(...)` and the selected locale is applied across the app.

Dark mode is managed separately per role using `ThemeRoleManager`.

### 3. Persistence and storage
The app stores local data in SQLite through `DatabaseHelper`.

Typical stored data includes:
- patient profiles
- appointments
- notifications
- medical records
- treatment plans
- reminders

Some UI images are loaded with Glide, and profile image paths can be stored in the database.

### 4. AI/API configuration
The project reads the Groq API key from Gradle properties and exposes it through `BuildConfig.GROQ_API_KEY`.

The key is declared in `app/build.gradle.kts`:

```kotlin
val groqApiKey = (project.findProperty("GROQ_API_KEY") as String?) ?: ""
buildConfigField("String", "GROQ_API_KEY", "\"$groqApiKey\"")
```

This means you should store the actual key in your local Gradle properties instead of hardcoding it into source files.

## Local setup

### Requirements
- Android Studio
- JDK 11
- Android SDK 36 or compatible
- Git

### Run the app locally

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle
4. Add your Groq API key if you use AI features
5. Run the app on an emulator or device

### Add the API key
Create or edit your local `gradle.properties` file and add:

```properties
GROQ_API_KEY=your_api_key_here
```

Do not commit real secrets to GitHub.

## Build configuration

Important settings from `app/build.gradle.kts`:

- `applicationId`: `com.example.btl_android`
- `minSdk`: `24`
- `compileSdk`: `36`
- `targetSdk`: `36`
- `versionName`: `1.0`
- `versionCode`: `1`

## AndroidManifest

The manifest registers:
- launcher activity
- patient screens
- doctor screens
- reminder receiver
- required permissions such as internet, vibration, notifications, and exact alarms

App name is read from `@string/app_name`, so changing the string resource updates the displayed app name.

## Naming and branding

The app name displayed to users is `CareNova`. If you are renaming from an older project name, make sure these locations are updated:
- `res/values/strings.xml` → `app_name`
- `res/values-vi/strings.xml` → `app_name`
- launcher icon assets if you want to match the new branding
- README, GitHub repo title, and release notes

## Notes for GitHub

Before pushing to GitHub:
- remove local build outputs from version control
- do not commit secrets such as API keys
- keep only source files, resources, and configuration needed to build the app
- if you generate new launcher icons, keep the final exported assets in `res/mipmap*`

## License

Add your preferred license here before publishing the repository.
