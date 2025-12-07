package com.tofiq.blood.auth.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tofiq.blood.data.model.BloodGroup
import com.tofiq.blood.data.model.UserRole
import com.tofiq.blood.ui.theme.AccentCoral
import com.tofiq.blood.ui.theme.PrimaryRed
import com.tofiq.blood.ui.theme.SecondaryBlue
import com.tofiq.blood.ui.theme.TextSecondary
import java.time.LocalDate

// ============== Immutable Data Classes ==============

@Immutable
data class LoginFormData(
    val phoneNumber: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@Immutable
data class RegisterFormData(
    val phoneNumber: String = "",
    val password: String = "",
    val fullName: String = "",
    val role: UserRole? = null,
    val bloodGroup: BloodGroup? = null,
    val lastDonationDate: LocalDate? = null,
    val agreedToTerms: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

// ============== Stable State Holders ==============

@Stable
class LoginScreenState {
    var passwordVisible by mutableStateOf(false)
        private set
    var animateContent by mutableStateOf(false)
        private set

    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }

    fun startAnimation() {
        animateContent = true
    }
}

@Stable
class RegisterScreenState {
    var passwordVisible by mutableStateOf(false)
        private set
    var animateContent by mutableStateOf(false)
        private set
    var roleMenuExpanded by mutableStateOf(false)
        private set
    var bloodGroupMenuExpanded by mutableStateOf(false)
        private set
    var showDatePicker by mutableStateOf(false)
        private set
    var showSuccessDialog by mutableStateOf(false)
        private set

    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }

    fun startAnimation() {
        animateContent = true
    }

    fun updateRoleMenuExpanded(expanded: Boolean) {
        roleMenuExpanded = expanded
    }

    fun updateBloodGroupMenuExpanded(expanded: Boolean) {
        bloodGroupMenuExpanded = expanded
    }

    fun updateShowDatePicker(show: Boolean) {
        showDatePicker = show
    }

    fun updateShowSuccessDialog(show: Boolean) {
        showSuccessDialog = show
    }
}

/**
 * Animated logo component for auth screens
 */
@Composable
fun AuthLogo(
    animate: Boolean,
    primaryColor: Color = PrimaryRed,
    secondaryColor: Color = AccentCoral,
    rotationDirection: Float = 180f,
    modifier: Modifier = Modifier
) {
    val logoScale by animateFloatAsState(
        targetValue = if (animate) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logoScale"
    )

    val logoRotation by animateFloatAsState(
        targetValue = if (animate) 0f else rotationDirection,
        animationSpec = tween(durationMillis = 1000),
        label = "logoRotation"
    )

    Box(
        modifier = modifier
            .size(120.dp)
            .scale(logoScale)
            .rotate(logoRotation)
            .shadow(8.dp, CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(primaryColor, secondaryColor)
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
}

/**
 * Auth header text with animation
 */
@Composable
fun AuthHeader(
    title: String,
    subtitle: String,
    titleColor: Color,
    animate: Boolean,
    modifier: Modifier = Modifier
) {
    val contentAlpha by animateFloatAsState(
        targetValue = if (animate) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "contentAlpha"
    )

    Column(
        modifier = modifier.alpha(contentAlpha),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = titleColor,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Phone number input field
 */
@Composable
fun PhoneNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Phone Number") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "Phone",
                tint = SecondaryBlue
            )
        },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SecondaryBlue,
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedLabelColor = SecondaryBlue
        ),
        singleLine = true,
        placeholder = placeholder?.let { { Text(it) } }
    )
}

/**
 * Password input field with visibility toggle
 */
@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    passwordVisible: Boolean,
    onToggleVisibility: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Password") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Password",
                tint = SecondaryBlue
            )
        },
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    tint = TextSecondary
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SecondaryBlue,
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedLabelColor = SecondaryBlue
        ),
        singleLine = true,
        placeholder = placeholder?.let { { Text(it) } }
    )
}

/**
 * Auth action button (Sign In / Create Account)
 */
@Composable
fun AuthButton(
    text: String,
    onClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    backgroundColor: Color = PrimaryRed
) {
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = backgroundColor
        )
    } else {
        Button(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(8.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

/**
 * Error message card
 */
@Composable
fun AuthErrorMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
        )
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(12.dp),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Generic text input field for auth forms
 */
@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = label,
                tint = SecondaryBlue
            )
        },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SecondaryBlue,
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedLabelColor = SecondaryBlue,
            disabledBorderColor = Color(0xFFE0E0E0),
            disabledTextColor = Color.Black,
            disabledLabelColor = SecondaryBlue
        ),
        singleLine = true,
        enabled = enabled,
        readOnly = readOnly,
        placeholder = placeholder?.let { { Text(it) } }
    )
}
