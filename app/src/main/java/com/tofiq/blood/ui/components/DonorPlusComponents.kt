package com.tofiq.blood.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tofiq.blood.ui.theme.AccentCoral
import com.tofiq.blood.ui.theme.GradientEnd
import com.tofiq.blood.ui.theme.GradientMiddle
import com.tofiq.blood.ui.theme.GradientStart
import com.tofiq.blood.ui.theme.PrimaryRed
import com.tofiq.blood.ui.theme.SecondaryBlue
import com.tofiq.blood.ui.theme.TextSecondary
import kotlinx.coroutines.delay

/**
 * DonorPlus Design System Components
 * 
 * This file contains reusable UI components that maintain consistent styling
 * across the entire app. Use these components for all new features.
 */

// ==================== Backgrounds ====================

/**
 * Standard gradient background used throughout the app.
 * @param modifier Optional modifier
 * @param reversed If true, reverses the gradient direction
 * @param content Content to display on the background
 */
@Composable
fun DonorPlusGradientBackground(
    modifier: Modifier = Modifier,
    reversed: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (reversed) {
        listOf(GradientEnd, GradientMiddle, GradientStart)
    } else {
        listOf(GradientStart, GradientMiddle, GradientEnd)
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = colors,
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            )
    ) {
        content()
    }
}

/**
 * Solid background with standard light color
 */
@Composable
fun DonorPlusSolidBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        content()
    }
}

// ==================== Animated Logo ====================

/**
 * Animated heart logo used on auth screens and splash screens.
 * @param modifier Optional modifier
 * @param size Size of the logo container
 * @param iconSize Size of the heart icon
 * @param primaryColor Primary gradient color
 * @param secondaryColor Secondary gradient color
 * @param animate If true, plays entrance animation
 */
@Composable
fun DonorPlusAnimatedLogo(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    iconSize: Dp = 64.dp,
    primaryColor: Color = PrimaryRed,
    secondaryColor: Color = AccentCoral,
    animate: Boolean = true,
    rotationDirection: Float = 180f
) {
    var animateContent by remember { mutableStateOf(!animate) }
    
    if (animate) {
        LaunchedEffect(Unit) {
            delay(100)
            animateContent = true
        }
    }
    
    val logoScale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (animateContent) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logoScale"
    )
    
    val logoRotation by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (animateContent) 0f else rotationDirection,
        animationSpec = tween(durationMillis = 1000),
        label = "logoRotation"
    )
    
    Box(
        modifier = modifier
            .size(size)
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
            modifier = Modifier.size(iconSize),
            tint = Color.White
        )
    }
}

// ==================== Cards ====================

/**
 * Standard elevated card with rounded corners.
 * @param modifier Optional modifier
 * @param backgroundColor Card background color
 * @param elevation Card elevation
 * @param cornerRadius Corner radius
 * @param content Card content
 */
@Composable
fun DonorPlusCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    elevation: Dp = 16.dp,
    cornerRadius: Dp = 24.dp,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.shadow(elevation, RoundedCornerShape(cornerRadius)),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        content()
    }
}

/**
 * Animated card that slides in from bottom.
 * @param visible Controls visibility and animation
 * @param modifier Optional modifier
 * @param content Card content
 */
@Composable
fun DonorPlusAnimatedCard(
    visible: Boolean,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    content: @Composable () -> Unit
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
        DonorPlusCard(
            modifier = modifier,
            backgroundColor = backgroundColor
        ) {
            content()
        }
    }
}

// ==================== Buttons ====================

/**
 * Primary button with red background (main actions).
 * @param onClick Click handler
 * @param modifier Optional modifier
 * @param text Button text
 * @param enabled Whether button is enabled
 */
@Composable
fun DonorPlusPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(8.dp, RoundedCornerShape(28.dp)),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryRed,
            disabledContainerColor = PrimaryRed.copy(alpha = 0.38f)
        ),
        enabled = enabled
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )
    }
}

/**
 * Secondary button with blue background.
 * @param onClick Click handler
 * @param modifier Optional modifier
 * @param text Button text
 * @param enabled Whether button is enabled
 */
@Composable
fun DonorPlusSecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(8.dp, RoundedCornerShape(28.dp)),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = SecondaryBlue,
            disabledContainerColor = SecondaryBlue.copy(alpha = 0.38f)
        ),
        enabled = enabled
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )
    }
}

/**
 * Outlined button with transparent background.
 * @param onClick Click handler
 * @param modifier Optional modifier
 * @param text Button text
 * @param borderColor Color of the border
 */
@Composable
fun DonorPlusOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    borderColor: Color = PrimaryRed,
    textColor: Color = PrimaryRed,
    icon: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = textColor
        ),
        border = androidx.compose.foundation.BorderStroke(2.dp, borderColor)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )
    }
}

/**
 * Text button for secondary actions.
 * @param onClick Click handler
 * @param text Button text
 * @param color Text color
 */
@Composable
fun DonorPlusTextButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    color: Color = SecondaryBlue
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(contentColor = color)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}

// ==================== Text Fields ====================

/**
 * Standard text field with icon support.
 * @param value Current value
 * @param onValueChange Value change callback
 * @param label Field label
 * @param modifier Optional modifier
 * @param leadingIcon Leading icon
 * @param trailingIcon Trailing icon content
 * @param visualTransformation Visual transformation (for passwords)
 * @param singleLine Whether field is single line
 */
@Composable
fun DonorPlusTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = label,
                    tint = SecondaryBlue
                )
            }
        },
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SecondaryBlue,
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedLabelColor = SecondaryBlue,
            disabledBorderColor = Color(0xFFE0E0E0).copy(alpha = 0.38f)
        ),
        singleLine = singleLine,
        enabled = enabled
    )
}

// ==================== Headers ====================

/**
 * Large header with fade-in animation.
 * @param title Main title text
 * @param subtitle Optional subtitle text
 * @param modifier Optional modifier
 * @param animate Whether to animate
 * @param titleColor Color of the title
 */
@Composable
fun DonorPlusHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    animate: Boolean = true,
    titleColor: Color = PrimaryRed
) {
    var animateContent by remember { mutableStateOf(!animate) }
    
    if (animate) {
        LaunchedEffect(Unit) {
            delay(100)
            animateContent = true
        }
    }
    
    val contentAlpha by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (animateContent) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "contentAlpha"
    )
    
    Column(
        modifier = modifier.alpha(contentAlpha),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            ),
            color = titleColor,
            textAlign = TextAlign.Center
        )
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Section header for content sections.
 * @param text Header text
 * @param modifier Optional modifier
 */
@Composable
fun DonorPlusSectionHeader(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        ),
        color = color,
        modifier = modifier
    )
}

// ==================== Error/Info Messages ====================

/**
 * Error message card.
 * @param message Error message text
 * @param modifier Optional modifier
 */
@Composable
fun DonorPlusErrorMessage(
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
 * Info message card.
 * @param message Info message text
 * @param modifier Optional modifier
 */
@Composable
fun DonorPlusInfoMessage(
    message: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = SecondaryBlue.copy(alpha = 0.1f),
    textColor: Color = SecondaryBlue
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Text(
            text = message,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(12.dp),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Success message card.
 * @param message Success message text
 * @param modifier Optional modifier
 */
@Composable
fun DonorPlusSuccessMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
        )
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(12.dp),
            textAlign = TextAlign.Center
        )
    }
}

// ==================== Loading States ====================

/**
 * Standard loading indicator.
 * @param modifier Optional modifier
 */
@Composable
fun DonorPlusLoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = PrimaryRed
) {
    androidx.compose.material3.CircularProgressIndicator(
        modifier = modifier.size(48.dp),
        color = color
    )
}

// ==================== Spacers ====================

/**
 * Standard spacing values for consistency.
 */
object DonorPlusSpacing {
    val XS = 4.dp
    val S = 8.dp
    val M = 16.dp
    val L = 24.dp
    val XL = 40.dp
}

// ==================== Surface with Standard Padding ====================

/**
 * Standard content container with padding.
 * @param modifier Optional modifier
 * @param padding Content padding
 * @param content Content to display
 */
@Composable
fun DonorPlusContentContainer(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(DonorPlusSpacing.L),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        content()
    }
}

