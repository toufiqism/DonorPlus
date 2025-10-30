Donor Plus - Blood Donation Management App

This app includes beautiful, animated Login screens with REST API authentication, configurable backend settings, and modern Material Design 3 UI components.

## Authentication

### REST API Login (Default)
The app uses a REST API backend for authentication with phone number and password. The base URL is configurable through the Settings screen.

**Default Base URL**: `http://192.168.103.177:8080`
**Login Endpoint**: `/api/v1/auth/login`

### REST API Registration
The app includes a comprehensive registration feature that allows users to create accounts with all required information.

**Registration Endpoint**: `/api/v1/auth/register`

**Registration Fields**:
- **Phone Number** (Required): Must match pattern `^\\+?[0-9]{10,15}$`
- **Password** (Required): Minimum 8 characters
- **Full Name** (Required): User's full name
- **Role** (Required): Either `DONOR` or `REQUESTER`
- **Blood Group** (Required): One of the 8 blood types (A_POSITIVE, A_NEGATIVE, B_POSITIVE, B_NEGATIVE, AB_POSITIVE, AB_NEGATIVE, O_POSITIVE, O_NEGATIVE)
- **Agreed to Terms** (Required): User must agree to terms and conditions
- **Last Donation Date** (Optional): Only shown for donors, ISO date format
- **Latitude/Longitude** (Optional): Location coordinates if user chooses to share location

### Firebase Authentication (Alternative)
Firebase authentication support is retained for backward compatibility. To switch to Firebase:
- Modify `Module.kt` to bind `FirebaseAuthRepository` instead of `RestAuthRepository`
- Enable Email/Password sign-in in Firebase Console → Authentication → Sign-in method

## Prerequisites
- Android Studio Jellyfish or later
- Java 11 toolchain
- A backend server running at the configured base URL (or use the settings screen to change it)
- (Optional) A Firebase project if using Firebase authentication

## Setup
1. Launch the app and tap the Settings icon on the login screen
2. Configure the base URL to point to your backend server
3. For new users: Tap "Sign Up" on the login screen to register
4. Fill in all required registration fields:
   - Phone number (10-15 digits, optional + prefix)
   - Password (minimum 8 characters)
   - Full name
   - Select role (DONOR or REQUESTER)
   - Select blood group
   - Agree to terms and conditions
   - (Optional for donors) Select last donation date
5. Tap "Create Account" to register
6. Use phone number and password to login
7. (Optional) Place your Firebase config at `app/google-services.json` if using Firebase

## Dependencies
- **Retrofit** - REST API client
- **OkHttp** - HTTP client with logging interceptor
- **Gson** - JSON serialization/deserialization
- **SharedPreferences** - Local storage for auth tokens and settings
- **Firebase** (optional) - Alternative authentication backend
- **Jetpack Compose** with Material Design 3
- **Hilt** for Dependency Injection
- **Room** for local database (prepared for future use)

These are declared via `libs.versions.toml` and `app/build.gradle.kts`.

## Code Overview

### Architecture
- **MVVM Pattern**: Following best practices with ViewModel and Repository separation
- **Repository Pattern**: Abstract repository interface with multiple implementations (REST API, Firebase)
- **Dependency Injection**: Using Hilt for better testability and SOLID principles
- **Unidirectional Data Flow**: State flows from ViewModel to UI components
- **Single Source of Truth (SSOT)**: UI state managed centrally in ViewModels
- **Clean Architecture**: Separation of concerns with data, domain, and presentation layers

### Key Components

#### Authentication
- `app/src/main/java/com/tofiq/blood/auth/AuthRepository.kt`: 
  - `AuthRepository` interface defining authentication contract
  - `RestAuthRepository` implementation for REST API login and registration
  - `FirebaseAuthRepository` implementation for Firebase authentication (backward compatibility)
- `app/src/main/java/com/tofiq/blood/auth/AuthViewModel.kt`: 
  - Manages authentication UI state with phone number and password support
  - Handles registration with all required fields
  - Validates phone number format, password length, and required fields
  - Manages role selection, blood group selection, and terms agreement
- `app/src/main/java/com/tofiq/blood/auth/SettingsViewModel.kt`: Manages settings UI state for base URL configuration

#### UI Screens
- `app/src/main/java/com/tofiq/blood/auth/ui/AuthScreens.kt`: 
  - `LoginScreen` with phone number input and settings button
  - `RegisterScreen` with comprehensive registration form including:
    - Phone number and password fields
    - Full name input
    - Role selection (DONOR/REQUESTER)
    - Blood group dropdown
    - Terms and conditions checkbox
    - Optional last donation date picker (for donors)
    - Date picker dialog for selecting donation dates
- `app/src/main/java/com/tofiq/blood/auth/ui/SettingsScreen.kt`: Configure backend base URL with validation

#### Data Layer
- `app/src/main/java/com/tofiq/blood/data/model/`:
  - `LoginRequest.kt` - Request model with phoneNumber and password
  - `LoginResponse.kt` - Response model with token and user info
  - `RegisterRequest.kt` - Request model with all registration fields
  - `RegisterResponse.kt` - Response model with token and user info after registration
  - `UserRole.kt` - Enum for user roles (DONOR, REQUESTER)
  - `BloodGroup.kt` - Enum for blood groups (8 types)
  - `AuthToken.kt` - Token storage model
- `app/src/main/java/com/tofiq/blood/data/remote/AuthApiService.kt`: Retrofit API service interface with login and register endpoints
- `app/src/main/java/com/tofiq/blood/data/local/PreferencesManager.kt`: SharedPreferences wrapper for secure storage

#### Dependency Injection
- `app/src/main/java/com/tofiq/blood/di/Module.kt`:
  - `AuthRepositoryModule` - Provides AuthRepository implementation
  - `NetworkModule` - Provides Retrofit, OkHttp, and API services
  - `FirebaseModule` - Provides Firebase instances (optional)

#### Navigation
- `app/src/main/java/com/tofiq/blood/MainActivity.kt`: 
  - Navigation setup with routes: `login`, `register`, `settings`
  - Handles navigation state and back stack management

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

## API Integration

### Login Request
```json
POST /api/v1/auth/login
Content-Type: application/json

{
  "phoneNumber": "1234567890",
  "password": "your_password"
}
```

### Registration Request
```json
POST /api/v1/auth/register
Content-Type: application/json

{
  "phoneNumber": "+1234567890",
  "password": "secure_password123",
  "fullName": "John Doe",
  "role": "DONOR",
  "agreedToTerms": true,
  "bloodGroup": "O_POSITIVE",
  "lastDonationDate": "2025-01-20",
  "latitude": 40.7128,
  "longitude": -74.0060
}
```

**Note**: 
- `lastDonationDate` is optional and only applicable for DONOR role
- `latitude` and `longitude` are optional
- `role` must be either "DONOR" or "REQUESTER"
- `bloodGroup` must be one of: A_POSITIVE, A_NEGATIVE, B_POSITIVE, B_NEGATIVE, AB_POSITIVE, AB_NEGATIVE, O_POSITIVE, O_NEGATIVE

### Success Response
```json
{
  "success": true,
  "statusCode": 200,
  "message": "Registration successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "refresh_token_here",
    "userId": "user_id_here",
    "phoneNumber": "+1234567890"
  }
}
```

### Error Response
```json
{
  "success": false,
  "statusCode": 400,
  "message": "Invalid phone number or password",
  "data": null
}
```

## Configuration

### Changing Base URL
1. Open the app and tap the Settings icon (⚙️) on the login screen
2. Enter the new base URL (e.g., `http://192.168.1.100:8080` or `https://api.yourserver.com`)
3. Tap "Save Configuration"
4. The app will immediately use the new URL for all API calls

### Default Configuration
- Base URL: `http://192.168.103.177:8080`
- Connection Timeout: 30 seconds
- Read/Write Timeout: 30 seconds
- Logging: Enabled (Body-level logging for debugging)

## Troubleshooting

### REST API Issues
- **Login fails with network error**: 
  - Verify the backend server is running at the configured URL
  - Check if the device/emulator can reach the server (use ping or browser)
  - Ensure the backend accepts requests from your device's IP
  - Check the Logcat for detailed error messages (OkHttp logging is enabled)
- **Base URL not saving**: 
  - Ensure the URL format is correct (must start with http:// or https://)
  - Check SharedPreferences permissions

### Firebase Issues (if using Firebase)
- If login/register fails immediately, confirm Email/Password provider is enabled in Firebase Console.
- Ensure the SHA-1/256 fingerprints are added to Firebase if you use features requiring them (not required for basic email/password).
- If `FirebaseApp.initializeApp(this)` fails, check the `google-services.json` placement under the `app/` module and re-sync Gradle.

### UI Issues
- If animations appear janky, ensure hardware acceleration is enabled on your device/emulator.
- If the settings screen doesn't open, check navigation configuration in MainActivity.kt

## Recent Updates (October 2025)

### REST API Login Implementation
- ✅ Implemented REST API authentication with configurable base URL
- ✅ Phone number and password login (replaced email login)
- ✅ Secure token storage using SharedPreferences
- ✅ Settings screen for configuring backend URL
- ✅ Dynamic Retrofit instance with configurable base URL
- ✅ Proper error handling and validation
- ✅ Network logging for debugging
- ✅ Backward compatibility with Firebase authentication

### New Features
- **Settings Screen**: 
  - Configure backend base URL from the UI
  - URL validation (protocol and format checking)
  - Reset to default URL option
  - Success/error feedback with animations
- **Phone Number Login**:
  - Updated login screen to use phone number instead of email
  - Input validation for phone number and password
  - Better error messages for network and API errors
- **Registration Feature**:
  - Complete registration form with all required fields
  - Role selection (DONOR/REQUESTER) with dropdown menu
  - Blood group selection with all 8 types
  - Terms and conditions checkbox
  - Date picker for last donation date (donors only)
  - Client-side validation for all fields
  - Phone number format validation (10-15 digits)
  - Password length validation (minimum 8 characters)
  - Error handling for registration failures
- **Token Management**:
  - Secure storage of auth tokens and refresh tokens
  - Automatic token retrieval on app restart
  - Clean logout with token cleanup

### Login & Registration UI Overhaul
- ✅ Implemented colorful, modern design with gradient backgrounds
- ✅ Added smooth entrance animations for all UI elements
- ✅ Forced light mode for consistent branding
- ✅ Added password visibility toggle
- ✅ Improved form fields with icons and better styling
- ✅ Enhanced button designs with shadows and rounded corners
- ✅ Better error message presentation
- ✅ Responsive design for all screen sizes
- ✅ Settings icon in top-right corner for easy access
- ✅ Maintained existing functionality - no breaking changes

### Design Principles Applied
- **SOLID Pattern**: Single responsibility, proper separation of concerns
- **Unidirectional Flow**: State flows one way from ViewModel to UI
- **SSOT**: Single source of truth for authentication state
- **Proper Error Handling**: All error cases handled gracefully
- **Null Safety**: Comprehensive null checking throughout
- **Clean Architecture**: Clear separation between data, domain, and presentation layers

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

## Security Considerations

### Current Implementation
- Auth tokens stored in SharedPreferences (encrypted by Android system)
- HTTPS should be used for production base URLs
- Phone numbers and passwords validated before sending to API
- Network logging should be disabled in production builds

### Recommendations for Production
1. **Use HTTPS**: Always use HTTPS for API communication
2. **Encrypt SharedPreferences**: Consider using EncryptedSharedPreferences for sensitive data
3. **Implement Token Refresh**: Add automatic token refresh logic
4. **Add Certificate Pinning**: Pin SSL certificates for additional security
5. **Obfuscate Code**: Enable ProGuard/R8 for release builds
6. **Disable Logging**: Set logging level to NONE in production

## Future Improvements
- ✅ ~~Implement REST API login with phone number~~ (Completed)
- ✅ ~~Add configurable base URL settings~~ (Completed)
- ✅ ~~Implement registration endpoint integration~~ (Completed)
- Add phone number format validation with country code support
- Add location picker with map integration for optional location sharing
- Add password reset functionality via SMS/OTP
- Implement token refresh mechanism
- Add a Home screen and route after authentication
- Implement biometric authentication option
- Add remember me functionality with encrypted storage
- Implement social login options (Google, Facebook)
- Add offline mode with local data caching
- Implement form validation and password strength indicator
- Add OTP verification for phone numbers
- Multi-factor authentication (MFA) support

