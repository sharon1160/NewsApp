package com.example.newsapp.data.repository

import androidx.paging.PagingData
import com.example.newsapp.data.model.New
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(query: String, filter: String) : Flow<PagingData<New>>
}
