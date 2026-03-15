package com.example.vinylcollection.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vinylcollection.ui.screens.add.AddRecordScreen
import com.example.vinylcollection.ui.screens.collection.CollectionScreen
import com.example.vinylcollection.ui.screens.ocr.OcrScreen
import com.example.vinylcollection.ui.screens.scanner.BarcodeScannerScreen
import com.example.vinylcollection.ui.screens.stats.StatsScreen
import com.example.vinylcollection.ui.viewmodel.VinylViewModel

sealed class Screen(val route: String, val label: String) {
    data object Collection : Screen("collection", "Collection")
    data object Add : Screen("add", "Add")
    data object Scanner : Screen("scanner", "Scanner")
    data object Ocr : Screen("ocr", "OCR")
    data object Stats : Screen("stats", "Stats")
}

@Composable
fun VinylNavGraph(viewModel: VinylViewModel) {
    val navController = rememberNavController()
    val screens = listOf(Screen.Collection, Screen.Add, Screen.Scanner, Screen.Ocr, Screen.Stats)

    Scaffold(
        bottomBar = {
            val currentEntry by navController.currentBackStackEntryAsState()
            val currentRoute = currentEntry?.destination?.route
            NavigationBar {
                screens.forEach { screen ->
                    val icon = when (screen) {
                        Screen.Collection -> Icons.Default.Home
                        Screen.Add -> Icons.Default.Add
                        Screen.Scanner -> Icons.Default.Search
                        Screen.Ocr -> Icons.Default.ReceiptLong
                        Screen.Stats -> Icons.Default.Favorite
                    }
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                            }
                        },
                        icon = { Icon(icon, contentDescription = screen.label) },
                        label = { Text(screen.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Collection.route,
            modifier = Modifier
        ) {
            composable(Screen.Collection.route) {
                CollectionScreen(viewModel = viewModel, contentPadding = paddingValues)
            }
            composable(Screen.Add.route) {
                AddRecordScreen(
                    viewModel = viewModel,
                    contentPadding = paddingValues,
                    openScanner = { navController.navigate(Screen.Scanner.route) },
                    openOcr = { navController.navigate(Screen.Ocr.route) }
                )
            }
            composable(Screen.Scanner.route) {
                BarcodeScannerScreen(viewModel = viewModel, contentPadding = paddingValues)
            }
            composable(Screen.Ocr.route) {
                OcrScreen(viewModel = viewModel, contentPadding = paddingValues)
            }
            composable(Screen.Stats.route) {
                StatsScreen(viewModel = viewModel, contentPadding = paddingValues)
            }
        }
    }
}
