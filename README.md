# FestForge — Event Staff Booking Platform (Android App)

**Package ID:** `com.rehan.festforge`

FestForge is an **EVENT STAFF BOOKING PLATFORM** built with Native Android, Kotlin, Jetpack Compose, Material 3, MVVM, and Firebase.

---

## 🌟 Core Features & User Roles

### 1. Customer Role
- **Indian Phone Auth (+91)** with 6-digit OTP verification.
- **Home Dashboard**: Categories (Waiter, Butler, Captain, Supervisor, Bartender, Chef, Helper, Housekeeping, Event Staff), location greeting, featured workers.
- **Worker Search & Filter**: Filter by category, city, experience, rating, rate, and availability.
- **Worker Profile**: Bio, skills, hourly rate, rating, completed jobs, customer reviews.
- **Complete Booking Flow**:
  - Event date & time shift picker
  - Staff quantity selection
  - Venue address & special instructions
  - Fare breakdown (Subtotal + 5% platform service charge)
  - Booking confirmation & real-time document creation
- **Booking History**: Filtered tabs for Upcoming, Active, Completed, Cancelled.
- **Review System**: 1–5 star rating and written review after job completion.

### 2. Worker Role
- **Worker Onboarding**: Primary category, skills, hourly rate, experience, city, bio, and Aadhaar/Gov ID proof upload.
- **Worker Dashboard**:
  - Availability toggle switch (Online / Offline)
  - Today's earnings and job requests list
  - Accept / Reject job requests
  - Active & upcoming event shifts
- **Earnings Summary**: Lifetime earnings, weekly earnings, shift history.

### 3. Admin Role
- **Admin Dashboard**: System metrics (Revenue, Total Users, Total Staff, Total Bookings).
- **Identity Verification**: Review pending worker ID proofs, Approve/Reject worker profiles.
- **Category Management**: Add, edit, or toggle event staff categories.
- **User Directory**: View account roles and manage users.

---

## 🛠️ Project Structure

```
FestForge/
├── .github/workflows/android.yml      # CI/CD Workflow for assembleDebug APK
├── app/
│   ├── build.gradle.kts               # App module build configuration
│   ├── src/main/
│   │   ├── AndroidManifest.xml        # Manifest permissions & Activity declaration
│   │   ├── java/com/rehan/festforge/
│   │   │   ├── FestForgeApplication.kt
│   │   │   ├── MainActivity.kt
│   │   │   ├── data/
│   │   │   │   ├── model/             # User, WorkerProfile, Booking, Category, Review, Notification, AppConfig
│   │   │   │   ├── repository/        # Auth, Worker, Booking, Category, Review, Notification, Admin
│   │   │   │   ├── datastore/         # UserPreferences
│   │   │   │   └── firebase/          # Messaging Service
│   │   │   ├── viewmodel/             # ViewModels for all screens
│   │   │   ├── ui/
│   │   │   │   ├── theme/             # Material 3 Gold/Dark theme
│   │   │   │   ├── components/        # Header, WorkerCard, BookingCard, StatusChip, TextField, Button
│   │   │   │   └── screens/           # Splash, Auth, Customer, Booking, Worker, Review, Admin, Settings
│   │   │   └── navigation/            # NavRoutes & FestForgeNavGraph
├── firestore.rules                    # Security rules
├── storage.rules                      # Storage rules
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

---

## 🔒 Security & Rules
- **Worker Self-Verification Prevented**: Workers cannot mark themselves as VERIFIED in Firestore. Only Admin can update `verificationStatus`.
- **Admin Role Restricted**: Admin role cannot be self-assigned during normal registration.
- **Protected Storage**: Identity verification documents are readable only by the uploading worker and Admin.

## Build-ready fixes applied
This package includes a portable `gradlew`/`gradlew.bat` launcher pinned to Gradle 8.7, removes the missing custom debug-keystore dependency, and makes the Google Services plugin conditional on the real `app/google-services.json` file. Firebase Phone Authentication uses `PhoneAuthProvider` rather than a simulated OTP.

For real Firebase operation, create an Android app with package `com.rehan.festforge`, place the genuine `google-services.json` at `app/google-services.json`, enable Phone Authentication, add the required SHA fingerprints, and deploy the included Firestore/Storage rules. Never commit service-account keys or signing secrets.
