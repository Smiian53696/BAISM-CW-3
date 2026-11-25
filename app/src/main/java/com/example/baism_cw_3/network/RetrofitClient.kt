package com.example.baism_cw_3.network

import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL =
        "https://rickandmortyapi.com/api/"

    // Certificate Pinning
    private val certificatePinner = CertificatePinner.Builder()
        .add(
            "rickandmortyapi.com",
            "sha256/4HQtdsbE14lqcEw5YHG4JVx5rHWfpCmS09cZ0yfVd4U="
        )
        .build()

    // Konfiguracja klienta HTTP
    private val client = OkHttpClient.Builder()
        .certificatePinner(certificatePinner)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(180, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val api: RickMortyApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickMortyApi::class.java)
    }
}