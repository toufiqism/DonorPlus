# DonorPlus Design System - Quick Start Guide

## 🚀 Get Started in 5 Minutes

This quick reference guide will help you start building features with the DonorPlus design system immediately.

---

## Step 1: Import Components

```kotlin
// At the top of your file
import com.tofiq.blood.ui.components.*
import com.tofiq.blood.ui.theme.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
```

---

## Step 2: Choose Your Background

### For Auth/Special Screens
```kotlin
DonorPlusGradientBackground {
    // Your content
}
```

### For Main App Screens
```kotlin
DonorPlusSolidBackground {
    // Your content
}
```

---

## Step 3: Add Essential Components

### Standard Screen Layout
```kotlin
@Composable
fun MyNewScreen() {
    DonorPlusSolidBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(DonorPlusSpacing.L)
        ) {
            // 1. Add a header
            DonorPlusSectionHeader(text = "My Screen Title")
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.XL))
            
            // 2. Add a card for content
            DonorPlusCard {
                Column(modifier = Modifier.padding(DonorPlusSpacing.L)) {
                    // Your content here
                }
            }
        }
    }
}
```

---

## Step 4: Add Form Fields

```kotlin
var email by remember { mutableStateOf("") }
var password by remember { mutableStateOf("") }

DonorPlusTextField(
    value = email,
    onValueChange = { email = it },
    label = "Email",
    leadingIcon = Icons.Default.Email
)

Spacer(modifier = Modifier.height(DonorPlusSpacing.M))

DonorPlusTextField(
    value = password,
    onValueChange = { password = it },
    label = "Password",
    leadingIcon = Icons.Default.Lock
)
```

---

## Step 5: Add Buttons

### Primary Action (Red Button)
```kotlin
DonorPlusPrimaryButton(
    onClick = { /* Action */ },
    text = "Submit"
)
```

### Secondary Action (Blue Button)
```kotlin
DonorPlusSecondaryButton(
    onClick = { /* Action */ },
    text = "Cancel"
)
```

### Text Link
```kotlin
DonorPlusTextButton(
    onClick = { /* Action */ },
    text = "Learn More"
)
```

---

## Step 6: Handle States

### Loading State
```kotlin
var isLoading by remember { mutableStateOf(false) }

if (isLoading) {
    DonorPlusLoadingIndicator()
} else {
    DonorPlusPrimaryButton(
        onClick = { isLoading = true },
        text = "Submit"
    )
}
```

### Error State
```kotlin
var error by remember { mutableStateOf<String?>(null) }

error?.let { msg ->
    DonorPlusErrorMessage(message = msg)
    Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
}
```

### Success State
```kotlin
var showSuccess by remember { mutableStateOf(false) }

if (showSuccess) {
    DonorPlusSuccessMessage(message = "Success!")
}
```

---

## Common Patterns

### Pattern 1: Simple Form
```kotlin
@Composable
fun SimpleFormScreen() {
    var name by remember { mutableStateOf("") }
    
    DonorPlusSolidBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(DonorPlusSpacing.L)
        ) {
            DonorPlusSectionHeader(text = "Form Title")
            Spacer(modifier = Modifier.height(DonorPlusSpacing.XL))
            
            DonorPlusCard {
                Column(modifier = Modifier.padding(DonorPlusSpacing.L)) {
                    DonorPlusTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Name",
                        leadingIcon = Icons.Default.Person
                    )
                    
                    Spacer(modifier = Modifier.height(DonorPlusSpacing.L))
                    
                    DonorPlusPrimaryButton(
                        onClick = { /* Save */ },
                        text = "Save"
                    )
                }
            }
        }
    }
}
```

### Pattern 2: Welcome Screen
```kotlin
@Composable
fun WelcomeScreen() {
    DonorPlusGradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(DonorPlusSpacing.L),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DonorPlusAnimatedLogo()
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.XL))
            
            DonorPlusHeader(
                title = "Welcome!",
                subtitle = "Let's get started"
            )
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.XL))
            
            var showCard by remember { mutableStateOf(false) }
            
            LaunchedEffect(Unit) {
                delay(100)
                showCard = true
            }
            
            DonorPlusAnimatedCard(visible = showCard) {
                Column(modifier = Modifier.padding(DonorPlusSpacing.L)) {
                    DonorPlusPrimaryButton(
                        onClick = { /* Continue */ },
                        text = "Get Started"
                    )
                }
            }
        }
    }
}
```

### Pattern 3: List Screen
```kotlin
@Composable
fun ListScreen() {
    val items = listOf("Item 1", "Item 2", "Item 3")
    
    DonorPlusSolidBackground {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(DonorPlusSpacing.M)
        ) {
            item {
                DonorPlusSectionHeader(text = "List Title")
                Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
            }
            
            items(items) { item ->
                DonorPlusCard(
                    modifier = Modifier.padding(vertical = DonorPlusSpacing.S)
                ) {
                    Text(
                        text = item,
                        modifier = Modifier.padding(DonorPlusSpacing.M)
                    )
                }
            }
        }
    }
}
```

---

## Spacing Reference

```kotlin
DonorPlusSpacing.XS  // 4dp  - Tight spacing
DonorPlusSpacing.S   // 8dp  - Small spacing
DonorPlusSpacing.M   // 16dp - Medium spacing
DonorPlusSpacing.L   // 24dp - Large spacing (content padding)
DonorPlusSpacing.XL  // 40dp - Extra large spacing (sections)
```

---

## Color Reference

```kotlin
PrimaryRed      // Main actions, branding
SecondaryBlue   // Secondary actions, links
AccentCoral     // Highlights
TextPrimary     // Main text
TextSecondary   // Secondary text
ErrorRed        // Errors
SuccessGreen    // Success messages
```

---

## Common Components Cheat Sheet

| Component | Use Case | Example |
|-----------|----------|---------|
| `DonorPlusPrimaryButton` | Main actions | Login, Submit, Save |
| `DonorPlusSecondaryButton` | Alt actions | Cancel, Back |
| `DonorPlusTextField` | Input fields | Email, Name, Phone |
| `DonorPlusCard` | Content container | Forms, List items |
| `DonorPlusHeader` | Page headers | Welcome screens |
| `DonorPlusSectionHeader` | Section titles | Lists, groups |
| `DonorPlusLoadingIndicator` | Loading state | API calls |
| `DonorPlusErrorMessage` | Error display | Form errors |
| `DonorPlusAnimatedLogo` | Branding | Splash, welcome |

---

## Animation Tips

### Add entrance animation to cards:
```kotlin
var showCard by remember { mutableStateOf(false) }

LaunchedEffect(Unit) {
    delay(100)
    showCard = true
}

DonorPlusAnimatedCard(visible = showCard) {
    // Content
}
```

### Add header animation:
```kotlin
DonorPlusHeader(
    title = "My Title",
    subtitle = "Subtitle",
    animate = true  // Enable animation
)
```

### Animate logo:
```kotlin
DonorPlusAnimatedLogo(
    animate = true,
    rotationDirection = 180f  // or -180f for reverse
)
```

---

## Complete Example

```kotlin
@Composable
fun CompleteExampleScreen() {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    
    DonorPlusSolidBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(DonorPlusSpacing.L)
        ) {
            // Header
            DonorPlusSectionHeader(text = "Contact Us")
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.XL))
            
            // Form Card
            DonorPlusCard {
                Column(modifier = Modifier.padding(DonorPlusSpacing.L)) {
                    // Input
                    DonorPlusTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        leadingIcon = Icons.Default.Email
                    )
                    
                    Spacer(modifier = Modifier.height(DonorPlusSpacing.L))
                    
                    // Error
                    error?.let { msg ->
                        DonorPlusErrorMessage(message = msg)
                        Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                    }
                    
                    // Button
                    if (isLoading) {
                        DonorPlusLoadingIndicator()
                    } else {
                        DonorPlusPrimaryButton(
                            onClick = {
                                if (email.isBlank()) {
                                    error = "Email is required"
                                } else {
                                    isLoading = true
                                    error = null
                                    // Submit
                                }
                            },
                            text = "Submit"
                        )
                    }
                }
            }
        }
    }
}
```

---

## Next Steps

1. ✅ **Read this guide** - You're doing it!
2. 📖 **Check examples** - See `ExampleScreens.kt` for more patterns
3. 📚 **Read full guide** - See `DESIGN_SYSTEM_GUIDE.md` for details
4. 🎨 **Build your feature** - Use these components!
5. ✨ **Stay consistent** - Follow the patterns

---

## Need Help?

- **Examples**: `app/src/main/java/com/tofiq/blood/ui/examples/ExampleScreens.kt`
- **Components**: `app/src/main/java/com/tofiq/blood/ui/components/DonorPlusComponents.kt`
- **Full Guide**: `DESIGN_SYSTEM_GUIDE.md`
- **Current Screens**: `app/src/main/java/com/tofiq/blood/auth/ui/AuthScreens.kt`

---

## Remember

- ✅ Use predefined components
- ✅ Follow spacing system
- ✅ Stick to color palette
- ✅ Add animations
- ✅ Handle states (loading, error, success)
- ✅ Test on different screen sizes

---

**Happy coding! Build something beautiful! 🎨❤️**

