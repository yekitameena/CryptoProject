package com.example.cryptoproject.data.remote

import com.example.cryptoproject.domain.model.CryptoDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoApi {

    @GET("/v2/assets")
    suspend fun getCryptoDetails(
        @Query("limit") limit : String,
        @Query("offset") offset : String
    ): Response<CryptoDetails>
}