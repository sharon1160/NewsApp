package com.example.newsapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.newsapp.data.model.New
import com.example.newsapp.data.repository.FavoritesRepository
import com.example.newsapp.data.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class SearchViewModel(
    private val newsRepository: NewsRepository,
    favoriteRepository: FavoritesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _paginatedNews = MutableStateFlow<PagingData<New>>(PagingData.empty())
    private val paginatedNews = _paginatedNews.cachedIn(viewModelScope)

    private val favoritesNews =  favoriteRepository.getAllFavorites().flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val resultedList = combine(paginatedNews,favoritesNews) { paginatedNews, favoritesNews ->
        paginatedNews.map { new ->
            if (favoritesNews.contains(new)) {
                new.copy(isFavorite = true)
            } else {
                new
            }
        }
    }

    fun searchNew(query: String, filter: String = DEFAULT_FILTER) {
        newsRepository.getNews(query, filter.lowercase()).onEach { paginatedNews ->
            _paginatedNews.update {
                paginatedNews
            }
        }.launchIn(viewModelScope)
        _uiState.update {
            it.copy(query = query)
        }
    }

    fun updateQuery(query: String) {
        _uiState.update {
            it.copy(query = query)
        }
    }

    companion object {
        const val DEFAULT_FILTER = "Relevance"
    }
}