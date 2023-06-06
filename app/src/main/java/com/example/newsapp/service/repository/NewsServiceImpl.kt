package com.example.newsapp.service.repository

import com.example.newsapp.service.model.Constants
import com.example.newsapp.service.model.New
import com.example.newsapp.service.model.NewResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class NewsServiceImpl(private val client: HttpClient): NewsService {

    override suspend fun searchNew(query: String): List<New> {
        val response = client.get<String> {
            url {
                protocol = URLProtocol.HTTPS
                host = Constants.HOST
                encodedPath = "/search"
                parameters.append("api-key", Constants.API_KEY)
                parameters.append("q", query)
                parameters.append("show-fields", Constants.FIELDS)
            }
        }
        val apiResponse = Json.decodeFromString<NewResponse>(response)
        return apiResponse.response.results
    }

    override fun closeClient() {
        client.close()
    }
}
