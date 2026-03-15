package com.example.vinylcollection.ui.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vinylcollection.ui.components.InfoCard
import com.example.vinylcollection.ui.viewmodel.VinylViewModel

@Composable
fun StatsScreen(
    viewModel: VinylViewModel,
    contentPadding: PaddingValues
) {
    val records = viewModel.records.value
    val favorites = viewModel.favorites.value
    val totalValue = records.mapNotNull { it.estimatedValue }.sum()
    val topGenre = records
        .map { it.genre }
        .filter { it.isNotBlank() }
        .groupingBy { it }
        .eachCount()
        .maxByOrNull { it.value }
        ?.key
        ?: "—"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        InfoCard("Total records", records.size.toString())
        InfoCard("Favorites", favorites.size.toString())
        InfoCard("Estimated collection value", "$${"%.2f".format(totalValue)}")
        InfoCard("Top genre", topGenre)
    }
}
