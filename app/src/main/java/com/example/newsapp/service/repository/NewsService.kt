package com.example.newsapp.service.repository

import com.example.newsapp.service.model.New
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*

interface NewsService {
    suspend fun searchNew(query: String, page: String = "1", filter: String): List<New>

    fun closeClient()

    companion object {
        fun create(): NewsService {
            return  NewsServiceImpl(client = HttpClient(Android) {
                install(Logging) {
                    level = LogLevel.ALL
                }
                install(JsonFeature) {
                    serializer = KotlinxSerializer()
                }
            })
        }
    }
}
