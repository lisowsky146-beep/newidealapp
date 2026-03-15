package com.example.vinylcollection.data.network

import com.example.vinylcollection.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DiscogsApi {
    private val authInterceptor = Interceptor { chain ->
        val builder = chain.request().newBuilder()
            .header("User-Agent", "VinylCollectionApp/1.0")

        if (BuildConfig.DISCOGS_TOKEN.isNotBlank()) {
            builder.header("Authorization", "Discogs token=" + BuildConfig.DISCOGS_TOKEN)
        }

        chain.proceed(builder.build())
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    val service: DiscogsService = Retrofit.Builder()
        .baseUrl("https://api.discogs.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DiscogsService::class.java)
}
