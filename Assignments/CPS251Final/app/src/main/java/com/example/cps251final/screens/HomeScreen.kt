package com.example.cps251final.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cps251final.data.Category
import com.example.cps251final.data.Transaction
import com.example.cps251final.data.TransactionType
import com.example.cps251final.viewmodel.FinanceViewModel
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: FinanceViewModel,
    onAddClick: () -> Unit,
    onViewAllClick: () -> Unit,
    onManageCategoriesClick: () -> Unit
) {
    val categories by viewModel.allCategories.collectAsState()
    val transactions by viewModel.allTransactions.collectAsState()
    val income by viewModel.incomeTotal.collectAsState()
    val expense by viewModel.expenseTotal.collectAsState()
    val balance by viewModel.balance.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.refreshTotals()
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finance Tracker") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                val isPositive = balance > 0

                val bgColor = if (isPositive)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.errorContainer

                val textColor = if (isPositive)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onErrorContainer

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = bgColor)

                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isPositive)
                                    Icons.Default.CheckCircle
                                else
                                    Icons.Default.Warning,
                                contentDescription = null,
                                tint = textColor
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Balance",
                                color = textColor,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "$${"%.2f".format(balance)}",
                            color = textColor,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xE62BBD2B)
                        )
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(
                                "Income",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "$${"%.2f".format(income)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xE6F57369)
                        )
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(
                                "Expense",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "$${"%.2f".format(expense)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF44336)
                            )
                        }
                    }
                }
            }
            item {
                Text(
                    "Quick Actions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    ActionCard(
                        text = "View Transactions",
                        onClick = onViewAllClick,
                        modifier = Modifier.weight(1f))

                    ActionCard("Categories",
                        onManageCategoriesClick,
                        modifier = Modifier.weight(1f))
                }
            }
            item {
                Text(
                    "Recent Transactions (Most Recent 5)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable{onViewAllClick()}
                )
            }

            if (transactions.isEmpty()) {
                item {
                    Card {
                        Text(
                            "No transactions yet",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {

                items(transactions.take(5)) { tx ->
                    TransactionItem(tx = tx,
                        categories = categories)
                }
            }
        }
    }
}

@Composable
fun ActionCard(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun TransactionItem(tx: Transaction,
                    categories: List<Category>) {

    val dateFormat = remember {
        SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    }

    val date = dateFormat.format(Date(tx.date))
    val categoryName = categories
        .find{it.id == tx.categoryId}
        ?.name ?: "Unkown"
    val amountColor = if (tx.type == TransactionType.EXPENSE)
        Color(0xFFF44336)
    else
        Color(0xFF4CAF50)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(Modifier.padding(12.dp)) {

                Text(
                    tx.description,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    "$categoryName • $date",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                "$${"%.2f".format(tx.amount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
        }
    }
}