package com.example.vinylcollection.ui.screens.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vinylcollection.ui.components.MessageBar
import com.example.vinylcollection.ui.components.RecordCard
import com.example.vinylcollection.ui.viewmodel.VinylViewModel

@Composable
fun CollectionScreen(
    viewModel: VinylViewModel,
    contentPadding: PaddingValues
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val records = viewModel.records.value
    val message = viewModel.message

    LaunchedEffect(message) {
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            viewModel.consumeMessage()
        }
    }

    Scaffold(snackbarHost = { MessageBar(snackbarHostState) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Collection")
            OutlinedTextField(
                value = viewModel.searchQuery,
                onValueChange = viewModel::updateSearchQuery,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search collection") }
            )
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(records, key = { it.id }) { record ->
                    RecordCard(
                        record = record,
                        onToggleFavorite = { viewModel.toggleFavorite(record) },
                        onRefreshValue = { viewModel.refreshRecordValue(record) },
                        onDelete = { viewModel.deleteRecord(record) }
                    )
                }
            }
        }
    }
}
