package com.example.assignment4nheemstar

import android.os.Bundle
import android.provider.Contacts
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.internal.isLiveLiteralsEnabled
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * This project covers concepts from Chapter 7 lessons:
 * - "Validation" - for form validation and error handling
 * - "Managing Input State" - for state management in forms
 * - "Text Fields" - for input field styling and error states
 * - "Regular Expressions" - for email, phone, and ZIP code validation
 *
 * Students should review these lessons before starting:
 * - Validation lesson for form validation patterns
 * - Managing Input State lesson for state management
 * - Text Fields lesson for input field styling
 * - Regular Expressions lesson for validation patterns
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ContactValidatorApp()
            }
        }
    }
}

@Composable
fun ContactValidatorApp() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Contact Information",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        ContactForm()
    }
}

@Composable
fun ContactForm() {
    // TODO: Create state variables for form fields
    // Hint: You need variables for:
    // - name (string for user's name)
    // - email (string for email address)
    // - phone (string for phone number)
    // - zipCode (string for ZIP code)
    // Use remember and mutableStateOf for each
    // See "Validation" lesson for examples of state management
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }


    // TODO: Create validation state variables
    // Hint: You need boolean variables for:
    // - isNameValid, isEmailValid, isPhoneValid, isZipValid
    // Use remember and mutableStateOf for each
    // See "Managing Input State" lesson for examples of validation state management
    var isNameValid by remember { mutableStateOf(true)}
    var isEmailValid by remember { mutableStateOf(true)}
    var isPhoneValid by remember { mutableStateOf(true) }
    var isZipValid by remember { mutableStateOf(true) }

    // TODO: Create submitted information state variable
    // Hint: You need a variable for: submittedInfo (string for displaying submitted data)
    // Use remember and mutableStateOf
    var submittedInfo by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surface,
                // TODO: Apply a RoundedCornerShape with a reasonable dp value
                // See "Text Fields" lesson for examples of shape customization
            shape = RoundedCornerShape(12.dp)
            )
            // TODO: Add horizontal and vertical padding with a reasonable dp value
            // See "Text Fields" lesson for examples of padding
        .padding(horizontal = 16.dp, vertical = 8.dp),
    // TODO: Arrange items vertically with a reasonable dp spacing
    // See "Text Fields" lesson for examples of vertical arrangement
    verticalArrangement = Arrangement.spacedBy(/* TODO: Provide a reasonable value for vertical spacing */ 12.dp)
    ) {
        // TODO: Call the NameField composable here
        // Pass the name state, validation state, and onValueChange lambda
        // See "Text Fields" lesson for examples of error state styling
        NameField(
            name = name,
            isNameValid = isNameValid,
            onValueChange = { newValue ->
                name = newValue
                isNameValid = newValue.isNotBlank()
            }
        )

        // TODO: Call the EmailField composable here
        // Pass the email state, validation state, and onValueChange lambda
        // See "Validation" lesson for email validation examples
        EmailField(
            email = email,
            isEmailValid = isEmailValid,
            onValueChange = { newValue ->
                email = newValue
                isEmailValid = validateEmail(newValue)
            }
        )

        // TODO: Call the PhoneField composable here
        // Pass the phone state, validation state, and onValueChange lambda
        // See "Regular Expressions" lesson for phone number validation patterns
        PhoneField(
            phone=phone,
            isPhoneValid = isPhoneValid,
            onValueChange = { newValue ->
                phone = newValue
                isPhoneValid = validatePhone(newValue)
            }
        )

        // TODO: Call the ZipCodeField composable here
        // Pass the zipCode state, validation state, and onValueChange lambda
        // See "Regular Expressions" lesson for ZIP code validation examples
        ZipCodeField(
            zipCode = zipCode,
            isZipValid = isZipValid
        ) {newValue ->
            zipCode = newValue
        isZipValid = validateZipCode(newValue)
        }

        // TODO: Create the Submit button
        // Use Button composable with enabled state based on all validations
        // The button should only be enabled when all fields are valid and not empty
        // See "Validation" lesson for examples of complex button state management
        Button(
            onClick = {
                submittedInfo = """
            Name: $name
            Email: $email
            Phone: $phone
            ZIP: $zipCode
        """.trimIndent()
            },
            enabled = isNameValid &&
                    isEmailValid &&
                    isPhoneValid &&
                    isZipValid &&
                    name.isNotBlank() &&
                    email.isNotBlank() &&
                    phone.isNotBlank() &&
                    zipCode.isNotBlank()
        ) {
            Text("Submit")
        }

        // TODO: Add display for submitted information
        if (submittedInfo.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = submittedInfo,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun NameField(
    name: String,
    isNameValid: Boolean,
    onValueChange: (String) -> Unit
) {
    // TODO: Create the name input field
    // Use OutlinedTextField with:
    // - value = name
    // - onValueChange = onValueChange
    // - label = "Full Name"
    // - isError = !isNameValid && name.isNotEmpty()
    // - supportingText that shows "Please enter your name" when invalid
    // - keyboardOptions with ImeAction.Next
    // - modifier = Modifier.fillMaxWidth()
    // See "Text Fields" lesson for complete examples of error state styling
    OutlinedTextField(
        value = name,
        onValueChange = onValueChange,
        label = { Text("Full Name") },
        isError = !isNameValid && name.isNotEmpty(),
        supportingText = {
            if (!isNameValid && name.isNotEmpty()) {
                Text("Please enter your name")
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        modifier = Modifier.fillMaxWidth()
    )


}

@Composable
fun EmailField(
    email: String,
    isEmailValid: Boolean,
    onValueChange: (String) -> Unit
) {
    // TODO: Create the email input field
    // Use OutlinedTextField with:
    // - value = email
    // - onValueChange = onValueChange
    // - label = "Email Address"
    // - isError = !isEmailValid && email.isNotEmpty()
    // - supportingText that shows "Please enter a valid email address" when invalid
    // - keyboardOptions with KeyboardType.Email and ImeAction.Next
    // - modifier = Modifier.fillMaxWidth()
    // See "Validation" lesson for email validation examples with regex
    OutlinedTextField(
        value = email,
        onValueChange = onValueChange,
        label = { Text("Email Address") },
        isError = !isEmailValid && email.isNotEmpty(),
        supportingText = {
            if (!isEmailValid && email.isNotEmpty()) {
                Text("Please enter a valid email address")
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        modifier = Modifier.fillMaxWidth()
    )


}

@Composable
fun PhoneField(
    phone: String,
    isPhoneValid: Boolean,
    onValueChange: (String) -> Unit
) {
    // TODO: Create the phone input field
    // Use OutlinedTextField with:
    // - value = phone
    // - onValueChange = onValueChange
    // - label = "Phone Number"
    // - isError = !isPhoneValid && phone.isNotEmpty()
    // - supportingText that shows "Please enter a valid phone number (XXX-XXX-XXXX or XXX/XXX/XXXX)" when invalid
    // - keyboardOptions with KeyboardType.Phone and ImeAction.Next
    // - modifier = Modifier.fillMaxWidth()
    // See "Regular Expressions" lesson for phone number validation patterns and examples
    OutlinedTextField(
        value = phone,
        onValueChange = onValueChange,
        label = { Text("Phone Number") },
        isError = !isPhoneValid && phone.isNotEmpty(),
        supportingText = {
            if (!isPhoneValid && phone.isNotEmpty()) {
                Text("Please enter a valid phone number (xxx-xxx-xxxx or xxx/xxx/xxxx)")
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        modifier = Modifier.fillMaxWidth()
    )

}

@Composable
fun ZipCodeField(
    zipCode: String,
    isZipValid: Boolean,
    onValueChange: (String) -> Unit
) {
    // TODO: Create the ZIP code input field
    // Use OutlinedTextField with:
    // - value = zipCode
    // - onValueChange = onValueChange
    // - label = "ZIP Code"
    // - isError = !isZipValid && zipCode.isNotEmpty()
    // - supportingText that shows "Please enter a valid 5-digit ZIP code" when invalid
    // - keyboardOptions with KeyboardType.Number and ImeAction.Done
    // - modifier = Modifier.fillMaxWidth()
    // See "Regular Expressions" lesson for ZIP code validation examples
    OutlinedTextField(
        value = zipCode,
        onValueChange = onValueChange,
        label = { Text("ZIP Code") },
        isError = !isZipValid && zipCode.isNotEmpty(),
        supportingText = {
            if (!isZipValid && zipCode.isNotEmpty()) {
                Text("Please enter a valid 5-digit ZIP code")
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        modifier = Modifier.fillMaxWidth()
    )

}

// TODO: Create validation functions using regex
// Hint: You need three functions:
// 1. validateEmail() - checks email format using regex pattern
//    Use the pattern: "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
// 2. validatePhone() - checks for phone numbers like 123-456-7890 or 123/456/7890
//    Use the pattern: "^\\d{3}[-/]\\d{3}[-/]\\d{4}$"
// 3. validateZipCode() - checks for exactly 5 digits
//    Use the pattern: "^\\d{5}$"
// Use the .matches() function with regex patterns
// See "Regular Expressions" lesson for complete regex examples and validation patterns

fun validateEmail(email: String): Boolean {
    val pattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    return pattern.matches(email)
}
fun validatePhone(phone: String): Boolean {
    val pattern = Regex("^\\d{3}[-/]\\d{3}[-/]\\d{4}$")
    return pattern.matches(phone)
}
fun validateZipCode(zipCode: String): Boolean{
    val pattern = Regex("^\\d{5}$")
    return pattern.matches(zipCode)
}

// The submit button logic combines multiple validations:
// enabled = isNameValid && isEmailValid && isPhoneValid && isZipValid &&
//          name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && zipCode.isNotEmpty()
// See "Validation" lesson for detailed examples of complex button state management
//when button is clicked and all fields are valid and not empty, the submitted information should be displayed
//in a text field below the button.


/**
 * Preview for Android Studio's design view.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ContactValidatorAppPreview() {
    ContactValidatorApp()
}

/**
 * Show me your ContactForm function and point to where you declare your state variables for form fields. Explain why you need separate variables for the input values and validation states.  Or if you did not have seperate variables why not?
 * Show me one of your input fields (like NameField or EmailField) and point to the error part. Explain how it creates visual feedback for users
 * Point to your validateEmail function and explain the regex pattern you used. What would happen if you changed the pattern to something simpler like just checking for '@' and '.' characters? Why is the current pattern (shown in the starter script) more robust?
 * Point to your Submit button and explain the enabled condition. What would happen if you simplified the condition to just check if all fields are not empty without validation?
 * Point to your Submit button and explain how it gets the text to appear below it when clicked.?
 */