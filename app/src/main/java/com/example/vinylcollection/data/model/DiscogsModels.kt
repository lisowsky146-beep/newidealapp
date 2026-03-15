package com.example.vinylcollection.data.model

import com.google.gson.annotations.SerializedName

data class DiscogsSearchResponse(
    @SerializedName("results") val results: List<DiscogsReleaseDto> = emptyList()
)

data class DiscogsReleaseDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("year") val year: Int? = null,
    @SerializedName("genre") val genre: List<String>? = null,
    @SerializedName("label") val label: List<String>? = null,
    @SerializedName("barcode") val barcode: List<String>? = null,
    @SerializedName("thumb") val thumb: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("format") val format: List<String>? = null
)

data class DiscogsReleaseDetails(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("artists") val artists: List<DiscogsArtistDto> = emptyList(),
    @SerializedName("year") val year: Int? = null,
    @SerializedName("genres") val genres: List<String> = emptyList(),
    @SerializedName("labels") val labels: List<DiscogsLabelDto> = emptyList(),
    @SerializedName("community") val community: DiscogsCommunityDto? = null
)

data class DiscogsArtistDto(
    @SerializedName("name") val name: String
)

data class DiscogsLabelDto(
    @SerializedName("name") val name: String
)

data class DiscogsCommunityDto(
    @SerializedName("lowest_price") val lowestPrice: Double? = null,
    @SerializedName("median_price") val medianPrice: Double? = null
)
