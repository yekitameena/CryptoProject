package com.example.cryptoproject.data.repository

import com.example.cryptoproject.data.remote.CryptoApi
import com.example.cryptoproject.domain.model.CryptoDetails
import com.example.cryptoproject.domain.repository.CryptoRepository
import retrofit2.Response
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor (
    private val api: CryptoApi
) : CryptoRepository {

     override suspend fun getCryptoDetailsList(limit: String, offset: String): Response<CryptoDetails> {
        return api.getCryptoDetails(limit, offset)
    }
}