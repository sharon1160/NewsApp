package com.example.newsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.newsapp.data.model.New
import com.example.newsapp.data.paging.NewsPagingSource
import kotlinx.coroutines.flow.Flow

class NewsRepositoryImpl(private val newsService: NewsService): NewsRepository {
    override fun getNews(query: String, filter: String): Flow<PagingData<New>> = Pager(
        initialKey = null,
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
            prefetchDistance = 1
        ),
        pagingSourceFactory = {
            NewsPagingSource(
                newsService,
                query,
                filter
            )
        }
    ).flow
}