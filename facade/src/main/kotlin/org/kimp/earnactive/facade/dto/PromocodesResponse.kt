package org.kimp.earnactive.facade.dto

import kotlinx.serialization.Serializable

@Serializable
data class PromocodesResponse(
    val codes: List<Promocode>
)
