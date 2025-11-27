package com.app.examen.data.repository

import com.app.examen.data.remote.CovidApi
import com.app.examen.domain.model.CovidReport
import com.app.examen.domain.model.DailyStat
import com.app.examen.domain.repository.CovidRepository
import javax.inject.Inject

class CovidRepositoryImpl @Inject constructor(
    private val api: CovidApi
) : CovidRepository {
    private val apiKey = "Rg5YZ5F2dHsCtAQbNrhzCA==PhHwjRRHn89pzoBD"

    override suspend fun getCovidDataByCountry(country: String): Result<List<CovidReport>> {
        return try {
            val response = api.getCovidData(apiKey, country)

            // Mapeo de DTO (Data) a Model (Domain)
            val domainData = response.map { dto ->
                CovidReport(
                    region = if (dto.region.isEmpty()) "Global/Nacional" else dto.region,
                    dailyStats = dto.cases.map { (date, stats) ->
                        DailyStat(date, stats.total, stats.new)
                    }.sortedByDescending { it.date } // Ordenamos por fecha más reciente
                )
            }
            Result.success(domainData)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}