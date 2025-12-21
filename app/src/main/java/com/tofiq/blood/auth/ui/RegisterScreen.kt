package com.tofiq.blood.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tofiq.blood.auth.AuthViewModel
import com.tofiq.blood.auth.ui.components.AuthAnimatedCard
import com.tofiq.blood.auth.ui.components.AuthButton
import com.tofiq.blood.auth.ui.components.AuthErrorMessage
import com.tofiq.blood.auth.ui.components.AuthFooter
import com.tofiq.blood.auth.ui.components.AuthHeader
import com.tofiq.blood.auth.ui.components.AuthLogo
import com.tofiq.blood.auth.ui.components.AuthTextField
import com.tofiq.blood.auth.ui.components.DatePickerField
import com.tofiq.blood.auth.ui.components.DropdownItem
import com.tofiq.blood.auth.ui.components.PasswordField
import com.tofiq.blood.auth.ui.components.PhoneNumberField
import com.tofiq.blood.auth.ui.components.RegisterScreenState
import com.tofiq.blood.auth.ui.components.SelectDropdown
import com.tofiq.blood.auth.ui.components.TermsCheckbox
import com.tofiq.blood.data.model.BloodGroup
import com.tofiq.blood.data.model.UserRole
import com.tofiq.blood.ui.components.DonorPlusGradientBackground
import com.tofiq.blood.ui.theme.AccentCoral
import com.tofiq.blood.ui.theme.PrimaryRed
import com.tofiq.blood.ui.theme.SecondaryBlue
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.ZoneId

// ============== Dropdown Items (defined outside composable for stability) ==============

private val roleDropdownItems = UserRole.entries.map { role ->
    DropdownItem(value = role, displayText = role.name)
}

private val bloodGroupDropdownItems = BloodGroup.entries.map { bloodGroup ->
    DropdownItem(value = bloodGroup, displayText = bloodGroup.name.replace("_", " "))
}

/**
 * Registration screen for new user signup
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit,
    onRegistered: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val screenState = remember { RegisterScreenState() }
    val datePickerState = rememberDatePickerState()
    var previousLoadingState by remember { mutableStateOf(false) }

    // Detect successful registration
    LaunchedEffect(uiState.isLoading, uiState.phoneNumber, uiState.fullName) {
        if (previousLoadingState && !uiState.isLoading) {
            if (uiState.phoneNumber.isEmpty() &&
                uiState.fullName.isEmpty() &&
                uiState.errorMessage == null
            ) {
                screenState.updateShowSuccessDialog(true)
            }
        }
        previousLoadingState = uiState.isLoading
    }

    LaunchedEffect(Unit) {
        delay(100)
        screenState.startAnimation()
    }

    DonorPlusGradientBackground(reversed = true) {
        RegisterContent(
            animateContent = screenState.animateContent,
            phoneNumber = uiState.phoneNumber,
            onPhoneNumberChange = viewModel::updatePhoneNumber,
            password = uiState.password,
            onPasswordChange = viewModel::updatePassword,
            fullName = uiState.fullName,
            onFullNameChange = viewModel::updateFullName,
            role = uiState.role,
            onRoleChange = viewModel::updateRole,
            bloodGroup = uiState.bloodGroup,
            onBloodGroupChange = viewModel::updateBloodGroup,
            lastDonationDate = uiState.lastDonationDate?.toString() ?: "",
            agreedToTerms = uiState.agreedToTerms,
            onAgreedToTermsChange = viewModel::updateAgreedToTerms,
            isLoading = uiState.isLoading,
            errorMessage = uiState.errorMessage,
            passwordVisible = screenState.passwordVisible,
            onTogglePasswordVisibility = screenState::togglePasswordVisibility,
            roleMenuExpanded = screenState.roleMenuExpanded,
            onRoleMenuExpandedChange = screenState::updateRoleMenuExpanded,
            bloodGroupMenuExpanded = screenState.bloodGroupMenuExpanded,
            onBloodGroupMenuExpandedChange = screenState::updateBloodGroupMenuExpanded,
            onShowDatePicker = { screenState.updateShowDatePicker(true) },
            onRegisterClick = { viewModel.registerUser { } },
            onLoginClick = onLoginClick
        )

        // Date Picker Dialog
        if (screenState.showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { screenState.updateShowDatePicker(false) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val instant = Instant.ofEpochMilli(millis)
                                val date = instant.atZone(ZoneId.systemDefault()).toLocalDate()
                                viewModel.updateLastDonationDate(date)
                            }
                            screenState.updateShowDatePicker(false)
                        }
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { screenState.updateShowDatePicker(false) }) { Text("Cancel") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // Success Dialog
        if (screenState.showSuccessDialog) {
            RegistrationSuccessDialog(
                onDismiss = {
                    screenState.updateShowSuccessDialog(false)
                    onRegistered()
                }
            )
        }
    }
}

@Composable
private fun RegisterContent(
    animateContent: Boolean,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    fullName: String,
    onFullNameChange: (String) -> Unit,
    role: UserRole?,
    onRoleChange: (UserRole) -> Unit,
    bloodGroup: BloodGroup?,
    onBloodGroupChange: (BloodGroup) -> Unit,
    lastDonationDate: String,
    agreedToTerms: Boolean,
    onAgreedToTermsChange: (Boolean) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    roleMenuExpanded: Boolean,
    onRoleMenuExpandedChange: (Boolean) -> Unit,
    bloodGroupMenuExpanded: Boolean,
    onBloodGroupMenuExpandedChange: (Boolean) -> Unit,
    onShowDatePicker: () -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthLogo(
            animate = animateContent,
            primaryColor = SecondaryBlue,
            secondaryColor = AccentCoral,
            rotationDirection = -180f
        )

        Spacer(modifier = Modifier.height(24.dp))

        AuthHeader(
            title = "Join Us!",
            subtitle = "Create your account to become a hero",
            titleColor = SecondaryBlue,
            animate = animateContent
        )

        Spacer(modifier = Modifier.height(40.dp))

        RegisterCard(
            visible = animateContent,
            phoneNumber = phoneNumber,
            onPhoneNumberChange = onPhoneNumberChange,
            password = password,
            onPasswordChange = onPasswordChange,
            fullName = fullName,
            onFullNameChange = onFullNameChange,
            role = role,
            onRoleChange = onRoleChange,
            bloodGroup = bloodGroup,
            onBloodGroupChange = onBloodGroupChange,
            lastDonationDate = lastDonationDate,
            agreedToTerms = agreedToTerms,
            onAgreedToTermsChange = onAgreedToTermsChange,
            isLoading = isLoading,
            errorMessage = errorMessage,
            passwordVisible = passwordVisible,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            roleMenuExpanded = roleMenuExpanded,
            onRoleMenuExpandedChange = onRoleMenuExpandedChange,
            bloodGroupMenuExpanded = bloodGroupMenuExpanded,
            onBloodGroupMenuExpandedChange = onBloodGroupMenuExpandedChange,
            onShowDatePicker = onShowDatePicker,
            onRegisterClick = onRegisterClick
        )

        Spacer(modifier = Modifier.height(24.dp))

        RegisterFooter(
            animate = animateContent,
            onLoginClick = onLoginClick
        )
    }
}

@Composable
private fun RegisterCard(
    visible: Boolean,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    fullName: String,
    onFullNameChange: (String) -> Unit,
    role: UserRole?,
    onRoleChange: (UserRole) -> Unit,
    bloodGroup: BloodGroup?,
    onBloodGroupChange: (BloodGroup) -> Unit,
    lastDonationDate: String,
    agreedToTerms: Boolean,
    onAgreedToTermsChange: (Boolean) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    roleMenuExpanded: Boolean,
    onRoleMenuExpandedChange: (Boolean) -> Unit,
    bloodGroupMenuExpanded: Boolean,
    onBloodGroupMenuExpandedChange: (Boolean) -> Unit,
    onShowDatePicker: () -> Unit,
    onRegisterClick: () -> Unit
) {
    AuthAnimatedCard(visible = visible) {
        RegisterCardContent(
            phoneNumber = phoneNumber,
            onPhoneNumberChange = onPhoneNumberChange,
            password = password,
            onPasswordChange = onPasswordChange,
            fullName = fullName,
            onFullNameChange = onFullNameChange,
            role = role,
            onRoleChange = onRoleChange,
            bloodGroup = bloodGroup,
            onBloodGroupChange = onBloodGroupChange,
            lastDonationDate = lastDonationDate,
            agreedToTerms = agreedToTerms,
            onAgreedToTermsChange = onAgreedToTermsChange,
            isLoading = isLoading,
            errorMessage = errorMessage,
            passwordVisible = passwordVisible,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            roleMenuExpanded = roleMenuExpanded,
            onRoleMenuExpandedChange = onRoleMenuExpandedChange,
            bloodGroupMenuExpanded = bloodGroupMenuExpanded,
            onBloodGroupMenuExpandedChange = onBloodGroupMenuExpandedChange,
            onShowDatePicker = onShowDatePicker,
            onRegisterClick = onRegisterClick
        )
    }
}

@Composable
private fun RegisterCardContent(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    fullName: String,
    onFullNameChange: (String) -> Unit,
    role: UserRole?,
    onRoleChange: (UserRole) -> Unit,
    bloodGroup: BloodGroup?,
    onBloodGroupChange: (BloodGroup) -> Unit,
    lastDonationDate: String,
    agreedToTerms: Boolean,
    onAgreedToTermsChange: (Boolean) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    roleMenuExpanded: Boolean,
    onRoleMenuExpandedChange: (Boolean) -> Unit,
    bloodGroupMenuExpanded: Boolean,
    onBloodGroupMenuExpandedChange: (Boolean) -> Unit,
    onShowDatePicker: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PhoneNumberField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            placeholder = "+1234567890"
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordField(
            value = password,
            onValueChange = onPasswordChange,
            passwordVisible = passwordVisible,
            onToggleVisibility = onTogglePasswordVisibility,
            placeholder = "Minimum 8 characters"
        )

        Spacer(modifier = Modifier.height(16.dp))

        AuthTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            label = "Full Name",
            leadingIcon = Icons.Default.Person
        )

        Spacer(modifier = Modifier.height(16.dp))

        SelectDropdown(
            selectedValue = role,
            items = roleDropdownItems,
            onItemSelected = onRoleChange,
            expanded = roleMenuExpanded,
            onExpandedChange = onRoleMenuExpandedChange,
            label = "Role *",
            leadingIcon = Icons.Default.AccountCircle,
            leadingIconTint = SecondaryBlue
        )

        Spacer(modifier = Modifier.height(16.dp))

        SelectDropdown(
            selectedValue = bloodGroup,
            items = bloodGroupDropdownItems,
            onItemSelected = onBloodGroupChange,
            expanded = bloodGroupMenuExpanded,
            onExpandedChange = onBloodGroupMenuExpandedChange,
            label = "Blood Group *",
            leadingIcon = Icons.Default.Favorite,
            leadingIconTint = PrimaryRed,
            placeholder = "Select Blood Group"
        )

        if (role == UserRole.DONOR) {
            Spacer(modifier = Modifier.height(16.dp))
            DatePickerField(
                value = lastDonationDate,
                onClick = onShowDatePicker,
                label = "Last Donation Date (Optional)",
                leadingIcon = Icons.Default.CalendarToday
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TermsCheckbox(
            checked = agreedToTerms,
            onCheckedChange = onAgreedToTermsChange
        )

        Spacer(modifier = Modifier.height(24.dp))

        AuthButton(
            text = "Create Account",
            onClick = onRegisterClick,
            isLoading = isLoading,
            backgroundColor = SecondaryBlue
        )

        errorMessage?.let { msg ->
            Spacer(modifier = Modifier.height(16.dp))
            AuthErrorMessage(message = msg)
        }
    }
}

@Composable
private fun RegisterFooter(
    animate: Boolean,
    onLoginClick: () -> Unit
) {
    AuthFooter(
        promptText = "Already have an account?",
        actionText = "Sign In",
        onActionClick = onLoginClick,
        actionColor = PrimaryRed,
        animate = animate
    )
}

@Composable
private fun RegistrationSuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { },
        title = {
            Text(
                text = "Registration Successful!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = SecondaryBlue
            )
        },
        text = {
            Text(
                text = "Your account has been created successfully. You can now log in with your phone number and password.",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = SecondaryBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("OK")
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}
