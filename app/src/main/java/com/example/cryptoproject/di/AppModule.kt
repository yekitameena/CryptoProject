package com.example.cryptoproject.di

import com.example.cryptoproject.common.Constants.BASE_URL
import com.example.cryptoproject.data.remote.CryptoApi
import com.example.cryptoproject.data.repository.CryptoRepositoryImpl
import com.example.cryptoproject.domain.repository.CryptoRepository
import com.example.cryptoproject.domain.usecase.GetCryptoDetailsUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {
    @Provides
    @ViewModelScoped
    fun provideCryptoApi(): CryptoApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoApi::class.java)
    }

    @ViewModelScoped
    @Provides
    fun providesCryptoRepository(cryptoApi: CryptoApi): CryptoRepository =
        CryptoRepositoryImpl(cryptoApi)


    @ViewModelScoped
    @Provides
    fun provideCryptoUseCase(
        repository: CryptoRepository
    ): GetCryptoDetailsUsecase {
        return GetCryptoDetailsUsecase(repository)
    }
}