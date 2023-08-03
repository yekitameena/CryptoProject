package com.example.cryptoproject.domain.model

data class CryptoItem(
    val name: String,
    val changePercent24Hr: String,
    val priceUsd: String
)

data class CryptoDetails(
    val data: List<CryptoItem>? = null
)