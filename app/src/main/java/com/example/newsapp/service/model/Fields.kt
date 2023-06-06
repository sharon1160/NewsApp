package com.example.newsapp.service.model

import kotlinx.serialization.Serializable

@Serializable
data class Fields (
    val headline: String,
    val trailText: String,
    val thumbnail: String,
    val bodyText: String
)
