package com.app.examen.domain.repository

import com.app.examen.domain.model.CovidReport

interface CovidRepository {
    suspend fun getCovidDataByCountry(country: String): Result<List<CovidReport>>
}