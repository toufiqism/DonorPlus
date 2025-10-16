Donor Plus - Firebase Email/Password Auth (Compose)

This app now includes basic Login and Registration screens using Firebase Authentication.

Prerequisites
- Android Studio Jellyfish or later
- Java 11 toolchain
- A Firebase project with an Android app registered for package `com.tofiq.blood`
- Enable Email/Password sign-in in Firebase Console → Authentication → Sign-in method

Setup
1. Place your Firebase config at `app/google-services.json`.
2. Ensure the `applicationId` matches the package name in your Firebase app (`com.tofiq.blood`).
3. Sync Gradle. The project already applies the Google Services plugin and Firebase BOM.

Dependencies
- Firebase BOM `com.google.firebase:firebase-bom`
- Firebase Auth KTX `com.google.firebase:firebase-auth-ktx`

These are declared via `libs.versions.toml` and `app/build.gradle.kts`.

Code Overview
- `app/src/main/java/com/tofiq/blood/MainActivity.kt`: Initializes Firebase and sets Compose navigation with routes `login` and `register`.
- `app/src/main/java/com/tofiq/blood/auth/AuthRepository.kt`: `FirebaseAuthRepository` implements login/registration via `FirebaseAuth`.
- `app/src/main/java/com/tofiq/blood/auth/AuthViewModel.kt`: Holds UI state and exposes `login` and `register` actions.
- `app/src/main/java/com/tofiq/blood/auth/ui/AuthScreens.kt`: `LoginScreen` and `RegisterScreen` Composables.

iOS note
This is an Android-only project. If you later create a Flutter version to support iOS, use `firebase_auth` plugin (which supports iOS) and add the iOS `GoogleService-Info.plist` to the iOS target.

UI/Behavior
- Minimal email/password fields, loading indicator, and error messages.
- Navigation between Login and Registration.
- On success, the current code leaves a TODO to navigate to the main/home screen.

Troubleshooting
- If login/register fails immediately, confirm Email/Password provider is enabled in Firebase Console.
- Ensure the SHA-1/256 fingerprints are added to Firebase if you use features requiring them (not required for basic email/password).
- If `FirebaseApp.initializeApp(this)` fails, check the `google-services.json` placement under the `app/` module and re-sync Gradle.

Future Improvements
- Introduce Hilt injection for `AuthRepository` and `AuthViewModel`.
- Add form validation and password rules.
- Add a Home screen and route after authentication.

