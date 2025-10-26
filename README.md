Donor Plus - Firebase Email/Password Auth (Compose)

This app includes beautiful, animated Login and Registration screens using Firebase Authentication with modern Material Design 3.

## Prerequisites
- Android Studio Jellyfish or later
- Java 11 toolchain
- A Firebase project with an Android app registered for package `com.tofiq.blood`
- Enable Email/Password sign-in in Firebase Console → Authentication → Sign-in method

## Setup
1. Place your Firebase config at `app/google-services.json`.
2. Ensure the `applicationId` matches the package name in your Firebase app (`com.tofiq.blood`).
3. Sync Gradle. The project already applies the Google Services plugin and Firebase BOM.

## Dependencies
- Firebase BOM `com.google.firebase:firebase-bom`
- Firebase Auth KTX `com.google.firebase:firebase-auth-ktx`
- Jetpack Compose with Material Design 3
- Hilt for Dependency Injection

These are declared via `libs.versions.toml` and `app/build.gradle.kts`.

## Code Overview

### Architecture
- **MVVM Pattern**: Following best practices with ViewModel and Repository separation
- **Dependency Injection**: Using Hilt for better testability and SOLID principles
- **Unidirectional Data Flow**: State flows from ViewModel to UI components
- **Single Source of Truth (SSOT)**: UI state managed centrally in AuthViewModel

### Key Components
- `app/src/main/java/com/tofiq/blood/MainActivity.kt`: Initializes Firebase and sets Compose navigation with routes `login` and `register`.
- `app/src/main/java/com/tofiq/blood/auth/AuthRepository.kt`: `FirebaseAuthRepository` implements login/registration via `FirebaseAuth`.
- `app/src/main/java/com/tofiq/blood/auth/AuthViewModel.kt`: Holds UI state and exposes `login` and `register` actions.
- `app/src/main/java/com/tofiq/blood/auth/ui/AuthScreens.kt`: Modern, animated `LoginScreen` and `RegisterScreen` Composables.
- `app/src/main/java/com/tofiq/blood/ui/theme/`: Theme configuration with custom color schemes and typography.

## UI/UX Features

### Modern Design Elements
- **Light Mode Only**: Consistent, vibrant color scheme optimized for the blood donation theme
- **Gradient Backgrounds**: Beautiful gradient backgrounds with soft, pastel colors
- **Animated Entrance Effects**: 
  - Logo scales and rotates on screen entry
  - Content fades in smoothly
  - Login/Register cards slide up with spring animations
- **Password Visibility Toggle**: Users can show/hide password with an eye icon
- **Custom Color Scheme**:
  - Primary Red (#E63946) - Blood donation theme
  - Secondary Blue (#457B9D) - Calm and trustworthy
  - Accent Coral (#FF6B6B) - Warm and inviting
  - Gradient backgrounds for visual appeal
- **Material Design 3**: 
  - Rounded corners (24dp for cards, 28dp for buttons)
  - Elevated cards with shadows
  - Icons for email and password fields
  - Improved typography with bold headers

### User Experience
- Email/password fields with validation
- Loading indicators during authentication
- Error messages displayed in styled containers
- Smooth navigation between Login and Registration screens
- Responsive design that works on all Android screen sizes
- Proper null handling and error states

### Animations
- **Logo Animation**: Bouncy scale and rotation effect (1000ms)
- **Content Fade**: Smooth fade-in for text elements (800ms)
- **Card Slide**: Spring-based slide-up animation for form cards
- All animations use Material motion principles for natural feel

## Platform Support

### Android
- Minimum SDK: As defined in `build.gradle.kts`
- Target SDK: Latest stable
- Supports all screen sizes (phone, tablet)
- Light mode enforced for consistent branding

### iOS Note
This is an Android-only project. If you later create a Flutter version to support iOS:
- Use `firebase_auth` plugin (which supports iOS)
- Add the iOS `GoogleService-Info.plist` to the iOS target
- Ensure all animations are compatible with iOS
- Test the gradient backgrounds on iOS devices

## Error Handling
- Graceful handling of network failures
- Firebase authentication errors displayed to users
- Null safety throughout the codebase
- Loading states prevent duplicate requests

## Troubleshooting
- If login/register fails immediately, confirm Email/Password provider is enabled in Firebase Console.
- Ensure the SHA-1/256 fingerprints are added to Firebase if you use features requiring them (not required for basic email/password).
- If `FirebaseApp.initializeApp(this)` fails, check the `google-services.json` placement under the `app/` module and re-sync Gradle.
- If animations appear janky, ensure hardware acceleration is enabled on your device/emulator.

## Recent Updates (October 2025)

### Login & Registration UI Overhaul
- ✅ Implemented colorful, modern design with gradient backgrounds
- ✅ Added smooth entrance animations for all UI elements
- ✅ Forced light mode for consistent branding
- ✅ Added password visibility toggle
- ✅ Improved form fields with icons and better styling
- ✅ Enhanced button designs with shadows and rounded corners
- ✅ Better error message presentation
- ✅ Responsive design for all screen sizes
- ✅ Maintained existing functionality - no breaking changes

### Design Principles Applied
- **SOLID Pattern**: Single responsibility, proper separation of concerns
- **Unidirectional Flow**: State flows one way from ViewModel to UI
- **SSOT**: Single source of truth for authentication state
- **Proper Error Handling**: All error cases handled gracefully
- **Null Safety**: Comprehensive null checking throughout

## Design System

### Component Library
The app includes a comprehensive design system with reusable components. All new features should use these components to maintain consistency.

**Location**: `app/src/main/java/com/tofiq/blood/ui/components/DonorPlusComponents.kt`

### Quick Start
```kotlin
// Import components
import com.tofiq.blood.ui.components.*
import com.tofiq.blood.ui.theme.*

// Use in your screen
@Composable
fun MyScreen() {
    DonorPlusSolidBackground {
        Column(modifier = Modifier.padding(DonorPlusSpacing.L)) {
            DonorPlusSectionHeader(text = "My Screen")
            
            DonorPlusCard {
                DonorPlusTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = "Input"
                )
            }
            
            DonorPlusPrimaryButton(
                onClick = { /* Action */ },
                text = "Submit"
            )
        }
    }
}
```

### Available Components
- **Backgrounds**: `DonorPlusGradientBackground`, `DonorPlusSolidBackground`
- **Buttons**: `DonorPlusPrimaryButton`, `DonorPlusSecondaryButton`, `DonorPlusOutlinedButton`, `DonorPlusTextButton`
- **Cards**: `DonorPlusCard`, `DonorPlusAnimatedCard`
- **Headers**: `DonorPlusHeader`, `DonorPlusSectionHeader`
- **Fields**: `DonorPlusTextField`
- **Messages**: `DonorPlusErrorMessage`, `DonorPlusInfoMessage`, `DonorPlusSuccessMessage`
- **Other**: `DonorPlusAnimatedLogo`, `DonorPlusLoadingIndicator`, `DonorPlusContentContainer`

### Documentation
- **Quick Start**: See `DESIGN_SYSTEM_QUICK_START.md` for immediate usage
- **Full Guide**: See `DESIGN_SYSTEM_GUIDE.md` for comprehensive documentation
- **Examples**: See `app/src/main/java/com/tofiq/blood/ui/examples/ExampleScreens.kt` for screen patterns
- **Visual Details**: See `VISUAL_FEATURES.md` for design specifications

### Design Principles
When building new features, follow these principles:
1. **Use components from DonorPlusComponents.kt** - Don't create custom buttons or cards
2. **Follow the color palette** - Use `PrimaryRed`, `SecondaryBlue`, `AccentCoral`, etc.
3. **Use spacing system** - `DonorPlusSpacing.XS/S/M/L/XL` for all spacing
4. **Add animations** - Use animated components for better UX
5. **Handle states** - Show loading, error, and success states properly
6. **Test responsively** - Ensure it works on all screen sizes

## Future Improvements
- Add form validation and password strength indicator
- Implement email verification flow
- Add password reset functionality
- Add a Home screen and route after authentication
- Implement biometric authentication option
- Add remember me functionality
- Implement social login options (Google, Facebook)

