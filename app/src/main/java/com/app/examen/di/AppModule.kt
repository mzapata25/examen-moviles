package com.app.examen.di

import com.app.examen.data.remote.CovidApi
import com.app.examen.data.repository.CovidRepositoryImpl
import com.app.examen.domain.repository.CovidRepository
import retrofit2.converter.gson.GsonConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.api-ninjas.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCovidApi(retrofit: Retrofit): CovidApi {
        return retrofit.create(CovidApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCovidRepository(api: CovidApi): CovidRepository {
        return CovidRepositoryImpl(api)
    }
}