package com.example.cps251final.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cps251final.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.room.TypeConverters
import androidx.room.TypeConverter
import com.example.cps251final.data.FinanceRepository

class FinanceViewModel(
    private val repository: FinanceRepository
) : ViewModel(){

    //Transaction stuff
    val allTransactions: StateFlow<List<Transaction>> = repository.getAllTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTransaction(transaction: Transaction){
        viewModelScope.launch {repository.insertTransaction(transaction)}
    }
    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch{repository.updateTransaction(transaction)}
    }
    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch { repository.deleteTransaction(transaction) }
    }

    suspend fun getTransactionById(id: Long): Transaction? {
        return repository.getTransactionById(id)
    }
    //Category stuff
    val allCategories: StateFlow<List<Category>> =
        repository.getAllCategories().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addCategory(category: Category){
        viewModelScope.launch { repository.insertCategory(category) }
    }
    fun updateCategory(category: Category) {
        viewModelScope.launch { repository.updateCategory(category) }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch { repository.deleteCategory(category) }
    }
    fun categoryHasTransactions(categoryId: Long): Boolean {
        return allTransactions.value.any { it.categoryId == categoryId }
    }
    //totals
    private val _incomeTotal = MutableStateFlow(0.0)
    val incomeTotal: StateFlow<Double> = _incomeTotal
    private val _expenseTotal = MutableStateFlow(0.0)
    val expenseTotal: StateFlow<Double> = _expenseTotal

    fun refreshTotals() {
        viewModelScope.launch {
            _incomeTotal.value = repository.getTotalByType(TransactionType.INCOME)
            _expenseTotal.value = repository.getTotalByType(TransactionType.EXPENSE)
        }
    }
    val balance: StateFlow<Double> =
        combine(incomeTotal, expenseTotal) {
                income, expense -> income - expense
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),0.0)
}