package org.kimp.earnactive.weather.owa.model

import kotlinx.serialization.Serializable

@Serializable
data class WindData(
    val speed: Double,
    val deg: Double
)
