package org.kimp.earnactive.facade.dto

import kotlinx.serialization.Serializable

@Serializable
data class WeatherInfo(
    val temperature: Double,
    val backgroundUrl: String,
    val weatherType: String
)
