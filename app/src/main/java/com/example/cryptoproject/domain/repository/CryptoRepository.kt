package com.example.cryptoproject.domain.repository

import com.example.cryptoproject.domain.model.CryptoDetails
import retrofit2.Response

interface CryptoRepository {
    suspend fun getCryptoDetailsList(limit: String, offset: String): Response<CryptoDetails>
}