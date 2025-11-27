package com.app.examen.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CovidRegionDto(
    @SerializedName("country") // Agrega esto
    val country: String,

    @SerializedName("region") // Agrega esto
    val region: String,

    @SerializedName("cases") // Agrega esto
    val cases: Map<String, CovidStatsDto>
)

data class CovidStatsDto(
    @SerializedName("total") // Agrega esto
    val total: Int,

    @SerializedName("new") // Agrega esto
    val new: Int
)