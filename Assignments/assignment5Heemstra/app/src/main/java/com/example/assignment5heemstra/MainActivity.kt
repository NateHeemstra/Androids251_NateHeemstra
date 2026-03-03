package com.example.assignment5heemstra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check

/**
 * This project covers concepts from Chapter 8 lessons:
 * - "Lazy Column" - for creating scrollable contact lists
 * - "Handling Clicks and Selection" - for interactive contact selection
 * - "Combining LazyColumn and LazyRow" - for understanding list composition
 *
 * Students should review these lessons before starting:
 * - LazyColumn lesson for list implementation
 * - Clicks and Selection lesson for interactive behavior
 * - Combined lesson for understanding how lists work together
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // TODO: Call the main composable function
                    // Add ContactListApp() here
                    ContactListApp()
                }
            }
        }
    }
}




@Composable
fun ContactListApp() {
    // TODO: Create sample contact data
    // Create a list of 25 contacts with names, emails, and phone numbers
    // Use the Contact data class provided below
    // See "Lazy Column" lesson for examples of data structures and LazyColumn usage
    val contacts = listOf(
        // TODO: Add 25Contact objects here
        // Example: Contact("John Doe", "john@example.com", "555-0101")
        Contact("John Doe", "john@example.com", "555-0101"),
        Contact("Jane Smith", "jane@example.com", "555-0102"),
        Contact("Michael Brown", "michael@example.com", "555-0103"),
        Contact("Emily Davis", "emily@example.com", "555-0104"),
        Contact("Daniel Wilson", "daniel@example.com", "555-0105"),
        Contact("Olivia Martinez", "olivia@example.com", "555-0106"),
        Contact("Matthew Anderson", "matthew@example.com", "555-0107"),
        Contact("Sophia Taylor", "sophia@example.com", "555-0108"),
        Contact("David Thomas", "david@example.com", "555-0109"),
        Contact("Ava Hernandez", "ava@example.com", "555-0110"),
        Contact("James Moore", "james@example.com", "555-0111"),
        Contact("Isabella Martin", "isabella@example.com", "555-0112"),
        Contact("Benjamin Jackson", "benjamin@example.com", "555-0113"),
        Contact("Mia Thompson", "mia@example.com", "555-0114"),
        Contact("Lucas White", "lucas@example.com", "555-0115"),
        Contact("Charlotte Harris", "charlotte@example.com", "555-0116"),
        Contact("Henry Clark", "henry@example.com", "555-0117"),
        Contact("Amelia Lewis", "amelia@example.com", "555-0118"),
        Contact("Alexander Walker", "alexander@example.com", "555-0119"),
        Contact("Harper Hall", "harper@example.com", "555-0120"),
        Contact("Sebastian Allen", "sebastian@example.com", "555-0121"),
        Contact("Evelyn Young", "evelyn@example.com", "555-0122"),
        Contact("Jack King", "jack@example.com", "555-0123"),
        Contact("Abigail Wright", "abigail@example.com", "555-0124"),
        Contact("William Scott", "william@example.com", "555-0125")
    )

    // TODO: Call the ContactList composable
    // Pass the contacts list as a parameter
    ContactList(contacts)
}

@Composable
fun ContactList(contacts: List<Contact>) {
    // TODO: Create state variable for selected contact
    // Use remember and mutableStateOf to track which contact is selected
    // Type should be Contact? (nullable Contact)
    // See "Handling Clicks and Selection" lesson for examples of state management and selection tracking
    var selectedContact by remember {mutableStateOf<Contact?>(null)}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 50.dp)
    ) {
        // TODO: Add header text
        // Create a Text composable with:
        // - text = "Contact List"
        // - style = MaterialTheme.typography.headlineMedium
        // - modifier = Modifier.padding(bottom = 16.dp)
        Text(text = "Contact list",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // TODO: Add selection status text
        // Create a Text composable that shows:
        // - "Selected: [contact name]" if a contact is selected
        // - "No contact selected" if no contact is selected
        // - style = MaterialTheme.typography.bodyLarge
        // - modifier = Modifier.padding(bottom = 24.dp)
        Text(text = selectedContact?.let {"Selected: ${it.name}"  }
            ?: "No contact selected",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 24.dp)
            )



        // TODO: Create LazyColumn for the contact list
        // Use LazyColumn with:
        // - verticalArrangement = Arrangement.spacedBy(8.dp)
        // - items(contacts) to iterate through the contacts
        // - Call ContactItem for each contact with proper parameters
        // See "Lazy Column" lesson for complete LazyColumn implementation examples
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)){
            items(contacts){item ->
                ContactItem(contact = item,
                    isSelected = selectedContact == item,
                    onClick = {selectedContact = item}
                    )
            }


        }
        // TODO: Add clear selection button
        // Only show this button when a contact is selected
        // Use Button with:
        // - onClick to clear the selection
        // - modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        // - Text("Clear Selection")
        // See "Handling Clicks and Selection" lesson for examples of conditional UI and button interactions
        Button(onClick = {
            selectedContact = null
        },modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            ){
            Text("clear selection")
        }


    }
}

@Composable
fun ContactItem(
    contact: Contact,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO: Create contact avatar (colored circle with initials)
            // Use Box with:
            // - modifier = Modifier.size(50.dp)
            // - background with conditional color (primary when selected, secondary when not)
            // - shape = CircleShape
            // - contentAlignment = Alignment.Center
            // - Text with contact initials (first letter of each name)
            // See "Handling Clicks and Selection" lesson for examples of conditional styling based on selection state
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = if (isSelected) Color.LightGray else Color.Gray,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = contact.name
                    ?.split(" ")
                    ?.map{it.first().uppercaseChar()}
                    ?.joinToString("")
                    ?: ""
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            val textColor = if(isSelected){
                Color.Black

            }else{
                Color.Blue
            }
            // TODO: Create contact information column
            // Use Column with:
            // - modifier = Modifier.weight(1f)
            // - Text for name (titleMedium, bold, conditional color)
            // - Text for email (bodyMedium, conditional color)
            // - Text for phone (bodySmall, conditional color)
            // See "Handling Clicks and Selection" lesson for examples of conditional text styling and layout
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = contact.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Text(text = contact.email,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
                )
                Text(text = contact.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor
                )
            }
            // TODO: Add selection indicator
            // Only show when contact is selected
            // Use Icon with:
            // - imageVector = Icons.Default.Check
            // - contentDescription = "Selected"
            // - tint = MaterialTheme.colorScheme.primary
            // - modifier = Modifier.size(24.dp)
            // See "Handling Clicks and Selection" lesson for examples of conditional UI elements and visual feedback
            if(isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier . size (24.dp)
                )
            }
        }
    }
}

// Data class for contact information
data class Contact(
    val name: String,
    val email: String,
    val phone: String
)

/**
 * Preview for Android Studio's design view.

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ContactListAppPreview() {
    ContactListApp()
}*/