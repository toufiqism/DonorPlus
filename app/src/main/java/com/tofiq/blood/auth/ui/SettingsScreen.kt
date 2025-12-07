package com.tofiq.blood.auth.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tofiq.blood.auth.SettingsViewModel
import com.tofiq.blood.ui.theme.AccentCoral
import com.tofiq.blood.ui.theme.GradientEnd
import com.tofiq.blood.ui.theme.GradientMiddle
import com.tofiq.blood.ui.theme.GradientStart
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(GradientStart, GradientMiddle, GradientEnd),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            )
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SettingsIcon()
        
        Spacer(modifier = Modifier.height(24.dp))
        
        SettingsHeader()
        
        Spacer(modifier = Modifier.height(32.dp))
        
        SettingsCard(
            baseUrl = baseUrl,
            onBaseUrlChange = onBaseUrlChange,
            onSave = onSave,
            onReset = onReset,
            isSaved = isSaved,
            errorMessage = errorMessage
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        SettingsInfoCard()
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
private fun SettingsHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "API Configuration",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = PrimaryRed
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Configure the backend server URL",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BaseUrlField(
                value = baseUrl,
                onValueChange = onBaseUrlChange
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            SaveButton(onClick = onSave)
            
            Spacer(modifier = Modifier.height(12.dp))
            
            ResetButton(onClick = onReset)
            
            SuccessMessage(visible = isSaved)
            
            errorMessage?.let { msg ->
                Spacer(modifier = Modifier.height(16.dp))
                ErrorMessage(message = msg)
            }
        }
    }
}

@Composable
private fun BaseUrlField(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Base URL") },
        placeholder = { Text("http://192.168.103.177:8080") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SecondaryBlue,
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedLabelColor = SecondaryBlue
        ),
        singleLine = true,
        supportingText = {
            Text(
                text = "Format: http://ip:port or https://domain.com",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    )
}

@Composable
private fun SaveButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(8.dp, RoundedCornerShape(28.dp)),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(containerColor = SecondaryBlue)
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Save Configuration",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun ResetButton(onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(contentColor = PrimaryRed)
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Reset to Default",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
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
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
            )
        ) {
            Text(
                text = "✓ Configuration saved successfully",
                color = Color(0xFF2E7D32),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ErrorMessage(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
        )
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SettingsInfoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AccentCoral.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "ℹ️ Information",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = PrimaryRed
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "• The base URL should point to your backend server\n" +
                        "• Include the protocol (http:// or https://)\n" +
                        "• Include the port number if required\n" +
                        "• Changes take effect immediately",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}
