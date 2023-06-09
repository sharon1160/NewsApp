package com.example.newsapp.data.repository

import android.util.Log
import com.example.newsapp.data.model.New
import com.example.newsapp.data.model.NewResponse
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
                parameters.append("api-key", API_KEY)
                parameters.append("q", query)
                parameters.append("show-fields", FIELDS)
                parameters.append("page", page)
                parameters.append("page-size", PAGE_SIZE)
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

    companion object {
        const val BASE_URL = "https://content.guardianapis.com/search"
        const val API_KEY = "17659ee4-960a-487d-aa50-d45bcb2304f7"
        const val FIELDS =  "headline,trailText,thumbnail,bodyText"
        const val PAGE_SIZE = "20"
    }
}
