package com.example.newsapp.service.repository

import androidx.paging.PagingData
import com.example.newsapp.service.model.New
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(query: String) : Flow<PagingData<New>>
}