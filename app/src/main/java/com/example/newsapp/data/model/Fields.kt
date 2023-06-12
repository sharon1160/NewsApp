package com.example.newsapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Fields (
    val headline: String,
    val trailText: String = "",
    val thumbnail: String = "https://plantillasdememes.com/img/plantillas/imagen-no-disponible01601774755.jpg",
    val bodyText: String = ""
)
