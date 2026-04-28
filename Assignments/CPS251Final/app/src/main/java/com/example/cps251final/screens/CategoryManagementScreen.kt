package com.example.cps251final.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cps251final.data.*
import com.example.cps251final.viewmodel.FinanceViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagementScreen(
    viewModel: FinanceViewModel,
    onBack: () -> Unit
) {

    val categories by viewModel.allCategories.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<Category?>(null) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<Category?>(null) }
    var showErrorDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState)},
        topBar = {
            TopAppBar(
                title = { Text("Categories") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editingCategory = null
                showDialog = true
            }) {
                Text("+")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (categories.isEmpty()) {
                Text(
                    "No categories yet",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                categories.forEach { category ->
                    CategoryItem(
                        category = category,
                        onClick = {
                            editingCategory = category
                            showDialog = true
                        },
                        onDelete = {
                            categoryToDelete = category
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }
    if (showDialog) {

        var name by remember { mutableStateOf(editingCategory?.name ?: "") }
        var type by remember { mutableStateOf(editingCategory?.type ?: TransactionType.EXPENSE) }
        var color by remember { mutableStateOf(editingCategory?.color ?: "#6200EE") }

        val colorOptions = listOf(
            "#6200EE", "#03DAC5", "#3700B3", "#018786", "#000000"
        )

        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = {

                    if (name.isBlank()) return@Button

                    val category = Category(
                        id = editingCategory?.id ?: 0,
                        name = name,
                        type = type,
                        color = color
                    )

                    if (editingCategory == null) {
                        viewModel.addCategory(category)
                    } else {
                        viewModel.updateCategory(category)
                    }

                    showDialog = false
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text(if (editingCategory == null) "Add Category" else "Edit Category") },
            text = {

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    Text("Type", fontWeight = FontWeight.Bold)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TransactionType.values().forEach {
                            FilterChip(
                                selected = type == it,
                                onClick = { type = it },
                                label = { Text(it.name) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Category Name") }
                    )

                    Text("Color", fontWeight = FontWeight.Bold)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        colorOptions.forEach { hex ->
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(android.graphics.Color.parseColor(hex)), CircleShape)
                                    .clickable { color = hex }
                            )
                        }
                    }
                }
            }
        )
    }
    if (showDeleteDialog && categoryToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                Button(onClick = {
                    val category = categoryToDelete!!
                    val isUsed = viewModel.allTransactions.value
                        .any{it.categoryId == category.id}
                    if(isUsed){
                        showErrorDialog = true

                    }else{
                        viewModel.deleteCategory(categoryToDelete!!)
                        showDeleteDialog = false
                    }}) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Delete Category") },
            text = { Text("Are you sure you want to delete this category?") }
        )
    }
    if(showErrorDialog){
        AlertDialog(onDismissRequest = {showErrorDialog = false},
            confirmButton = {
                TextButton(onClick =  { showErrorDialog = false}){
                    Text("OK")
                }
            },
            title = { Text("Cannot Delete Category")},
            text = {
                Text("Cannot delete this category because it has transactions. " +
                "Please delete or reassign those transactions first.")
            }
            )
    }
}
@Composable
fun CategoryItem(
    category: Category,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Color(android.graphics.Color.parseColor(category.color)),
                        CircleShape
                    )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    category.name,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    category.type.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            TextButton(onClick = onDelete) {
                Text("Delete", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}