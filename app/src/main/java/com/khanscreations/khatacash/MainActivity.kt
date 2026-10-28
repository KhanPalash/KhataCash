package com.khanscreations.khatacash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.khanscreations.khatacash.data.model.TransactionType
import com.khanscreations.khatacash.ui.components.FigmaBottomNav
import com.khanscreations.khatacash.ui.navigation.Screen
import com.khanscreations.khatacash.ui.screens.customers.AddContactDialog
import com.khanscreations.khatacash.ui.screens.customers.CustomerDetailScreen
import com.khanscreations.khatacash.ui.screens.customers.CustomersScreen
import com.khanscreations.khatacash.ui.screens.dashboard.DashboardScreen
import com.khanscreations.khatacash.ui.screens.inventory.InventoryScreen
import com.khanscreations.khatacash.ui.screens.reports.ReportsScreen
import com.khanscreations.khatacash.ui.screens.transactions.AddTransactionScreen
import com.khanscreations.khatacash.ui.theme.KhataCashTheme
import com.khanscreations.khatacash.ui.viewmodel.KhataViewModel
import com.khanscreations.khatacash.ui.viewmodel.KhataViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: KhataViewModel by viewModels {
        KhataViewModelFactory((application as KhataCashApp).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KhataCashTheme {
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route ?: Screen.Dashboard.route

                var showAddTransactionScreen by remember { mutableStateOf(false) }
                var addTransactionInitialType by remember { mutableStateOf(TransactionType.SALE_CREDIT) }
                var showAddContactDialog by remember { mutableStateOf(false) }

                val isBottomNavVisible = currentRoute in listOf(
                    Screen.Dashboard.route,
                    Screen.Customers.route,
                    Screen.Inventory.route,
                    Screen.Reports.route
                )

                Scaffold(
                    bottomBar = {
                        if (isBottomNavVisible) {
                            FigmaBottomNav(
                                currentRoute = currentRoute,
                                onNavigate = { route ->
                                    navController.navigate(route) {
                                        popUpTo(Screen.Dashboard.route) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Dashboard.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Dashboard.route) {
                            DashboardScreen(
                                viewModel = viewModel,
                                onNavigateToCustomers = {
                                    navController.navigate(Screen.Customers.route)
                                },
                                onNavigateToSuppliers = {
                                    navController.navigate(Screen.Customers.route)
                                },
                                onOpenAddTransaction = { type ->
                                    addTransactionInitialType = type
                                    showAddTransactionScreen = true
                                }
                            )
                        }

                        composable(Screen.Customers.route) {
                            CustomersScreen(
                                viewModel = viewModel,
                                onContactClick = { contactId ->
                                    navController.navigate(Screen.CustomerDetail.createRoute(contactId))
                                },
                                onAddNewContact = {
                                    showAddContactDialog = true
                                }
                            )
                        }

                        composable(
                            route = Screen.CustomerDetail.route,
                            arguments = listOf(navArgument("contactId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val contactId = backStackEntry.arguments?.getLong("contactId") ?: 0L
                            CustomerDetailScreen(
                                contactId = contactId,
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(Screen.Inventory.route) {
                            InventoryScreen(viewModel = viewModel)
                        }

                        composable(Screen.Reports.route) {
                            ReportsScreen(viewModel = viewModel)
                        }
                    }

                    // Figma Frame 03: Add Transaction Modal Screen
                    if (showAddTransactionScreen) {
                        AddTransactionScreen(
                            viewModel = viewModel,
                            initialType = addTransactionInitialType,
                            onDismiss = { showAddTransactionScreen = false }
                        )
                    }

                    // Add Contact Dialog
                    if (showAddContactDialog) {
                        AddContactDialog(
                            onDismiss = { showAddContactDialog = false },
                            onConfirm = { name, phone, address, initialBalance, isCustomer ->
                                if (isCustomer) {
                                    viewModel.addCustomer(name, phone, address, initialBalance)
                                } else {
                                    viewModel.addSupplier(name, phone, address, initialBalance)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
