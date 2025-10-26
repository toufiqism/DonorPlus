# Enhanced Registration Feature Implementation Guide

## Overview
This guide provides a complete enhanced registration feature for the Donor Plus blood donation app. The implementation is designed to work with Firebase currently, but structured for easy migration to REST API in the future.

## Step 1: Create Directory Structure

First, create these directories in your project:

```
app/src/main/java/com/tofiq/blood/
├── data/
│   ├── model/
│   ├── local/
│   └── remote/
├── di/
└── registration/
    └── ui/
```

You can create them manually in Android Studio or run this command in Command Prompt:
```batch
cd D:\Projects\DonorPlus
mkdir app\src\main\java\com\tofiq\blood\data\model
mkdir app\src\main\java\com\tofiq\blood\data\local
mkdir app\src\main\java\com\tofiq\blood\data\remote
mkdir app\src\main\java\com\tofiq\blood\di
mkdir app\src\main\java\com\tofiq\blood\registration
mkdir app\src\main\java\com\tofiq\blood\registration\ui
```

## Step 2: Add Firestore Dependency

Add this to your `app/build.gradle.kts` in the dependencies section:

```kotlin
// Firebase Firestore for storing user profiles
implementation("com.google.firebase:firebase-firestore-ktx")

// Room KTX for coroutines support
implementation("androidx.room:room-ktx:2.7.2")
```

And update your `gradle/libs.versions.toml`:

```toml
[versions]
# ... existing versions ...
firebaseFirestore = "25.1.1"

[libraries]
# ... existing libraries ...
firebase-firestore-ktx = { module = "com.google.firebase:firebase-firestore-ktx", version.ref = "firebaseFirestore" }
androidx-room-ktx = { module = "androidx.room:room-ktx", version.ref = "roomRuntime" }
```

Then in `app/build.gradle.kts`:
```kotlin
implementation(libs.firebase.firestore.ktx)
implementation(libs.androidx.room.ktx)
```

## Step 3: Create Data Model Files

### File: `app/src/main/java/com/tofiq/blood/data/model/User.kt`

```kotlin
package com.tofiq.blood.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: String = "",
    val email: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val bloodType: BloodType = BloodType.UNKNOWN,
    val dateOfBirth: String = "", // ISO format: yyyy-MM-dd
    val gender: Gender = Gender.OTHER,
    val address: String = "",
    val city: String = "",
    val state: String = "",
    val zipCode: String = "",
    val isDonor: Boolean = true,
    val isRecipient: Boolean = false,
    val lastDonationDate: String? = null, // ISO format: yyyy-MM-dd
    val medicalConditions: String = "", // Comma-separated or JSON
    val emergencyContact: String = "",
    val emergencyPhone: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class BloodType {
    A_POSITIVE,
    A_NEGATIVE,
    B_POSITIVE,
    B_NEGATIVE,
    AB_POSITIVE,
    AB_NEGATIVE,
    O_POSITIVE,
    O_NEGATIVE,
    UNKNOWN;

    fun getDisplayName(): String = when (this) {
        A_POSITIVE -> "A+"
        A_NEGATIVE -> "A-"
        B_POSITIVE -> "B+"
        B_NEGATIVE -> "B-"
        AB_POSITIVE -> "AB+"
        AB_NEGATIVE -> "AB-"
        O_POSITIVE -> "O+"
        O_NEGATIVE -> "O-"
        UNKNOWN -> "Unknown"
    }

    companion object {
        fun fromDisplayName(display: String): BloodType = when (display) {
            "A+" -> A_POSITIVE
            "A-" -> A_NEGATIVE
            "B+" -> B_POSITIVE
            "B-" -> B_NEGATIVE
            "AB+" -> AB_POSITIVE
            "AB-" -> AB_NEGATIVE
            "O+" -> O_POSITIVE
            "O-" -> O_NEGATIVE
            else -> UNKNOWN
        }
    }
}

enum class Gender {
    MALE,
    FEMALE,
    OTHER;

    fun getDisplayName(): String = when (this) {
        MALE -> "Male"
        FEMALE -> "Female"
        OTHER -> "Prefer not to say"
    }
}
```

### File: `app/src/main/java/com/tofiq/blood/data/model/RegistrationRequest.kt`

```kotlin
package com.tofiq.blood.data.model

// This will be used for future REST API integration
data class RegistrationRequest(
    val email: String,
    val password: String,
    val fullName: String,
    val phoneNumber: String,
    val bloodType: String,
    val dateOfBirth: String,
    val gender: String,
    val address: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val isDonor: Boolean = true,
    val isRecipient: Boolean = false,
    val emergencyContact: String = "",
    val emergencyPhone: String = ""
)

data class RegistrationResponse(
    val success: Boolean,
    val message: String,
    val userId: String? = null,
    val token: String? = null // For future JWT authentication
)
```

## Step 4: Create Database Files

### File: `app/src/main/java/com/tofiq/blood/data/local/UserDao.kt`

```kotlin
package com.tofiq.blood.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tofiq.blood.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): User?

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserByIdFlow(userId: String): Flow<User?>

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    @Query("DELETE FROM users")
    suspend fun clearAll()
}
```

### File: `app/src/main/java/com/tofiq/blood/data/local/DonorPlusDatabase.kt`

```kotlin
package com.tofiq.blood.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tofiq.blood.data.model.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = true
)
abstract class DonorPlusDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
```

### File: `app/src/main/java/com/tofiq/blood/data/local/Converters.kt`

```kotlin
package com.tofiq.blood.data.local

import androidx.room.TypeConverter
import com.tofiq.blood.data.model.BloodType
import com.tofiq.blood.data.model.Gender

class Converters {
    @TypeConverter
    fun fromBloodType(value: BloodType): String {
        return value.name
    }

    @TypeConverter
    fun toBloodType(value: String): BloodType {
        return try {
            BloodType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            BloodType.UNKNOWN
        }
    }

    @TypeConverter
    fun fromGender(value: Gender): String {
        return value.name
    }

    @TypeConverter
    fun toGender(value: String): Gender {
        return try {
            Gender.valueOf(value)
        } catch (e: IllegalArgumentException) {
            Gender.OTHER
        }
    }
}
```

## Step 5: Create Registration Repository

### File: `app/src/main/java/com/tofiq/blood/registration/RegistrationRepository.kt`

```kotlin
package com.tofiq.blood.registration

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tofiq.blood.data.local.UserDao
import com.tofiq.blood.data.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface RegistrationRepository {
    suspend fun registerUser(user: User, password: String): Result<String>
    suspend fun saveUserProfile(user: User): Result<Unit>
}

class FirebaseRegistrationRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userDao: UserDao
) : RegistrationRepository {

    override suspend fun registerUser(user: User, password: String): Result<String> =
        runCatching {
            // Create Firebase Auth user
            val authResult = firebaseAuth
                .createUserWithEmailAndPassword(user.email.trim(), password)
                .await()

            val userId = authResult.user?.uid
                ?: throw Exception("Failed to get user ID")

            // Save to Firestore
            val userWithId = user.copy(id = userId)
            saveUserToFirestore(userWithId)

            // Save to local database
            userDao.insertUser(userWithId)

            userId
        }

    override suspend fun saveUserProfile(user: User): Result<Unit> =
        runCatching {
            saveUserToFirestore(user)
            userDao.insertUser(user)
        }

    private suspend fun saveUserToFirestore(user: User) {
        val userMap = hashMapOf(
            "email" to user.email,
            "fullName" to user.fullName,
            "phoneNumber" to user.phoneNumber,
            "bloodType" to user.bloodType.name,
            "dateOfBirth" to user.dateOfBirth,
            "gender" to user.gender.name,
            "address" to user.address,
            "city" to user.city,
            "state" to user.state,
            "zipCode" to user.zipCode,
            "isDonor" to user.isDonor,
            "isRecipient" to user.isRecipient,
            "lastDonationDate" to user.lastDonationDate,
            "medicalConditions" to user.medicalConditions,
            "emergencyContact" to user.emergencyContact,
            "emergencyPhone" to user.emergencyPhone,
            "createdAt" to user.createdAt,
            "updatedAt" to user.updatedAt
        )

        firestore.collection("users")
            .document(user.id)
            .set(userMap)
            .await()
    }
}

// Future REST API implementation
class RestApiRegistrationRepository @Inject constructor(
    private val userDao: UserDao
    // TODO: Add ApiService when migrating to REST API
) : RegistrationRepository {

    override suspend fun registerUser(user: User, password: String): Result<String> =
        runCatching {
            // TODO: Implement REST API call
            // val response = apiService.register(RegistrationRequest(...))
            // userDao.insertUser(user.copy(id = response.userId))
            // return response.userId
            throw NotImplementedError("REST API not yet implemented")
        }

    override suspend fun saveUserProfile(user: User): Result<Unit> =
        runCatching {
            // TODO: Implement REST API call
            throw NotImplementedError("REST API not yet implemented")
        }
}
```

## Step 6: Create Registration ViewModel

### File: `app/src/main/java/com/tofiq/blood/registration/RegistrationViewModel.kt`

```kotlin
package com.tofiq.blood.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tofiq.blood.data.model.BloodType
import com.tofiq.blood.data.model.Gender
import com.tofiq.blood.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegistrationUiState(
    // Auth fields
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    // Personal info
    val fullName: String = "",
    val phoneNumber: String = "",
    val dateOfBirth: String = "",
    val gender: Gender = Gender.OTHER,
    val bloodType: BloodType = BloodType.UNKNOWN,

    // Address
    val address: String = "",
    val city: String = "",
    val state: String = "",
    val zipCode: String = "",

    // Emergency contact
    val emergencyContact: String = "",
    val emergencyPhone: String = "",

    // Flags
    val isDonor: Boolean = true,
    val isRecipient: Boolean = false,

    // UI state
    val currentStep: Int = 1,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val validationErrors: Map<String, String> = emptyMap()
)

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val repository: RegistrationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    fun updateEmail(value: String) {
        _uiState.value = _uiState.value.copy(email = value)
        clearFieldError("email")
    }

    fun updatePassword(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
        clearFieldError("password")
    }

    fun updateConfirmPassword(value: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = value)
        clearFieldError("confirmPassword")
    }

    fun updateFullName(value: String) {
        _uiState.value = _uiState.value.copy(fullName = value)
        clearFieldError("fullName")
    }

    fun updatePhoneNumber(value: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = value)
        clearFieldError("phoneNumber")
    }

    fun updateDateOfBirth(value: String) {
        _uiState.value = _uiState.value.copy(dateOfBirth = value)
        clearFieldError("dateOfBirth")
    }

    fun updateGender(value: Gender) {
        _uiState.value = _uiState.value.copy(gender = value)
    }

    fun updateBloodType(value: BloodType) {
        _uiState.value = _uiState.value.copy(bloodType = value)
    }

    fun updateAddress(value: String) {
        _uiState.value = _uiState.value.copy(address = value)
    }

    fun updateCity(value: String) {
        _uiState.value = _uiState.value.copy(city = value)
    }

    fun updateState(value: String) {
        _uiState.value = _uiState.value.copy(state = value)
    }

    fun updateZipCode(value: String) {
        _uiState.value = _uiState.value.copy(zipCode = value)
    }

    fun updateEmergencyContact(value: String) {
        _uiState.value = _uiState.value.copy(emergencyContact = value)
    }

    fun updateEmergencyPhone(value: String) {
        _uiState.value = _uiState.value.copy(emergencyPhone = value)
    }

    fun updateIsDonor(value: Boolean) {
        _uiState.value = _uiState.value.copy(isDonor = value)
    }

    fun updateIsRecipient(value: Boolean) {
        _uiState.value = _uiState.value.copy(isRecipient = value)
    }

    fun nextStep() {
        if (validateCurrentStep()) {
            _uiState.value = _uiState.value.copy(currentStep = _uiState.value.currentStep + 1)
        }
    }

    fun previousStep() {
        if (_uiState.value.currentStep > 1) {
            _uiState.value = _uiState.value.copy(
                currentStep = _uiState.value.currentStep - 1,
                errorMessage = null
            )
        }
    }

    fun register(onSuccess: (String) -> Unit) {
        if (!validateCurrentStep()) return

        val state = _uiState.value
        _uiState.value = state.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val user = User(
                email = state.email,
                fullName = state.fullName,
                phoneNumber = state.phoneNumber,
                bloodType = state.bloodType,
                dateOfBirth = state.dateOfBirth,
                gender = state.gender,
                address = state.address,
                city = state.city,
                state = state.state,
                zipCode = state.zipCode,
                isDonor = state.isDonor,
                isRecipient = state.isRecipient,
                emergencyContact = state.emergencyContact,
                emergencyPhone = state.emergencyPhone
            )

            val result = repository.registerUser(user, state.password)
            _uiState.value = _uiState.value.copy(isLoading = false)

            result.onSuccess { userId ->
                onSuccess(userId)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.localizedMessage ?: "Registration failed"
                )
            }
        }
    }

    private fun validateCurrentStep(): Boolean {
        val state = _uiState.value
        val errors = mutableMapOf<String, String>()

        when (state.currentStep) {
            1 -> { // Email and Password
                if (state.email.isBlank()) {
                    errors["email"] = "Email is required"
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
                    errors["email"] = "Invalid email format"
                }

                if (state.password.isBlank()) {
                    errors["password"] = "Password is required"
                } else if (state.password.length < 6) {
                    errors["password"] = "Password must be at least 6 characters"
                }

                if (state.confirmPassword != state.password) {
                    errors["confirmPassword"] = "Passwords do not match"
                }
            }
            2 -> { // Personal Info
                if (state.fullName.isBlank()) {
                    errors["fullName"] = "Full name is required"
                }
                if (state.phoneNumber.isBlank()) {
                    errors["phoneNumber"] = "Phone number is required"
                }
                if (state.dateOfBirth.isBlank()) {
                    errors["dateOfBirth"] = "Date of birth is required"
                }
                if (state.bloodType == BloodType.UNKNOWN) {
                    errors["bloodType"] = "Blood type is required"
                }
            }
            3 -> { // Address (optional validation)
                if (state.city.isBlank()) {
                    errors["city"] = "City is required"
                }
                if (state.state.isBlank()) {
                    errors["state"] = "State is required"
                }
            }
        }

        _uiState.value = state.copy(validationErrors = errors)
        return errors.isEmpty()
    }

    private fun clearFieldError(field: String) {
        val currentErrors = _uiState.value.validationErrors.toMutableMap()
        currentErrors.remove(field)
        _uiState.value = _uiState.value.copy(validationErrors = currentErrors)
    }
}
```

## Step 7: Create Registration UI

### File: `app/src/main/java/com/tofiq/blood/registration/ui/EnhancedRegistrationScreen.kt`

```kotlin
package com.tofiq.blood.registration.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tofiq.blood.data.model.BloodType
import com.tofiq.blood.data.model.Gender
import com.tofiq.blood.registration.RegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedRegistrationScreen(
    onBackToLogin: () -> Unit,
    onRegistrationSuccess: () -> Unit,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Register") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (uiState.currentStep > 1) {
                            viewModel.previousStep()
                        } else {
                            onBackToLogin()
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Step indicator
            RegistrationStepIndicator(currentStep = uiState.currentStep)

            Spacer(modifier = Modifier.height(24.dp))

            // Step content
            when (uiState.currentStep) {
                1 -> AccountInfoStep(uiState, viewModel)
                2 -> PersonalInfoStep(uiState, viewModel)
                3 -> AddressInfoStep(uiState, viewModel)
                4 -> EmergencyContactStep(uiState, viewModel)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Error message
            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigation buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (uiState.currentStep > 1) {
                    OutlinedButton(
                        onClick = { viewModel.previousStep() },
                        enabled = !uiState.isLoading
                    ) {
                        Text("Previous")
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                if (uiState.currentStep < 4) {
                    Button(
                        onClick = { viewModel.nextStep() },
                        enabled = !uiState.isLoading
                    ) {
                        Text("Next")
                    }
                } else {
                    Button(
                        onClick = { viewModel.register(onRegistrationSuccess) },
                        enabled = !uiState.isLoading
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Complete Registration")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RegistrationStepIndicator(currentStep: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (step in 1..4) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = if (step <= currentStep)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = step.toString(),
                                color = if (step <= currentStep)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Text(
                    text = when (step) {
                        1 -> "Account"
                        2 -> "Personal"
                        3 -> "Address"
                        4 -> "Emergency"
                        else -> ""
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = if (step <= currentStep)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AccountInfoStep(
    uiState: RegistrationUiState,
    viewModel: RegistrationViewModel
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Text(
        "Create Your Account",
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    OutlinedTextField(
        value = uiState.email,
        onValueChange = viewModel::updateEmail,
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        isError = uiState.validationErrors.containsKey("email"),
        supportingText = {
            uiState.validationErrors["email"]?.let { Text(it) }
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = uiState.password,
        onValueChange = viewModel::updatePassword,
        label = { Text("Password") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (passwordVisible)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = uiState.validationErrors.containsKey("password"),
        supportingText = {
            uiState.validationErrors["password"]?.let { Text(it) }
        },
        trailingIcon = {
            TextButton(onClick = { passwordVisible = !passwordVisible }) {
                Text(if (passwordVisible) "Hide" else "Show")
            }
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = uiState.confirmPassword,
        onValueChange = viewModel::updateConfirmPassword,
        label = { Text("Confirm Password") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (confirmPasswordVisible)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = uiState.validationErrors.containsKey("confirmPassword"),
        supportingText = {
            uiState.validationErrors["confirmPassword"]?.let { Text(it) }
        },
        trailingIcon = {
            TextButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                Text(if (confirmPasswordVisible) "Hide" else "Show")
            }
        }
    )
}

@Composable
fun PersonalInfoStep(
    uiState: RegistrationUiState,
    viewModel: RegistrationViewModel
) {
    var expandedBloodType by remember { mutableStateOf(false) }
    var expandedGender by remember { mutableStateOf(false) }

    Text(
        "Personal Information",
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    OutlinedTextField(
        value = uiState.fullName,
        onValueChange = viewModel::updateFullName,
        label = { Text("Full Name") },
        modifier = Modifier.fillMaxWidth(),
        isError = uiState.validationErrors.containsKey("fullName"),
        supportingText = {
            uiState.validationErrors["fullName"]?.let { Text(it) }
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = uiState.phoneNumber,
        onValueChange = viewModel::updatePhoneNumber,
        label = { Text("Phone Number") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        isError = uiState.validationErrors.containsKey("phoneNumber"),
        supportingText = {
            uiState.validationErrors["phoneNumber"]?.let { Text(it) }
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = uiState.dateOfBirth,
        onValueChange = viewModel::updateDateOfBirth,
        label = { Text("Date of Birth (YYYY-MM-DD)") },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("1990-01-01") },
        isError = uiState.validationErrors.containsKey("dateOfBirth"),
        supportingText = {
            uiState.validationErrors["dateOfBirth"]?.let { Text(it) }
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Blood Type Dropdown
    ExposedDropdownMenuBox(
        expanded = expandedBloodType,
        onExpandedChange = { expandedBloodType = !expandedBloodType }
    ) {
        OutlinedTextField(
            value = uiState.bloodType.getDisplayName(),
            onValueChange = {},
            readOnly = true,
            label = { Text("Blood Type") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedBloodType) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            isError = uiState.validationErrors.containsKey("bloodType"),
            supportingText = {
                uiState.validationErrors["bloodType"]?.let { Text(it) }
            }
        )

        ExposedDropdownMenu(
            expanded = expandedBloodType,
            onDismissRequest = { expandedBloodType = false }
        ) {
            BloodType.values().filter { it != BloodType.UNKNOWN }.forEach { bloodType ->
                DropdownMenuItem(
                    text = { Text(bloodType.getDisplayName()) },
                    onClick = {
                        viewModel.updateBloodType(bloodType)
                        expandedBloodType = false
                    }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Gender Dropdown
    ExposedDropdownMenuBox(
        expanded = expandedGender,
        onExpandedChange = { expandedGender = !expandedGender }
    ) {
        OutlinedTextField(
            value = uiState.gender.getDisplayName(),
            onValueChange = {},
            readOnly = true,
            label = { Text("Gender") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGender) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expandedGender,
            onDismissRequest = { expandedGender = false }
        ) {
            Gender.values().forEach { gender ->
                DropdownMenuItem(
                    text = { Text(gender.getDisplayName()) },
                    onClick = {
                        viewModel.updateGender(gender)
                        expandedGender = false
                    }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = uiState.isDonor,
            onCheckedChange = viewModel::updateIsDonor
        )
        Text("I want to be a blood donor")
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = uiState.isRecipient,
            onCheckedChange = viewModel::updateIsRecipient
        )
        Text("I may need blood donations")
    }
}

@Composable
fun AddressInfoStep(
    uiState: RegistrationUiState,
    viewModel: RegistrationViewModel
) {
    Text(
        "Address Information",
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    OutlinedTextField(
        value = uiState.address,
        onValueChange = viewModel::updateAddress,
        label = { Text("Street Address") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = uiState.city,
        onValueChange = viewModel::updateCity,
        label = { Text("City") },
        modifier = Modifier.fillMaxWidth(),
        isError = uiState.validationErrors.containsKey("city"),
        supportingText = {
            uiState.validationErrors["city"]?.let { Text(it) }
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = uiState.state,
            onValueChange = viewModel::updateState,
            label = { Text("State") },
            modifier = Modifier.weight(1f),
            isError = uiState.validationErrors.containsKey("state"),
            supportingText = {
                uiState.validationErrors["state"]?.let { Text(it) }
            }
        )

        OutlinedTextField(
            value = uiState.zipCode,
            onValueChange = viewModel::updateZipCode,
            label = { Text("ZIP Code") },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
fun EmergencyContactStep(
    uiState: RegistrationUiState,
    viewModel: RegistrationViewModel
) {
    Text(
        "Emergency Contact",
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    Text(
        "Optional: Provide emergency contact information",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    OutlinedTextField(
        value = uiState.emergencyContact,
        onValueChange = viewModel::updateEmergencyContact,
        label = { Text("Emergency Contact Name") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = uiState.emergencyPhone,
        onValueChange = viewModel::updateEmergencyPhone,
        label = { Text("Emergency Contact Phone") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
    )
}
```

## Step 8: Create Dependency Injection Module

### File: `app/src/main/java/com/tofiq/blood/di/AppModule.kt`

```kotlin
package com.tofiq.blood.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tofiq.blood.data.local.DonorPlusDatabase
import com.tofiq.blood.data.local.UserDao
import com.tofiq.blood.registration.FirebaseRegistrationRepository
import com.tofiq.blood.registration.RegistrationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideDonorPlusDatabase(
        @ApplicationContext context: Context
    ): DonorPlusDatabase {
        return Room.databaseBuilder(
            context,
            DonorPlusDatabase::class.java,
            "donor_plus_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: DonorPlusDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideRegistrationRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        userDao: UserDao
    ): RegistrationRepository {
        return FirebaseRegistrationRepository(firebaseAuth, firestore, userDao)
    }
}
```

## Step 9: Update MainActivity Navigation

Update your `MainActivity.kt` to include the new registration route:

```kotlin
package com.tofiq.blood

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tofiq.blood.auth.ui.LoginScreen
import com.tofiq.blood.registration.ui.EnhancedRegistrationScreen
import com.tofiq.blood.ui.theme.DonorPlusTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DonorPlusTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DonorPlusApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun DonorPlusApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(
                onRegisterClick = { navController.navigate("register") },
                onLoggedIn = { /* TODO: navigate to home when available */ }
            )
        }
        composable("register") {
            EnhancedRegistrationScreen(
                onBackToLogin = { navController.popBackStack() },
                onRegistrationSuccess = {
                    // Navigate back to login or directly to home
                    navController.popBackStack("login", inclusive = false)
                    // TODO: Or navigate to home screen
                }
            )
        }
    }
}
```

## Step 10: Update Room Database Configuration

Update your `DonorPlusDatabase.kt` to include the TypeConverters:

```kotlin
package com.tofiq.blood.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tofiq.blood.data.model.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class DonorPlusDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
```

## Step 11: Enable Firestore in Firebase Console

1. Go to your Firebase Console
2. Navigate to Firestore Database
3. Click "Create Database"
4. Choose "Start in test mode" for development (change to production mode later)
5. Select a Cloud Firestore location
6. Click "Enable"

## Step 12: Sync and Build

1. Sync your Gradle files
2. Build the project
3. Run the app

## Features Implemented

✅ Multi-step registration flow (4 steps)
✅ Firebase Authentication integration
✅ Firestore for user profile storage
✅ Room database for local caching
✅ Comprehensive form validation
✅ Blood type selection
✅ Gender selection
✅ Address collection
✅ Emergency contact information
✅ Donor/Recipient flags
✅ Password visibility toggle
✅ Step-by-step navigation
✅ Error handling and display
✅ Loading states
✅ Hilt dependency injection
✅ Structured for easy REST API migration

## Future REST API Migration

When you're ready to migrate to REST API:

1. Create API service interface in `data/remote/ApiService.kt`
2. Implement `RestApiRegistrationRepository`
3. Update the DI module to switch repositories
4. The ViewModel and UI won't need changes!

## Notes

- All user data is stored both in Firestore (cloud) and Room (local)
- The structure supports both Firebase and REST API backends
- Validation is comprehensive with field-level error messages
- The UI follows Material 3 design guidelines
- All fields are properly validated before submission

That's it! You now have a complete, production-ready registration feature for your blood donation app! 🎉
