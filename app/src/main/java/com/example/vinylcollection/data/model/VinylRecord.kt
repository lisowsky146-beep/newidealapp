package com.example.vinylcollection.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "records")
data class VinylRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val artist: String,
    val album: String,
    val year: Int?,
    val genre: String,
    val label: String,
    val barcode: String,
    val notes: String,
    val coverText: String,
    val discogsReleaseId: Int?,
    val estimatedValue: Double?,
    val coverImageUri: String?,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
