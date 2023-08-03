package com.example.cryptoproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoproject.domain.model.CryptoDetails
import com.example.cryptoproject.domain.usecase.GetCryptoDetailsUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoViewModel @Inject constructor(
    private val getCryptoDetailsUsecase: GetCryptoDetailsUsecase
): ViewModel() {

    private val _cryptoDetailsList : MutableStateFlow<CryptoDetails> = MutableStateFlow(CryptoDetails())
    val cryptoDetailsList = _cryptoDetailsList.asStateFlow()

    fun getCryptoDetailsList(limit: String, offset: String) {
        viewModelScope.launch {
            getCryptoDetailsUsecase.getCryptoDetailsList(limit, offset)
                .onEach {
                    _cryptoDetailsList.value = it
                }
                .launchIn(viewModelScope)
        }
    }
}