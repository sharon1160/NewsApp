package com.example.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapp.service.model.New
import com.example.newsapp.service.repository.FavoritesRepository
import com.example.newsapp.service.repository.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel(
    private val newsRepository: NewsRepository,
    private val favoriteRepository: FavoritesRepository
) : ViewModel() {

    private val _paginatedNews = MutableStateFlow<PagingData<New>>(PagingData.empty())
    val paginatedNews = _paginatedNews.cachedIn(viewModelScope)

    private val _favoritesNews = MutableStateFlow<List<New>>(emptyList())
    val favoritesNews = _favoritesNews.asStateFlow()

    fun loadFavorites() {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteRepository.getAllFavorites().onEach { favoritesList ->
                _favoritesNews.update {
                    favoritesList
                }
            }.launchIn(viewModelScope)
        }
    }

    fun searchNew(query: String, filter: String = "relevance"){
        CoroutineScope(Dispatchers.IO).launch {
            newsRepository.getNews(query, filter).onEach { paginatedNews ->
                _paginatedNews.update {
                    paginatedNews
                }
            }.launchIn(viewModelScope)
        }
    }


    fun saveFilter(query: String, newFilter: String) {
        searchNew(query, newFilter.lowercase())
    }
}