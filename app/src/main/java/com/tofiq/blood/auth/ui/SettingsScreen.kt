package com.tofiq.blood.auth.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tofiq.blood.auth.SettingsViewModel
import com.tofiq.blood.auth.ui.components.authOutlinedTextFieldColors
import com.tofiq.blood.ui.components.DonorPlusCard
import com.tofiq.blood.ui.components.DonorPlusErrorMessage
import com.tofiq.blood.ui.components.DonorPlusGradientBackground
import com.tofiq.blood.ui.components.DonorPlusHeader
import com.tofiq.blood.ui.components.DonorPlusInfoMessage
import com.tofiq.blood.ui.components.DonorPlusSecondaryButton
import com.tofiq.blood.ui.components.DonorPlusSpacing
import com.tofiq.blood.ui.components.DonorPlusSuccessMessage
import com.tofiq.blood.ui.components.DonorPlusTextField
import com.tofiq.blood.ui.components.DonorPlusTextButton
import com.tofiq.blood.ui.theme.AccentCoral
import com.tofiq.blood.ui.theme.PrimaryRed
import com.tofiq.blood.ui.theme.SecondaryBlue
import com.tofiq.blood.ui.theme.TextSecondary
import kotlinx.coroutines.delay

/**
 * Settings screen for configuring base URL
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Auto-hide success message after 3 seconds
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            delay(3000)
            viewModel.clearSavedMessage()
        }
    }

    Scaffold(
        topBar = {
            SettingsTopBar(onNavigateBack = onNavigateBack)
        }
    ) { paddingValues ->
        SettingsContent(
            baseUrl = uiState.baseUrl,
            onBaseUrlChange = viewModel::updateBaseUrl,
            onSave = viewModel::saveBaseUrl,
            onReset = viewModel::resetToDefault,
            isSaved = uiState.isSaved,
            errorMessage = uiState.errorMessage,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopBar(
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PrimaryRed,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

@Composable
private fun SettingsContent(
    baseUrl: String,
    onBaseUrlChange: (String) -> Unit,
    onSave: () -> Unit,
    onReset: () -> Unit,
    isSaved: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    DonorPlusGradientBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(DonorPlusSpacing.L)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SettingsIcon()
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.L))
            
            DonorPlusHeader(
                title = "API Configuration",
                subtitle = "Configure the backend server URL",
                titleColor = PrimaryRed,
                animate = false
            )
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.XL))
            
            SettingsCard(
                baseUrl = baseUrl,
                onBaseUrlChange = onBaseUrlChange,
                onSave = onSave,
                onReset = onReset,
                isSaved = isSaved,
                errorMessage = errorMessage
            )
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.L))
            
            SettingsInfoCard()
        }
    }
}

@Composable
private fun SettingsIcon() {
    Card(
        modifier = Modifier
            .size(80.dp)
            .shadow(8.dp, RoundedCornerShape(40.dp)),
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(containerColor = SecondaryBlue)
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings",
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            tint = Color.White
        )
    }
}

@Composable
private fun SettingsCard(
    baseUrl: String,
    onBaseUrlChange: (String) -> Unit,
    onSave: () -> Unit,
    onReset: () -> Unit,
    isSaved: Boolean,
    errorMessage: String?
) {
    DonorPlusCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DonorPlusSpacing.L),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BaseUrlField(
                value = baseUrl,
                onValueChange = onBaseUrlChange
            )
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.L))
            
            DonorPlusSecondaryButton(
                onClick = onSave,
                text = "Save Configuration",
                icon = Icons.Default.Check
            )
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.S))
            
            DonorPlusTextButton(
                onClick = onReset,
                text = "Reset to Default",
                color = PrimaryRed
            )
            
            SuccessMessage(visible = isSaved)
            
            errorMessage?.let { msg ->
                Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                DonorPlusErrorMessage(message = msg)
            }
        }
    }
}

@Composable
private fun BaseUrlField(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        DonorPlusTextField(
            value = value,
            onValueChange = onValueChange,
            label = "Base URL"
        )
        Spacer(modifier = Modifier.height(DonorPlusSpacing.XS))
        Text(
            text = "Format: http://ip:port or https://domain.com",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            modifier = Modifier.padding(start = DonorPlusSpacing.M)
        )
    }
}

@Composable
private fun SuccessMessage(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
        DonorPlusSuccessMessage(message = "✓ Configuration saved successfully")
    }
}

@Composable
private fun SettingsInfoCard() {
    DonorPlusInfoMessage(
        message = "ℹ️ Information\n\n" +
                "• The base URL should point to your backend server\n" +
                "• Include the protocol (http:// or https://)\n" +
                "• Include the port number if required\n" +
                "• Changes take effect immediately",
        backgroundColor = AccentCoral.copy(alpha = 0.1f),
        textColor = TextSecondary
    )
}
