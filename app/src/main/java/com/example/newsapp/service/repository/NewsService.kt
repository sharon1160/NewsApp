package com.example.newsapp.service.repository

import com.example.newsapp.service.model.New

interface NewsService {
    suspend fun searchNew(query: String, page: String = "1", filter: String): List<New>

    fun closeClient()
}
