package com.example.vinylcollection.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vinylcollection.data.model.DiscogsReleaseDetails
import com.example.vinylcollection.data.model.DiscogsReleaseDto
import com.example.vinylcollection.data.model.VinylRecord
import com.example.vinylcollection.data.repository.VinylRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VinylViewModel(
    private val repository: VinylRepository
) : ViewModel() {

    private val searchFlow = MutableStateFlow("")

    var searchQuery by mutableStateOf("")
        private set

    var message by mutableStateOf<String?>(null)
        private set

    var scannedBarcode by mutableStateOf("")
        private set

    var ocrText by mutableStateOf("")
        private set

    var discogsResults by mutableStateOf<List<DiscogsReleaseDto>>(emptyList())
        private set

    var discogsLoading by mutableStateOf(false)
        private set

    val records: StateFlow<List<VinylRecord>> = searchFlow
        .flatMapLatest { query ->
            if (query.isBlank()) repository.getAllRecords() else repository.searchRecords(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favorites: StateFlow<List<VinylRecord>> = repository.getFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) {
        searchQuery = query
        searchFlow.value = query
    }

    fun consumeMessage() {
        message = null
    }

    fun setScannedBarcode(value: String) {
        scannedBarcode = value
        message = "Barcode scanned: $value"
    }

    fun consumeScannedBarcode(): String {
        val value = scannedBarcode
        scannedBarcode = ""
        return value
    }

    fun setOcrText(value: String) {
        ocrText = value
        message = "OCR text captured"
    }

    fun consumeOcrText(): String {
        val value = ocrText
        ocrText = ""
        return value
    }

    fun addRecord(
        artist: String,
        album: String,
        year: Int?,
        genre: String,
        label: String,
        barcode: String,
        notes: String,
        coverText: String,
        coverImageUri: String?,
        discogsReleaseId: Int?,
        estimatedValue: Double?
    ) {
        viewModelScope.launch {
            repository.insert(
                VinylRecord(
                    artist = artist,
                    album = album,
                    year = year,
                    genre = genre,
                    label = label,
                    barcode = barcode,
                    notes = notes,
                    coverText = coverText,
                    coverImageUri = coverImageUri,
                    discogsReleaseId = discogsReleaseId,
                    estimatedValue = estimatedValue
                )
            )
            message = "Record added"
        }
    }

    fun toggleFavorite(record: VinylRecord) {
        viewModelScope.launch {
            repository.update(record.copy(isFavorite = !record.isFavorite))
        }
    }

    fun deleteRecord(record: VinylRecord) {
        viewModelScope.launch {
            repository.delete(record)
            message = "Record deleted"
        }
    }

    fun refreshRecordValue(record: VinylRecord) {
        val releaseId = record.discogsReleaseId ?: return
        viewModelScope.launch {
            runCatching { repository.getDiscogsRelease(releaseId) }
                .onSuccess { updateEstimatedValue(record, it) }
                .onFailure { message = it.message ?: "Failed to refresh value" }
        }
    }

    fun updateEstimatedValue(record: VinylRecord, details: DiscogsReleaseDetails) {
        viewModelScope.launch {
            val estimate = details.community?.medianPrice ?: details.community?.lowestPrice
            repository.update(
                record.copy(
                    discogsReleaseId = details.id,
                    estimatedValue = estimate,
                    year = record.year ?: details.year,
                    genre = if (record.genre.isBlank()) details.genres.joinToString() else record.genre,
                    label = if (record.label.isBlank()) details.labels.firstOrNull()?.name.orEmpty() else record.label
                )
            )
            message = if (estimate != null) "Value updated" else "Release linked, but no price found"
        }
    }

    fun searchDiscogs(barcode: String?, artist: String?, album: String?) {
        viewModelScope.launch {
            discogsLoading = true
            try {
                discogsResults = repository.searchDiscogs(barcode, artist, album)
                if (discogsResults.isEmpty()) {
                    message = "No Discogs results found"
                }
            } catch (e: Exception) {
                discogsResults = emptyList()
                message = e.message ?: "Discogs search failed"
            } finally {
                discogsLoading = false
            }
        }
    }

    suspend fun getRelease(id: Int): DiscogsReleaseDetails = repository.getDiscogsRelease(id)
}

class VinylViewModelFactory(
    private val repository: VinylRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VinylViewModel(repository) as T
    }
}
