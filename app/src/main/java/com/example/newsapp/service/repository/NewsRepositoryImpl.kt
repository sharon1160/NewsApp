package com.example.newsapp.service.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.newsapp.service.model.New
import com.example.newsapp.service.paging.NewsPagingSource
import kotlinx.coroutines.flow.Flow

class NewsRepositoryImpl(private val newsService: NewsService): NewsRepository {
    override fun getNews(query: String): Flow<PagingData<New>> = Pager(
        initialKey = null,
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
            prefetchDistance = 1
        ),
        pagingSourceFactory = {
            NewsPagingSource(
                newsService,
                query
            )
        }
    ).flow
}