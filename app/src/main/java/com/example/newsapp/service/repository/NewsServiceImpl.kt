package com.example.newsapp.service.repository

import android.util.Log
import com.example.newsapp.service.model.Constants
import com.example.newsapp.service.model.Constants.BASE_URL
import com.example.newsapp.service.model.New
import com.example.newsapp.service.model.NewResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class NewsServiceImpl: NewsService {
    private val client = HttpClient(CIO) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
            })
        }
    }

    @kotlinx.serialization.ExperimentalSerializationApi
    override suspend fun searchNew(query: String, page: String, filter: String): List<New> {
        val response: HttpResponse = client.get(BASE_URL) {
            url {
                parameters.append("api-key", Constants.API_KEY)
                parameters.append("q", query)
                parameters.append("show-fields", Constants.FIELDS)
                parameters.append("page", page)
                parameters.append("page-size", Constants.PAGE_SIZE)
                parameters.append("order-by", filter)
            }
        }
        try {
            return response.body<NewResponse>().response.results
        } catch (e: Exception) {
            Log.e("Error","$e")
        }
        return emptyList()
    }

    override fun closeClient() {
        client.close()
    }
}
