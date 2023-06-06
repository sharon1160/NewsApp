package com.example.newsapp.service.model
import kotlinx.serialization.Serializable

@Serializable
data class NewResponse(
    val response: ResponseData
)
