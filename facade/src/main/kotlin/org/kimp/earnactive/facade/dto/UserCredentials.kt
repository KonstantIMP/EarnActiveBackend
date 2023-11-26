package org.kimp.earnactive.facade.dto

data class UserCredentials(
    val authToken: String,
    val refreshToken: String
)
