package com.tofiq.blood.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tofiq.blood.auth.AuthViewModel
import com.tofiq.blood.auth.ui.components.AuthAnimatedCard
import com.tofiq.blood.auth.ui.components.AuthButton
import com.tofiq.blood.auth.ui.components.AuthFooter
import com.tofiq.blood.auth.ui.components.AuthHeader
import com.tofiq.blood.auth.ui.components.AuthLogo
import com.tofiq.blood.auth.ui.components.LoginScreenState
import com.tofiq.blood.auth.ui.components.PasswordField
import com.tofiq.blood.auth.ui.components.PhoneNumberField
import com.tofiq.blood.ui.components.DonorPlusGradientBackground
import com.tofiq.blood.ui.theme.AccentCoral
import com.tofiq.blood.ui.theme.PrimaryRed
import com.tofiq.blood.ui.theme.SecondaryBlue
import kotlinx.coroutines.delay

/**
 * Login screen for user authentication
 */
@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    onLoggedIn: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val screenState = remember { LoginScreenState() }
    val snackbarHostState = remember { SnackbarHostState() }

    // Clear form when user arrives at login screen (e.g., after logout)
    // This ensures previous credentials are not shown
    LaunchedEffect(Unit) {
        viewModel.clearLoginForm()
        delay(100)
        screenState.startAnimation()
    }

    // Show error message in Snackbar when errorMessage changes
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    ) { paddingValues ->
        DonorPlusGradientBackground(
            modifier = Modifier.padding(paddingValues)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LoginContent(
                    animateContent = screenState.animateContent,
                    phoneNumber = uiState.phoneNumber,
                    onPhoneNumberChange = viewModel::updatePhoneNumber,
                    password = uiState.password,
                    onPasswordChange = viewModel::updatePassword,
                    passwordVisible = screenState.passwordVisible,
                    onTogglePasswordVisibility = screenState::togglePasswordVisibility,
                    isLoading = uiState.isLoading,
                    onLoginClick = { 
                        viewModel.loginWithPhone { 
                            // Explicitly call onLoggedIn when login succeeds
                            onLoggedIn() 
                        } 
                    },
                    onRegisterClick = onRegisterClick
                )

                SettingsButton(
                    onClick = onSettingsClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun LoginContent(
    animateContent: Boolean,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    isLoading: Boolean,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
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
            phoneNumber = phoneNumber,
            onPhoneNumberChange = onPhoneNumberChange,
            password = password,
            onPasswordChange = onPasswordChange,
            passwordVisible = passwordVisible,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            isLoading = isLoading,
            onLoginClick = onLoginClick
        )

        Spacer(modifier = Modifier.height(24.dp))

        LoginFooter(
            animate = animateContent,
            onRegisterClick = onRegisterClick
        )
    }
}

@Composable
private fun SettingsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
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
    onLoginClick: () -> Unit
) {
    AuthAnimatedCard(visible = visible) {
        LoginCardContent(
            phoneNumber = phoneNumber,
            onPhoneNumberChange = onPhoneNumberChange,
            password = password,
            onPasswordChange = onPasswordChange,
            passwordVisible = passwordVisible,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            isLoading = isLoading,
            onLoginClick = onLoginClick
        )
    }
}

@Composable
private fun LoginCardContent(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    isLoading: Boolean,
    onLoginClick: () -> Unit
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
    }
}

@Composable
private fun LoginFooter(
    animate: Boolean,
    onRegisterClick: () -> Unit
) {
    AuthFooter(
        promptText = "Don't have an account?",
        actionText = "Sign Up",
        onActionClick = onRegisterClick,
        actionColor = SecondaryBlue,
        animate = animate
    )
}
