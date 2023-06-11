package com.example.newsapp.service.repository

import com.example.newsapp.service.model.New
import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.json

interface NewsService {
    suspend fun searchNew(query: String, page: String = "1", filter: String): List<New>

    fun closeClient()

    companion object {
        fun create(): NewsService {
            return  NewsServiceImpl(client = HttpClient(CIO) {
                install(Logging) {
                    level = LogLevel.ALL
                }
                install(ContentNegotiation) {
                    json(Json {
                        isLenient = true
                    })
                }
            })
        }
    }
}
