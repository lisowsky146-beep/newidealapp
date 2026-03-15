package com.example.vinylcollection.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vinylcollection.data.model.VinylRecord

@Composable
fun RecordCard(
    record: VinylRecord,
    onToggleFavorite: () -> Unit,
    onRefreshValue: () -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(record.artist.ifBlank { "Unknown artist" }, style = MaterialTheme.typography.titleMedium)
                    Text(record.album.ifBlank { "Unknown album" }, style = MaterialTheme.typography.bodyLarge)
                }
                Row {
                    IconButton(onClick = onRefreshValue) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh value")
                    }
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            if (record.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite"
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
            if (record.year != null) Text("Year: ${record.year}")
            if (record.genre.isNotBlank()) Text("Genre: ${record.genre}")
            if (record.label.isNotBlank()) Text("Label: ${record.label}")
            if (record.barcode.isNotBlank()) Text("Barcode: ${record.barcode}")
            if (record.estimatedValue != null) Text("Estimated value: $${"%.2f".format(record.estimatedValue)}")
            if (record.notes.isNotBlank()) Text("Notes: ${record.notes}")
        }
    }
}
