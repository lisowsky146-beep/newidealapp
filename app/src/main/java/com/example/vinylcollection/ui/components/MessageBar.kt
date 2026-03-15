package com.example.vinylcollection.ui.components

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable

@Composable
fun MessageBar(hostState: SnackbarHostState) {
    SnackbarHost(hostState = hostState)
}
