package org.kimp.earnactive.facade.dto

import kotlinx.serialization.Serializable

@Serializable
data class Promocode(
    val uuid: String,
    val name: String,
    val desc: String,
    val cost: Int,
    val value: String = "",
    val avatarUrl: String = ""
)
