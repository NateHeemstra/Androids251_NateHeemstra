package com.example.cps251final.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cps251final.data.*
import com.example.cps251final.viewmodel.FinanceViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    viewModel: FinanceViewModel,
    onBack: () -> Unit,
    onEdit: (Long) -> Unit
) {

    val transactions by viewModel.allTransactions.collectAsStateWithLifecycle()
    val categories by viewModel.allCategories.collectAsStateWithLifecycle()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }

    // FILTER STATE
    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }

    // FILTERED LIST
    val filtered = if (selectedCategoryId == null) {
        transactions
    } else {
        transactions.filter { it.categoryId == selectedCategoryId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transactions") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("Back")
                    }
                },
                actions = {
                    TextButton(onClick = { showFilterDialog = true }) {
                        Text("Filter")
                    }
                }
            )
        }
    ) { padding ->

        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No transactions yet")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filtered) { tx ->

                    val categoryName = categories
                        .find { it.id == tx.categoryId }
                        ?.name ?: "Unknown"

                    TransactionRow(
                        transaction = tx,
                        categoryName = categoryName,
                        onClick = { onEdit(tx.id) },
                        onDelete = {
                            transactionToDelete = tx
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }

    // DELETE DIALOG
    if (showDeleteDialog && transactionToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                Button(onClick = {
                    viewModel.deleteTransaction(transactionToDelete!!)
                    viewModel.refreshTotals()
                    showDeleteDialog = false
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Delete Transaction") },
            text = { Text("Are you sure you want to delete this transaction?") }
        )
    }

    // FILTER DIALOG
    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedCategoryId = null
                    showFilterDialog = false
                }) {
                    Text("Clear")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("Close")
                }
            },
            title = { Text("Filter by Category") },
            text = {
                Column {
                    categories.forEach { cat ->
                        Text(
                            text = cat.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedCategoryId = cat.id
                                    showFilterDialog = false
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }
        )
    }
}
@Composable
fun TransactionRow(
    transaction: Transaction,
    categoryName: String,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = remember {
        SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    }

    val date = dateFormat.format(Date(transaction.date))

    val amountColor = if (transaction.type == TransactionType.EXPENSE)
        Color(0xFFF44336)
    else
        Color(0xFF4CAF50)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(transaction.description, fontWeight = FontWeight.Bold)

                Text(
                    "$categoryName • $date",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "$${"%.2f".format(transaction.amount)}",
                    color = amountColor,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}