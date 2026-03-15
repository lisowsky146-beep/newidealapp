package com.example.vinylcollection.ui.screens.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vinylcollection.data.model.DiscogsReleaseDto
import com.example.vinylcollection.ui.viewmodel.VinylViewModel
import kotlinx.coroutines.launch

@Composable
fun AddRecordScreen(
    viewModel: VinylViewModel,
    contentPadding: PaddingValues,
    openScanner: () -> Unit,
    openOcr: () -> Unit
) {
    var artist by rememberSaveable { mutableStateOf("") }
    var album by rememberSaveable { mutableStateOf("") }
    var yearText by rememberSaveable { mutableStateOf("") }
    var genre by rememberSaveable { mutableStateOf("") }
    var label by rememberSaveable { mutableStateOf("") }
    var barcode by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }
    var coverText by rememberSaveable { mutableStateOf("") }
    var coverImageUri by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedReleaseId by rememberSaveable { mutableStateOf<Int?>(null) }
    var estimatedValue by rememberSaveable { mutableStateOf<Double?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel.scannedBarcode, viewModel.ocrText) {
        val scanned = viewModel.consumeScannedBarcode()
        if (scanned.isNotBlank()) barcode = scanned
        val ocr = viewModel.consumeOcrText()
        if (ocr.isNotBlank()) {
            coverText = if (coverText.isBlank()) ocr else coverText + "
" + ocr
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Add record")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                OutlinedTextField(value = artist, onValueChange = { artist = it }, label = { Text("Artist") }, modifier = Modifier.fillMaxWidth())
            }
            item {
                OutlinedTextField(value = album, onValueChange = { album = it }, label = { Text("Album") }, modifier = Modifier.fillMaxWidth())
            }
            item {
                OutlinedTextField(value = yearText, onValueChange = { yearText = it }, label = { Text("Year") }, modifier = Modifier.fillMaxWidth())
            }
            item {
                OutlinedTextField(value = genre, onValueChange = { genre = it }, label = { Text("Genre") }, modifier = Modifier.fillMaxWidth())
            }
            item {
                OutlinedTextField(value = label, onValueChange = { label = it }, label = { Text("Label") }, modifier = Modifier.fillMaxWidth())
            }
            item {
                OutlinedTextField(value = barcode, onValueChange = { barcode = it }, label = { Text("Barcode") }, modifier = Modifier.fillMaxWidth())
            }
            item {
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes") }, modifier = Modifier.fillMaxWidth())
            }
            item {
                OutlinedTextField(value = coverText, onValueChange = { coverText = it }, label = { Text("OCR cover text") }, modifier = Modifier.fillMaxWidth())
            }
            item {
                Button(onClick = openScanner) { Text("Open barcode scanner") }
            }
            item {
                Button(onClick = openOcr) { Text("Run OCR on cover photo") }
            }
            item {
                Button(onClick = { viewModel.searchDiscogs(barcode, artist, album) }) { Text("Search Discogs") }
            }
            if (viewModel.discogsLoading) {
                item { CircularProgressIndicator() }
            }
            if (viewModel.discogsResults.isNotEmpty()) {
                item { Text("Discogs matches") }
                items(viewModel.discogsResults, key = { it.id }) { release ->
                    DiscogsResultRow(release = release, onClick = {
                        artist = release.title.substringBefore(" - ", artist)
                        album = release.title.substringAfter(" - ", release.title)
                        yearText = release.year?.toString().orEmpty().ifBlank { yearText }
                        genre = release.genre?.joinToString().orEmpty().ifBlank { genre }
                        label = release.label?.firstOrNull().orEmpty().ifBlank { label }
                        barcode = release.barcode?.firstOrNull().orEmpty().ifBlank { barcode }
                        selectedReleaseId = release.id
                        scope.launch {
                            runCatching { viewModel.getRelease(release.id) }
                                .onSuccess { details ->
                                    estimatedValue = details.community?.medianPrice ?: details.community?.lowestPrice
                                }
                        }
                    })
                    HorizontalDivider()
                }
            }
            item {
                if (estimatedValue != null) {
                    Text("Estimated value: $${"%.2f".format(estimatedValue)}")
                }
            }
            item {
                Button(onClick = {
                    viewModel.addRecord(
                        artist = artist,
                        album = album,
                        year = yearText.toIntOrNull(),
                        genre = genre,
                        label = label,
                        barcode = barcode,
                        notes = notes,
                        coverText = coverText,
                        coverImageUri = coverImageUri,
                        discogsReleaseId = selectedReleaseId,
                        estimatedValue = estimatedValue
                    )
                    artist = ""
                    album = ""
                    yearText = ""
                    genre = ""
                    label = ""
                    barcode = ""
                    notes = ""
                    coverText = ""
                    coverImageUri = null
                    selectedReleaseId = null
                    estimatedValue = null
                }) {
                    Text("Save record")
                }
            }
        }
    }
}

@Composable
private fun DiscogsResultRow(release: DiscogsReleaseDto, onClick: () -> Unit) {
    Column(modifier = Modifier.clickable(onClick = onClick).padding(vertical = 8.dp)) {
        Text(release.title)
        Text("Year: ${release.year ?: "—"}")
        Text("Genre: ${release.genre?.joinToString().orEmpty().ifBlank { "—" }}")
        Text("Label: ${release.label?.firstOrNull().orEmpty().ifBlank { "—" }}")
    }
}
