package com.example.vinylcollection.data.repository

import com.example.vinylcollection.BuildConfig
import com.example.vinylcollection.data.local.VinylDao
import com.example.vinylcollection.data.model.DiscogsReleaseDetails
import com.example.vinylcollection.data.model.DiscogsReleaseDto
import com.example.vinylcollection.data.model.VinylRecord
import com.example.vinylcollection.data.network.DiscogsService
import kotlinx.coroutines.flow.Flow

class VinylRepository(
    private val vinylDao: VinylDao,
    private val discogsService: DiscogsService
) {
    fun getAllRecords(): Flow<List<VinylRecord>> = vinylDao.getAllRecords()
    fun getFavorites(): Flow<List<VinylRecord>> = vinylDao.getFavorites()
    fun searchRecords(query: String): Flow<List<VinylRecord>> = vinylDao.searchRecords(query)

    suspend fun insert(record: VinylRecord) = vinylDao.insert(record)
    suspend fun update(record: VinylRecord) = vinylDao.update(record)
    suspend fun delete(record: VinylRecord) = vinylDao.delete(record)

    suspend fun searchDiscogs(barcode: String?, artist: String?, album: String?): List<DiscogsReleaseDto> {
        if (BuildConfig.DISCOGS_TOKEN.isBlank()) {
            throw IllegalStateException("DISCOGS_TOKEN is empty. Add it in GitHub Secrets or local environment.")
        }
        return discogsService.searchReleases(
            barcode = barcode?.takeIf { it.isNotBlank() },
            artist = artist?.takeIf { it.isNotBlank() },
            releaseTitle = album?.takeIf { it.isNotBlank() }
        ).results
    }

    suspend fun getDiscogsRelease(id: Int): DiscogsReleaseDetails = discogsService.getRelease(id)
}
