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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tofiq.blood.auth.AuthViewModel
import com.tofiq.blood.auth.ui.components.AuthButton
import com.tofiq.blood.auth.ui.components.AuthErrorMessage
import com.tofiq.blood.auth.ui.components.AuthHeader
import com.tofiq.blood.auth.ui.components.AuthLogo
import com.tofiq.blood.auth.ui.components.AuthTextField
import com.tofiq.blood.auth.ui.components.PasswordField
import com.tofiq.blood.auth.ui.components.PhoneNumberField
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

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    onLoggedIn: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    var animateContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        animateContent = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(GradientStart, GradientMiddle, GradientEnd),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            )
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
                primaryColor = PrimaryRed,
                secondaryColor = AccentCoral,
                rotationDirection = 180f
            )

            Spacer(modifier = Modifier.height(24.dp))

            AuthHeader(
                title = "Welcome Back!",
                subtitle = "Sign in to save lives",
                titleColor = PrimaryRed,
                animate = animateContent
            )

            Spacer(modifier = Modifier.height(40.dp))

            LoginCard(
                visible = animateContent,
                phoneNumber = uiState.phoneNumber,
                onPhoneNumberChange = viewModel::updatePhoneNumber,
                password = uiState.password,
                onPasswordChange = viewModel::updatePassword,
                passwordVisible = passwordVisible,
                onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
                isLoading = uiState.isLoading,
                onLoginClick = { viewModel.loginWithPhone(onLoggedIn) },
                errorMessage = uiState.errorMessage
            )

            Spacer(modifier = Modifier.height(24.dp))

            LoginFooter(
                animate = animateContent,
                onRegisterClick = onRegisterClick
            )
        }

        // Settings Button
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun LoginCard(
    visible: Boolean,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    isLoading: Boolean,
    onLoginClick: () -> Unit,
    errorMessage: String?
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PhoneNumberField(
                    value = phoneNumber,
                    onValueChange = onPhoneNumberChange
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordField(
                    value = password,
                    onValueChange = onPasswordChange,
                    passwordVisible = passwordVisible,
                    onToggleVisibility = onTogglePasswordVisibility
                )

                Spacer(modifier = Modifier.height(24.dp))

                AuthButton(
                    text = "Sign In",
                    onClick = onLoginClick,
                    isLoading = isLoading,
                    backgroundColor = PrimaryRed
                )

                errorMessage?.let { msg ->
                    Spacer(modifier = Modifier.height(16.dp))
                    AuthErrorMessage(message = msg)
                }
            }
        }
    }
}

@Composable
private fun LoginFooter(
    animate: Boolean,
    onRegisterClick: () -> Unit
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
            text = "Don't have an account?",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.width(4.dp))
        TextButton(
            onClick = onRegisterClick,
            colors = ButtonDefaults.textButtonColors(contentColor = SecondaryBlue)
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit,
    onRegistered: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    var animateContent by remember { mutableStateOf(false) }
    var roleMenuExpanded by remember { mutableStateOf(false) }
    var bloodGroupMenuExpanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var previousLoadingState by remember { mutableStateOf(false) }

    // Detect successful registration
    LaunchedEffect(uiState.isLoading, uiState.phoneNumber, uiState.fullName) {
        if (previousLoadingState && !uiState.isLoading) {
            if (uiState.phoneNumber.isEmpty() &&
                uiState.fullName.isEmpty() &&
                uiState.errorMessage == null
            ) {
                showSuccessDialog = true
            }
        }
        previousLoadingState = uiState.isLoading
    }

    LaunchedEffect(Unit) {
        delay(100)
        animateContent = true
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
                uiState = uiState,
                viewModel = viewModel,
                passwordVisible = passwordVisible,
                onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
                roleMenuExpanded = roleMenuExpanded,
                onRoleMenuExpandedChange = { roleMenuExpanded = it },
                bloodGroupMenuExpanded = bloodGroupMenuExpanded,
                onBloodGroupMenuExpandedChange = { bloodGroupMenuExpanded = it },
                onShowDatePicker = { showDatePicker = true }
            )

            Spacer(modifier = Modifier.height(24.dp))

            RegisterFooter(
                animate = animateContent,
                onLoginClick = onLoginClick
            )
        }

        // Date Picker Dialog
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val instant = Instant.ofEpochMilli(millis)
                                val date = instant.atZone(ZoneId.systemDefault()).toLocalDate()
                                viewModel.updateLastDonationDate(date)
                            }
                            showDatePicker = false
                        }
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // Success Dialog
        if (showSuccessDialog) {
            SuccessDialog(
                onDismiss = {
                    showSuccessDialog = false
                    onRegistered()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterCard(
    visible: Boolean,
    uiState: com.tofiq.blood.auth.AuthUiState,
    viewModel: AuthViewModel,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    roleMenuExpanded: Boolean,
    onRoleMenuExpandedChange: (Boolean) -> Unit,
    bloodGroupMenuExpanded: Boolean,
    onBloodGroupMenuExpandedChange: (Boolean) -> Unit,
    onShowDatePicker: () -> Unit
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PhoneNumberField(
                    value = uiState.phoneNumber,
                    onValueChange = viewModel::updatePhoneNumber,
                    placeholder = "+1234567890"
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordField(
                    value = uiState.password,
                    onValueChange = viewModel::updatePassword,
                    passwordVisible = passwordVisible,
                    onToggleVisibility = onTogglePasswordVisibility,
                    placeholder = "Minimum 8 characters"
                )

                Spacer(modifier = Modifier.height(16.dp))

                AuthTextField(
                    value = uiState.fullName,
                    onValueChange = viewModel::updateFullName,
                    label = "Full Name",
                    leadingIcon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Role Dropdown
                RoleDropdown(
                    selectedRole = uiState.role,
                    expanded = roleMenuExpanded,
                    onExpandedChange = onRoleMenuExpandedChange,
                    onRoleSelected = viewModel::updateRole
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Blood Group Dropdown
                BloodGroupDropdown(
                    selectedBloodGroup = uiState.bloodGroup,
                    expanded = bloodGroupMenuExpanded,
                    onExpandedChange = onBloodGroupMenuExpandedChange,
                    onBloodGroupSelected = viewModel::updateBloodGroup
                )

                // Last Donation Date (for donors only)
                if (uiState.role == UserRole.DONOR) {
                    Spacer(modifier = Modifier.height(16.dp))
                    LastDonationDateField(
                        date = uiState.lastDonationDate?.toString() ?: "",
                        onClick = onShowDatePicker
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Terms Checkbox
                TermsCheckbox(
                    checked = uiState.agreedToTerms,
                    onCheckedChange = viewModel::updateAgreedToTerms
                )

                Spacer(modifier = Modifier.height(24.dp))

                AuthButton(
                    text = "Create Account",
                    onClick = { viewModel.registerUser { } },
                    isLoading = uiState.isLoading,
                    backgroundColor = SecondaryBlue
                )

                uiState.errorMessage?.let { msg ->
                    Spacer(modifier = Modifier.height(16.dp))
                    AuthErrorMessage(message = msg)
                }
            }
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
private fun SuccessDialog(onDismiss: () -> Unit) {
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
