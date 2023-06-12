package com.example.newsapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.data.model.New
import com.example.newsapp.data.repository.NewsService

class NewsPagingSource(
    private val newsService: NewsService,
    private val query: String,
    private val filter: String
) : PagingSource<Int, New>() {

    override fun getRefreshKey(state: PagingState<Int, New>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, New> {
        return try {
            val position = params.key ?: 1
            val responseList = newsService.searchNew(query, position.toString(), filter)
            return LoadResult.Page(
                data = responseList,
                prevKey = null,
                nextKey = if (responseList.isNotEmpty()) position + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
