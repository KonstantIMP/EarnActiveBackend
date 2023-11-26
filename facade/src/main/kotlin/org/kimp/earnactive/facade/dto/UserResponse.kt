package org.kimp.earnactive.facade.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val uuid: String,
    val name: String,
    val phone: String,
)
