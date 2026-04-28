package com.example.cps251final.screens

import android.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.FilterChip
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cps251final.data.*
import com.example.cps251final.viewmodel.FinanceViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTransactionScreen(
    viewModel: FinanceViewModel,
    transactionId: Long,
    onSave: () -> Unit,
    onBack: () -> Unit
) {

    val categories by viewModel.allCategories.collectAsState()

    var type by remember { mutableStateOf(TransactionType.EXPENSE) }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember {mutableStateOf(false)}
    var error by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(transactionId) {
        if(transactionId != -1L){
            val tx = viewModel.getTransactionById(transactionId)
            tx?.let{
                type = it.type
                selectedCategoryId = it.categoryId
                amount = it.amount.toString()
                description = it.description
                date = it.date
            }

        }

    }
    val dateFormat = remember {
        SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    }

    val filteredCategories = categories.filter { it.type == type }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (transactionId == -1L) "Add Transaction" else "Edit Transaction")
                },
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
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Type", fontWeight = FontWeight.Bold)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TransactionType.values().forEach { t ->
                    FilterChip(
                        selected = type == t,
                        onClick = { type = t },
                        label = { Text(t.name) }
                    )
                }
            }
            Text("Category", fontWeight = FontWeight.Bold)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                filteredCategories.forEach { cat ->
                    FilterChip(
                        selected = selectedCategoryId == cat.id,
                        onClick = { selectedCategoryId = cat.id },
                        label = { Text(cat.name) }
                    )
                }
            }
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth()
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{
                        showDatePicker = true
                    },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    "Date: ${dateFormat.format(Date(date))}",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            error?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Button(
                onClick = {

                    val parsedAmount = amount.toDoubleOrNull()

                    if (parsedAmount == null || parsedAmount <= 0) {
                        error = "Invalid amount"
                        return@Button
                    }

                    if (selectedCategoryId == null) {
                        error = "Select a category"
                        return@Button
                    }

                    val transaction = Transaction(
                        id = if (transactionId == -1L) 0 else transactionId,
                        amount = parsedAmount,
                        date = date,
                        description = description,
                        categoryId = selectedCategoryId!!,
                        type = type
                    )

                    if (transactionId == -1L) {
                        viewModel.addTransaction(transaction)
                    } else {
                        viewModel.updateTransaction(transaction)
                    }

                    viewModel.refreshTotals()
                    onSave()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }

            if(showDatePicker){
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = date
                )
                DatePickerDialog(
                    onDismissRequest = {showDatePicker = false},
                    confirmButton = {
                        Button(onClick = {
                            datePickerState.selectedDateMillis?.let{
                                date = it
                            }
                            showDatePicker = false
                        }){
                            Text("Ok")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {showDatePicker = false}){
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }
    }
}
