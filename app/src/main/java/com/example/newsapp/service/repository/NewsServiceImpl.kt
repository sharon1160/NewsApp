package com.example.newsapp.service.repository

import android.util.Log
import com.example.newsapp.service.model.Constants
import com.example.newsapp.service.model.New
import com.example.newsapp.service.model.NewResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class NewsServiceImpl(private val client: HttpClient): NewsService {

    @kotlinx.serialization.ExperimentalSerializationApi
    override suspend fun searchNew(query: String, page: String, filter: String): List<New> {
        val response = client.get<String> {
            url {
                protocol = URLProtocol.HTTPS
                host = Constants.HOST
                encodedPath = "/search"
                parameters.append("api-key", Constants.API_KEY)
                parameters.append("q", query)
                parameters.append("show-fields", Constants.FIELDS)
                parameters.append("page", page)
                parameters.append("page-size", Constants.PAGE_SIZE)
                parameters.append("order-by", filter)
            }
        }
        try {
            val apiResponse = Json.decodeFromString<NewResponse>(response)
            return apiResponse.response.results
        } catch (e: Exception) {
            Log.e("Error","$e")
        }
        return emptyList()
    }

    override fun closeClient() {
        client.close()
    }
}
