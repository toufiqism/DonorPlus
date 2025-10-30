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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.tofiq.blood.auth.AuthViewModel
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

    // Trigger animations on composition
    LaunchedEffect(Unit) {
        delay(100)
        animateContent = true
    }

    // Animated values
    val logoScale by animateFloatAsState(
        targetValue = if (animateContent) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logoScale"
    )

    val logoRotation by animateFloatAsState(
        targetValue = if (animateContent) 0f else 180f,
        animationSpec = tween(durationMillis = 1000),
        label = "logoRotation"
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (animateContent) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "contentAlpha"
    )

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
            // Logo/Icon with animation
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(logoScale)
                    .rotate(logoRotation)
                    .shadow(8.dp, CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(PrimaryRed, AccentCoral)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "DonorPlus Logo",
                    modifier = Modifier.size(64.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Welcome text with fade-in animation
            Column(
                modifier = Modifier.alpha(contentAlpha),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome Back!",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    ),
                    color = PrimaryRed,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Sign in to save lives",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Login Card with slide-in animation
            AnimatedVisibility(
                visible = animateContent,
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
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Phone Number Field
                        OutlinedTextField(
                            value = uiState.phoneNumber,
                            onValueChange = viewModel::updatePhoneNumber,
                            label = { Text("Phone Number") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "Phone",
                                    tint = SecondaryBlue
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SecondaryBlue,
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedLabelColor = SecondaryBlue
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Password Field
                        OutlinedTextField(
                            value = uiState.password,
                            onValueChange = viewModel::updatePassword,
                            label = { Text("Password") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Password",
                                    tint = SecondaryBlue
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible)
                                            Icons.Default.Visibility
                                        else
                                            Icons.Default.VisibilityOff,
                                        contentDescription = if (passwordVisible)
                                            "Hide password"
                                        else
                                            "Show password",
                                        tint = TextSecondary
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SecondaryBlue,
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedLabelColor = SecondaryBlue
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Login Button
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(48.dp),
                                color = PrimaryRed
                            )
                        } else {
                            Button(
                                onClick = { viewModel.loginWithPhone(onLoggedIn) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .shadow(8.dp, RoundedCornerShape(28.dp)),
                                shape = RoundedCornerShape(28.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryRed
                                )
                            ) {
                                Text(
                                    text = "Sign In",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                )
                            }
                        }

                        // Error message
                        uiState.errorMessage?.let { msg ->
                            Spacer(modifier = Modifier.height(16.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(
                                        alpha = 0.1f
                                    )
                                )
                            ) {
                                Text(
                                    text = msg,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(12.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Register link with fade-in
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
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = SecondaryBlue
                    )
                ) {
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // Settings Button (Top Right) - Rendered on top
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
    
    // Track previous loading state to detect successful registration
    var previousLoadingState by remember { mutableStateOf(false) }
    
    // Detect successful registration (when loading finishes and form is cleared)
    LaunchedEffect(uiState.isLoading, uiState.phoneNumber, uiState.fullName) {
        if (previousLoadingState && !uiState.isLoading) {
            // Loading finished - check if registration was successful
            // Success is indicated by cleared form fields
            if (uiState.phoneNumber.isEmpty() && 
                uiState.fullName.isEmpty() && 
                uiState.errorMessage == null) {
                showSuccessDialog = true
            }
        }
        previousLoadingState = uiState.isLoading
    }

    // Trigger animations on composition
    LaunchedEffect(Unit) {
        delay(100)
        animateContent = true
    }

    // Animated values
    val logoScale by animateFloatAsState(
        targetValue = if (animateContent) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logoScale"
    )

    val logoRotation by animateFloatAsState(
        targetValue = if (animateContent) 0f else -180f,
        animationSpec = tween(durationMillis = 1000),
        label = "logoRotation"
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (animateContent) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "contentAlpha"
    )

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
            // Logo/Icon with animation
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(logoScale)
                    .rotate(logoRotation)
                    .shadow(8.dp, CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(SecondaryBlue, AccentCoral)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "DonorPlus Logo",
                    modifier = Modifier.size(64.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Welcome text with fade-in animation
            Column(
                modifier = Modifier.alpha(contentAlpha),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Join Us!",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    ),
                    color = SecondaryBlue,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Create your account to become a hero",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Register Card with slide-in animation
            AnimatedVisibility(
                visible = animateContent,
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
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Phone Number Field
                        OutlinedTextField(
                            value = uiState.phoneNumber,
                            onValueChange = viewModel::updatePhoneNumber,
                            label = { Text("Phone Number") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "Phone",
                                    tint = SecondaryBlue
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SecondaryBlue,
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedLabelColor = SecondaryBlue
                            ),
                            singleLine = true,
                            placeholder = { Text("+1234567890") }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Password Field
                        OutlinedTextField(
                            value = uiState.password,
                            onValueChange = viewModel::updatePassword,
                            label = { Text("Password") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Password",
                                    tint = SecondaryBlue
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible)
                                            Icons.Default.Visibility
                                        else
                                            Icons.Default.VisibilityOff,
                                        contentDescription = if (passwordVisible)
                                            "Hide password"
                                        else
                                            "Show password",
                                        tint = TextSecondary
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SecondaryBlue,
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedLabelColor = SecondaryBlue
                            ),
                            singleLine = true,
                            placeholder = { Text("Minimum 8 characters") }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Full Name Field
                        OutlinedTextField(
                            value = uiState.fullName,
                            onValueChange = viewModel::updateFullName,
                            label = { Text("Full Name") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Full Name",
                                    tint = SecondaryBlue
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SecondaryBlue,
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedLabelColor = SecondaryBlue
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Role Dropdown
                        ExposedDropdownMenuBox(
                            expanded = roleMenuExpanded,
                            onExpandedChange = { roleMenuExpanded = !roleMenuExpanded }
                        ) {
                            OutlinedTextField(
                                value = uiState.role?.name ?: "",
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
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleMenuExpanded)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SecondaryBlue,
                                    unfocusedBorderColor = Color(0xFFE0E0E0),
                                    focusedLabelColor = SecondaryBlue
                                ),
                                placeholder = { Text("Select Role") }
                            )
                            ExposedDropdownMenu(
                                expanded = roleMenuExpanded,
                                onDismissRequest = { roleMenuExpanded = false }
                            ) {
                                UserRole.values().forEach { role ->
                                    DropdownMenuItem(
                                        text = { Text(role.name) },
                                        onClick = {
                                            viewModel.updateRole(role)
                                            roleMenuExpanded = false
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Blood Group Dropdown
                        ExposedDropdownMenuBox(
                            expanded = bloodGroupMenuExpanded,
                            onExpandedChange = { bloodGroupMenuExpanded = !bloodGroupMenuExpanded }
                        ) {
                            OutlinedTextField(
                                value = uiState.bloodGroup?.let {
                                    it.name.replace("_", " ")
                                } ?: "",
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
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = bloodGroupMenuExpanded)
                                },
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
                                expanded = bloodGroupMenuExpanded,
                                onDismissRequest = { bloodGroupMenuExpanded = false }
                            ) {
                                BloodGroup.values().forEach { bloodGroup ->
                                    DropdownMenuItem(
                                        text = { Text(bloodGroup.name.replace("_", " ")) },
                                        onClick = {
                                            viewModel.updateBloodGroup(bloodGroup)
                                            bloodGroupMenuExpanded = false
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Last Donation Date (Optional - only for donors)
                        if (uiState.role == UserRole.DONOR) {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = uiState.lastDonationDate?.toString() ?: "",
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
                                // Invisible clickable overlay
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .clickable { showDatePicker = true }
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Terms and Conditions Checkbox
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = uiState.agreedToTerms,
                                onCheckedChange = viewModel::updateAgreedToTerms,
                                colors = androidx.compose.material3.CheckboxDefaults.colors(
                                    checkedColor = SecondaryBlue
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "I agree to the terms and conditions *",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        viewModel.updateAgreedToTerms(!uiState.agreedToTerms)
                                    }
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Register Button
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(48.dp),
                                color = SecondaryBlue
                            )
                        } else {
                            Button(
                                onClick = {
                                    viewModel.registerUser { /* Don't navigate immediately - wait for dialog */ }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .shadow(8.dp, RoundedCornerShape(28.dp)),
                                shape = RoundedCornerShape(28.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SecondaryBlue
                                )
                            ) {
                                Text(
                                    text = "Create Account",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                )
                            }
                        }

                        // Error message
                        uiState.errorMessage?.let { msg ->
                            Spacer(modifier = Modifier.height(16.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(
                                        alpha = 0.1f
                                    )
                                )
                            ) {
                                Text(
                                    text = msg,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(12.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login link with fade-in
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
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = PrimaryRed
                    )
                ) {
                    Text(
                        text = "Sign In",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
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
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
        
        // Success Dialog
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { 
                    // Dialog is not dismissible by clicking outside
                    // User must click OK
                },
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
                        onClick = {
                            showSuccessDialog = false
                            onRegistered() // Navigate to login screen
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SecondaryBlue
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("OK")
                    }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}


