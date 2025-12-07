package com.tofiq.blood.auth.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tofiq.blood.auth.AuthViewModel
import com.tofiq.blood.auth.ui.components.AuthButton
import com.tofiq.blood.auth.ui.components.AuthErrorMessage
import com.tofiq.blood.auth.ui.components.AuthHeader
import com.tofiq.blood.auth.ui.components.AuthLogo
import com.tofiq.blood.auth.ui.components.AuthTextField
import com.tofiq.blood.auth.ui.components.PasswordField
import com.tofiq.blood.auth.ui.components.PhoneNumberField
import com.tofiq.blood.auth.ui.components.RegisterScreenState
import com.tofiq.blood.data.model.BloodGroup
import com.tofiq.blood.data.model.UserRole
import com.tofiq.blood.ui.theme.AccentCoral
import com.tofiq.blood.ui.theme.GradientEnd
import com.tofiq.blood.ui.theme.GradientMiddle
import com.tofiq.blood.ui.theme.GradientStart
import com.tofiq.blood.ui.theme.PrimaryRed
import com.tofiq.blood.ui.theme.SecondaryBlue
import com.tofiq.blood.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.ZoneId

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(GradientEnd, GradientMiddle, GradientStart),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            )
    ) {
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

@OptIn(ExperimentalMaterial3Api::class)
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
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        ) + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
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
}

@OptIn(ExperimentalMaterial3Api::class)
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

        RoleDropdown(
            selectedRole = role,
            expanded = roleMenuExpanded,
            onExpandedChange = onRoleMenuExpandedChange,
            onRoleSelected = onRoleChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        BloodGroupDropdown(
            selectedBloodGroup = bloodGroup,
            expanded = bloodGroupMenuExpanded,
            onExpandedChange = onBloodGroupMenuExpandedChange,
            onBloodGroupSelected = onBloodGroupChange
        )

        if (role == UserRole.DONOR) {
            Spacer(modifier = Modifier.height(16.dp))
            LastDonationDateField(
                date = lastDonationDate,
                onClick = onShowDatePicker
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RoleDropdown(
    selectedRole: UserRole?,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onRoleSelected: (UserRole) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange(!expanded) }
    ) {
        OutlinedTextField(
            value = selectedRole?.name ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Role *") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Role",
                    tint = SecondaryBlue
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SecondaryBlue,
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedLabelColor = SecondaryBlue
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            UserRole.entries.forEach { role ->
                DropdownMenuItem(
                    text = { Text(role.name) },
                    onClick = {
                        onRoleSelected(role)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BloodGroupDropdown(
    selectedBloodGroup: BloodGroup?,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onBloodGroupSelected: (BloodGroup) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange(!expanded) }
    ) {
        OutlinedTextField(
            value = selectedBloodGroup?.name?.replace("_", " ") ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Blood Group *") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Blood Group",
                    tint = PrimaryRed
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SecondaryBlue,
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedLabelColor = SecondaryBlue
            ),
            placeholder = { Text("Select Blood Group") }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            BloodGroup.entries.forEach { bloodGroup ->
                DropdownMenuItem(
                    text = { Text(bloodGroup.name.replace("_", " ")) },
                    onClick = {
                        onBloodGroupSelected(bloodGroup)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

@Composable
private fun LastDonationDateField(
    date: String,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = date,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text("Last Donation Date (Optional)") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Last Donation Date",
                    tint = SecondaryBlue
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Pick Date",
                    tint = SecondaryBlue
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SecondaryBlue,
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedLabelColor = SecondaryBlue,
                disabledBorderColor = Color(0xFFE0E0E0),
                disabledTextColor = Color.Black,
                disabledLabelColor = SecondaryBlue
            ),
            placeholder = { Text("Select date") }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable(onClick = onClick)
        )
    }
}

@Composable
private fun TermsCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = SecondaryBlue)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "I agree to the terms and conditions *",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
                .clickable { onCheckedChange(!checked) }
        )
    }
}

@Composable
private fun RegisterFooter(
    animate: Boolean,
    onLoginClick: () -> Unit
) {
    val contentAlpha by animateFloatAsState(
        targetValue = if (animate) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "footerAlpha"
    )

    Row(
        modifier = Modifier.alpha(contentAlpha),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Already have an account?",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.width(4.dp))
        TextButton(
            onClick = onLoginClick,
            colors = ButtonDefaults.textButtonColors(contentColor = PrimaryRed)
        ) {
            Text(
                text = "Sign In",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
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
