package com.example.newsapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Fields(
    val headline: String,
    val trailText: String = "",
    val thumbnail: String = DEFAULT_THUMBNAIL,
    val bodyText: String = ""
) {
    companion object {
        const val DEFAULT_THUMBNAIL =
            "https://plantillasdememes.com/img/plantillas/imagen-no-disponible01601774755.jpg"
    }
}
