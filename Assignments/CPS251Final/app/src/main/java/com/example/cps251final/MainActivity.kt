package com.example.cps251final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cps251final.ui.theme.CPS251FinalTheme
import com.example.cps251final.viewmodel.FinanceViewModel
import com.example.cps251final.data.FinanceDatabase
import com.example.cps251final.data.FinanceRepository
import com.example.cps251final.navigation.Navigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = FinanceDatabase.getDatabase(this)

        val repository = FinanceRepository(
            database.transactionDao(),
            database.categoryDao()
        )

        val viewModel = ViewModelProvider(
            this,
            FinanceViewModelFactory(repository)
        )[FinanceViewModel::class.java]

        setContent {
            CPS251FinalTheme {
                Navigation(viewModel)

            }
        }

    }

}
class FinanceViewModelFactory(
    private val repository: FinanceRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinanceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FinanceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
/*
Why is FinanceRepository used instead of calling DAOs directly from the ViewModel? What problem does this solve?
Why is FinanceViewModelFactory needed? Why can't FinanceViewModel be instantiated directly in MainActivity?
In TransactionListScreen, how does the filter dialog update the displayed transactions? Trace the data flow from user selection to UI update.
When navigating from Home → TransactionList → AddEditTransaction, what happens to the back stack? How does popBackStack() work?
Why are repository operations wrapped in viewModelScope.launch? What would happen if they were called directly without a coroutine scope?
 */