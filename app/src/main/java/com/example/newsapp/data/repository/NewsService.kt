package com.example.newsapp.data.repository

import com.example.newsapp.data.model.New

interface NewsService {
    suspend fun searchNew(query: String, page: String = "1", filter: String): List<New>

    fun closeClient()
}
