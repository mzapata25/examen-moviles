package com.app.examen.data.remote

import com.app.examen.data.remote.dto.CovidRegionDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CovidApi {
    @GET("v1/covid19")
    suspend fun getCovidData(
        @Header("X-Api-Key") apiKey: String, // Pasaremos la key aquí
        @Query("country") country: String
    ): List<CovidRegionDto>
}