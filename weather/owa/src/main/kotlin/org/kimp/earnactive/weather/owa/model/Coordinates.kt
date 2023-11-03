package org.kimp.earnactive.weather.owa.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Coordinates(
    @SerialName("lon")
    val longitude: Double,
    @SerialName("lat")
    val latitude: Double
)
