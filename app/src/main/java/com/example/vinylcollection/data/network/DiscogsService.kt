package com.example.vinylcollection.data.network

import com.example.vinylcollection.data.model.DiscogsReleaseDetails
import com.example.vinylcollection.data.model.DiscogsSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DiscogsService {
    @GET("database/search")
    suspend fun searchReleases(
        @Query("barcode") barcode: String? = null,
        @Query("artist") artist: String? = null,
        @Query("release_title") releaseTitle: String? = null,
        @Query("format") format: String = "Vinyl",
        @Query("type") type: String = "release"
    ): DiscogsSearchResponse

    @GET("releases/{id}")
    suspend fun getRelease(@Path("id") id: Int): DiscogsReleaseDetails
}
