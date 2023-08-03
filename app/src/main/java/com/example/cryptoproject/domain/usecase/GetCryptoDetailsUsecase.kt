package com.example.cryptoproject.domain.usecase

import android.util.Log
import com.example.cryptoproject.common.Constants
import com.example.cryptoproject.domain.model.CryptoDetails
import com.example.cryptoproject.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCryptoDetailsUsecase @Inject constructor(private val repository: CryptoRepository) {

    suspend fun getCryptoDetailsList(limit: String, offset: String): Flow<CryptoDetails> = flow {
        try {
            val cryptoListResponse = repository.getCryptoDetailsList(limit, offset)
            if (cryptoListResponse.isSuccessful) {
                cryptoListResponse.body().let {
                    it?.let { list -> emit(list) }
                }
            }
        } catch (e: Exception) {
            Log.e(Constants.TAG, e.localizedMessage)
        }
    }

}