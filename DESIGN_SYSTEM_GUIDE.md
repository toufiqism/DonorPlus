# DonorPlus Design System Guide

## Overview
This guide provides comprehensive documentation for the DonorPlus design system. Follow these guidelines when building new features to maintain consistent styling across the entire app.

---

## Table of Contents
1. [Core Principles](#core-principles)
2. [Color Palette](#color-palette)
3. [Typography](#typography)
4. [Spacing System](#spacing-system)
5. [Component Library](#component-library)
6. [Layout Patterns](#layout-patterns)
7. [Animation Guidelines](#animation-guidelines)
8. [Code Examples](#code-examples)
9. [Best Practices](#best-practices)

---

## Core Principles

### 1. Consistency
- Use predefined components from `DonorPlusComponents.kt`
- Stick to the established color palette
- Follow spacing guidelines
- Maintain animation timing standards

### 2. Accessibility
- Minimum touch target: 48dp
- Color contrast ratio: AA or better
- Screen reader support for all interactive elements
- Proper content descriptions for icons

### 3. Performance
- Use remember for state
- Minimize recomposition
- Hardware acceleration for animations
- Efficient gradient rendering

### 4. Responsiveness
- Support all screen sizes
- Scrollable content where needed
- Proper padding and margins
- Flexible layouts

---

## Color Palette

### Primary Colors
```kotlin
// Import from theme
import com.tofiq.blood.ui.theme.*

PrimaryRed      = Color(0xFFE63946)  // Main actions, branding
SecondaryBlue   = Color(0xFF457B9D)  // Secondary actions, trust
AccentCoral     = Color(0xFFFF6B6B)  // Highlights, accents
```

### Background Colors
```kotlin
BackgroundLight = Color(0xFFF8F9FA)  // Screen backgrounds
SurfaceWhite    = Color(0xFFFFFFFF)  // Card backgrounds
```

### Text Colors
```kotlin
TextPrimary     = Color(0xFF2B2D42)  // Main text
TextSecondary   = Color(0xFF8D99AE)  // Secondary text, hints
```

### Status Colors
```kotlin
ErrorRed        = Color(0xFFDC2F02)  // Errors
SuccessGreen    = Color(0xFF06D6A0)  // Success states
```

### Gradient Colors
```kotlin
GradientStart   = Color(0xFFFFCDD2)  // Light Pink
GradientMiddle  = Color(0xFFE3F2FD)  // Light Blue
GradientEnd     = Color(0xFFF1F8E9)  // Light Green
```

### Usage Guidelines

**Primary Red** - Use for:
- Primary action buttons (Login, Submit, Save)
- Important calls-to-action
- App branding elements
- Error states (with ErrorRed)

**Secondary Blue** - Use for:
- Secondary action buttons
- Form field focus states
- Links and navigation
- Info messages

**Accent Coral** - Use for:
- Highlights and badges
- Gradient accents
- Hover states
- Decorative elements

---

## Typography

### Text Styles
```kotlin
// Large Headers
MaterialTheme.typography.headlineLarge.copy(
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp
)

// Section Headers
MaterialTheme.typography.titleLarge.copy(
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp
)

// Button Text
MaterialTheme.typography.titleMedium.copy(
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp
)

// Body Text
MaterialTheme.typography.bodyLarge    // 16sp
MaterialTheme.typography.bodyMedium   // 14sp
MaterialTheme.typography.bodySmall    // 12sp
```

### Font Weights
- **Bold (700)**: Headers, buttons, important text
- **SemiBold (600)**: Subheaders, emphasis
- **Regular (400)**: Body text, descriptions
- **Light (300)**: Captions, secondary info

---

## Spacing System

### Standard Spacing Values
```kotlin
import com.tofiq.blood.ui.components.DonorPlusSpacing

DonorPlusSpacing.XS  = 4.dp   // Tight spacing
DonorPlusSpacing.S   = 8.dp   // Small spacing
DonorPlusSpacing.M   = 16.dp  // Medium spacing (default)
DonorPlusSpacing.L   = 24.dp  // Large spacing (content padding)
DonorPlusSpacing.XL  = 40.dp  // Extra large spacing (sections)
```

### Usage Examples
```kotlin
// Between related elements
Spacer(modifier = Modifier.height(DonorPlusSpacing.S))

// Between form fields
Spacer(modifier = Modifier.height(DonorPlusSpacing.M))

// Between sections
Spacer(modifier = Modifier.height(DonorPlusSpacing.XL))

// Content padding
Modifier.padding(DonorPlusSpacing.L)
```

---

## Component Library

### Backgrounds

#### 1. Gradient Background
```kotlin
DonorPlusGradientBackground(
    reversed = false  // Set true for reversed gradient
) {
    // Your content here
}
```

**When to use:**
- Auth screens (Login, Register)
- Welcome/Onboarding screens
- Special landing pages

#### 2. Solid Background
```kotlin
DonorPlusSolidBackground {
    // Your content here
}
```

**When to use:**
- Main app screens
- List views
- Detail pages

---

### Logo

#### Animated Logo
```kotlin
DonorPlusAnimatedLogo(
    size = 120.dp,              // Container size
    iconSize = 64.dp,           // Icon size
    primaryColor = PrimaryRed,   // Gradient start
    secondaryColor = AccentCoral, // Gradient end
    animate = true,              // Enable animation
    rotationDirection = 180f     // Rotation angle
)
```

**When to use:**
- Splash screen
- Auth screens
- About page
- Empty states

---

### Cards

#### 1. Standard Card
```kotlin
DonorPlusCard(
    backgroundColor = Color.White,
    elevation = 16.dp,
    cornerRadius = 24.dp
) {
    // Card content
    Column(modifier = Modifier.padding(24.dp)) {
        Text("Card Content")
    }
}
```

**When to use:**
- Content containers
- Form containers
- List items
- Detail sections

#### 2. Animated Card
```kotlin
var showCard by remember { mutableStateOf(false) }

LaunchedEffect(Unit) {
    delay(100)
    showCard = true
}

DonorPlusAnimatedCard(
    visible = showCard,
    backgroundColor = Color.White
) {
    // Card content
}
```

**When to use:**
- Initial screen load
- Dynamic content reveal
- Modal dialogs

---

### Buttons

#### 1. Primary Button (Red)
```kotlin
DonorPlusPrimaryButton(
    onClick = { /* Action */ },
    text = "Primary Action",
    icon = Icons.Default.Check  // Optional
)
```

**When to use:**
- Main actions (Login, Submit, Save)
- Confirmation buttons
- Call-to-action buttons

#### 2. Secondary Button (Blue)
```kotlin
DonorPlusSecondaryButton(
    onClick = { /* Action */ },
    text = "Secondary Action",
    icon = Icons.Default.Info  // Optional
)
```

**When to use:**
- Alternative actions
- Navigation buttons
- Less prominent actions

#### 3. Outlined Button
```kotlin
DonorPlusOutlinedButton(
    onClick = { /* Action */ },
    text = "Outlined Action",
    borderColor = PrimaryRed,
    textColor = PrimaryRed
)
```

**When to use:**
- Cancel actions
- Tertiary actions
- Alternative choices

#### 4. Text Button
```kotlin
DonorPlusTextButton(
    onClick = { /* Action */ },
    text = "Text Action",
    color = SecondaryBlue
)
```

**When to use:**
- Links
- Navigation links
- Inline actions

---

### Text Fields

#### Standard Text Field
```kotlin
var email by remember { mutableStateOf("") }

DonorPlusTextField(
    value = email,
    onValueChange = { email = it },
    label = "Email Address",
    leadingIcon = Icons.Default.Email,
    trailingIcon = {
        // Optional trailing icon
        IconButton(onClick = { /* Action */ }) {
            Icon(Icons.Default.Clear, "Clear")
        }
    },
    visualTransformation = VisualTransformation.None,
    singleLine = true
)
```

**When to use:**
- Form inputs
- Search fields
- User input

---

### Headers

#### 1. Large Header with Subtitle
```kotlin
DonorPlusHeader(
    title = "Welcome Back!",
    subtitle = "Sign in to save lives",
    titleColor = PrimaryRed,
    animate = true
)
```

**When to use:**
- Screen headers
- Section introductions
- Welcome messages

#### 2. Section Header
```kotlin
DonorPlusSectionHeader(
    text = "Recent Donations",
    color = TextPrimary
)
```

**When to use:**
- Content sections
- List categories
- Page subsections

---

### Messages

#### 1. Error Message
```kotlin
DonorPlusErrorMessage(
    message = "Invalid email or password"
)
```

#### 2. Info Message
```kotlin
DonorPlusInfoMessage(
    message = "Your profile has been updated"
)
```

#### 3. Success Message
```kotlin
DonorPlusSuccessMessage(
    message = "Registration successful!"
)
```

---

### Loading States

#### Loading Indicator
```kotlin
DonorPlusLoadingIndicator(
    color = PrimaryRed
)
```

**When to use:**
- API calls
- Data loading
- Processing states

---

## Layout Patterns

### 1. Auth Screen Pattern
```kotlin
@Composable
fun MyAuthScreen() {
    DonorPlusGradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(DonorPlusSpacing.L),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            DonorPlusAnimatedLogo()
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.L))
            
            // Header
            DonorPlusHeader(
                title = "Screen Title",
                subtitle = "Screen subtitle"
            )
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.XL))
            
            // Content Card
            var showCard by remember { mutableStateOf(false) }
            
            LaunchedEffect(Unit) {
                delay(100)
                showCard = true
            }
            
            DonorPlusAnimatedCard(visible = showCard) {
                Column(modifier = Modifier.padding(DonorPlusSpacing.L)) {
                    // Form fields
                }
            }
        }
    }
}
```

### 2. Main Screen Pattern
```kotlin
@Composable
fun MyMainScreen() {
    DonorPlusSolidBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // App Bar or Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(DonorPlusSpacing.L)) {
                    DonorPlusSectionHeader(text = "Page Title")
                }
            }
            
            // Content
            DonorPlusContentContainer {
                // Your content here
            }
        }
    }
}
```

### 3. List Screen Pattern
```kotlin
@Composable
fun MyListScreen() {
    DonorPlusSolidBackground {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(DonorPlusSpacing.M)
        ) {
            item {
                DonorPlusSectionHeader(text = "List Title")
                Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
            }
            
            items(dataList) { item ->
                DonorPlusCard(
                    modifier = Modifier.padding(vertical = DonorPlusSpacing.S)
                ) {
                    // List item content
                }
            }
        }
    }
}
```

### 4. Detail Screen Pattern
```kotlin
@Composable
fun MyDetailScreen() {
    DonorPlusSolidBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(PrimaryRed, AccentCoral)
                        )
                    )
            ) {
                // Hero content
            }
            
            // Detail Content
            DonorPlusContentContainer {
                DonorPlusCard {
                    Column(modifier = Modifier.padding(DonorPlusSpacing.L)) {
                        DonorPlusSectionHeader(text = "Details")
                        Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                        // Detail fields
                    }
                }
            }
        }
    }
}
```

---

## Animation Guidelines

### Timing Standards
- **Fast**: 300ms - Small UI changes
- **Medium**: 500-800ms - Content transitions
- **Slow**: 1000ms+ - Major screen transitions

### Animation Types

#### 1. Entrance Animations
```kotlin
// Fade In
val alpha by animateFloatAsState(
    targetValue = if (visible) 1f else 0f,
    animationSpec = tween(durationMillis = 800)
)

// Slide Up
AnimatedVisibility(
    visible = visible,
    enter = slideInVertically(
        initialOffsetY = { it },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    ) + fadeIn()
)

// Scale + Rotate
val scale by animateFloatAsState(
    targetValue = if (visible) 1f else 0f,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
)
```

#### 2. Transition Animations
```kotlin
// Cross-fade
Crossfade(targetState = currentState) { state ->
    when (state) {
        // Different states
    }
}

// Animated Content
AnimatedContent(targetState = currentState) { state ->
    // Content for state
}
```

### Spring Physics
```kotlin
// Bouncy (for playful elements)
spring(
    dampingRatio = Spring.DampingRatioMediumBouncy,
    stiffness = Spring.StiffnessLow
)

// Smooth (for elegant transitions)
spring(
    dampingRatio = Spring.DampingRatioNoBouncy,
    stiffness = Spring.StiffnessMedium
)
```

---

## Code Examples

### Example 1: Simple Form Screen
```kotlin
@Composable
fun ProfileEditScreen() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    DonorPlusSolidBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(DonorPlusSpacing.L)
        ) {
            DonorPlusSectionHeader(text = "Edit Profile")
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.XL))
            
            DonorPlusCard {
                Column(modifier = Modifier.padding(DonorPlusSpacing.L)) {
                    DonorPlusTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Full Name",
                        leadingIcon = Icons.Default.Person
                    )
                    
                    Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                    
                    DonorPlusTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        leadingIcon = Icons.Default.Email
                    )
                    
                    Spacer(modifier = Modifier.height(DonorPlusSpacing.L))
                    
                    if (isLoading) {
                        DonorPlusLoadingIndicator()
                    } else {
                        DonorPlusPrimaryButton(
                            onClick = { isLoading = true },
                            text = "Save Changes"
                        )
                    }
                }
            }
        }
    }
}
```

### Example 2: Dashboard Screen
```kotlin
@Composable
fun DashboardScreen() {
    DonorPlusSolidBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header Card
            DonorPlusCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DonorPlusSpacing.M),
                backgroundColor = PrimaryRed
            ) {
                Column(modifier = Modifier.padding(DonorPlusSpacing.L)) {
                    Text(
                        text = "Welcome Back, John!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                }
            }
            
            // Stats Section
            Column(modifier = Modifier.padding(DonorPlusSpacing.M)) {
                DonorPlusSectionHeader(text = "Your Impact")
                
                Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(DonorPlusSpacing.M)
                ) {
                    DonorPlusCard(modifier = Modifier.weight(1f)) {
                        // Stat card content
                    }
                    DonorPlusCard(modifier = Modifier.weight(1f)) {
                        // Stat card content
                    }
                }
            }
        }
    }
}
```

---

## Best Practices

### DO ✅
- **Use predefined components** from `DonorPlusComponents.kt`
- **Follow color palette** for all UI elements
- **Apply consistent spacing** using `DonorPlusSpacing`
- **Add animations** to enhance UX (entrance, transitions)
- **Handle loading states** with `DonorPlusLoadingIndicator`
- **Show error messages** using `DonorPlusErrorMessage`
- **Test on multiple screen sizes**
- **Use proper accessibility labels**
- **Add null safety checks**
- **Handle edge cases** (empty states, errors)

### DON'T ❌
- **Don't use random colors** - stick to the palette
- **Don't use arbitrary spacing** - use the spacing system
- **Don't create custom buttons** - use provided variants
- **Don't forget animations** - they enhance UX
- **Don't ignore loading states**
- **Don't skip error handling**
- **Don't use hardcoded strings** - use string resources
- **Don't forget content descriptions** for icons
- **Don't create inconsistent layouts**
- **Don't use dark theme colors** - app is light mode only

---

## Quick Reference

### Import Statements
```kotlin
// Components
import com.tofiq.blood.ui.components.*

// Theme colors
import com.tofiq.blood.ui.theme.*

// Material Icons
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
```

### Common Modifiers
```kotlin
// Full width
Modifier.fillMaxWidth()

// Standard padding
Modifier.padding(DonorPlusSpacing.L)

// Shadow
Modifier.shadow(8.dp, RoundedCornerShape(24.dp))

// Rounded corners
Modifier.clip(RoundedCornerShape(12.dp))

// Background gradient
Modifier.background(
    brush = Brush.linearGradient(
        colors = listOf(GradientStart, GradientMiddle, GradientEnd)
    )
)
```

---

## Support & Questions

If you have questions about the design system:
1. Check this guide first
2. Review `DonorPlusComponents.kt` for implementation details
3. Look at existing screens (Login, Register) for examples
4. Refer to `AuthScreens.kt` for advanced animation examples

---

## Changelog

### Version 1.0 (October 2025)
- Initial design system release
- Component library created
- Login/Register screens redesigned
- Light mode only enforced
- Animation guidelines established

---

**Remember**: Consistency is key! Use these components to build a cohesive, beautiful app that users will love. 🎨❤️

