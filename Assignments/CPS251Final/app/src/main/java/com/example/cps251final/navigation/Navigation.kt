package com.example.cps251final.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.cps251final.screens.*
import com.example.cps251final.viewmodel.FinanceViewModel

@Composable
fun Navigation(viewModel: FinanceViewModel) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"){
        composable("home"){
            HomeScreen(
                viewModel = viewModel,
                onAddClick = {
                    navController.navigate("add_edit")
                },
                onViewAllClick = {
                    navController.navigate("transactions")
                },
                onManageCategoriesClick = {
                    navController.navigate("categories")
                }
            )
        }
        composable(
            route = "add_edit?transactionId={transactionId}",
            arguments = listOf(
                navArgument("transactionId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->

            val transactionId =
                backStackEntry.arguments?.getLong("transactionId") ?: -1L

            AddEditTransactionScreen(
                viewModel = viewModel,
                transactionId = transactionId,
                onSave = {
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("categories") {
            CategoryManagementScreen(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("transactions") {
            TransactionListScreen(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                },
                onEdit = { transactionId ->
                    navController.navigate("add_edit?transactionId=$transactionId")
                }
            )
        }

    }}