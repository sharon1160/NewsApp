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
    // private val _uiState = MutableStateFlow(SearchUiState())
    // val uiState = _uiState.asStateFlow()

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

    fun searchNew(query: String){
        CoroutineScope(Dispatchers.IO).launch {
            newsRepository.getNews(query).onEach { paginatedNews ->
                _paginatedNews.update {
                    paginatedNews
                }
            }.launchIn(viewModelScope)
        }
    }
    /*
    fun updateIsFavorite(new: New) {
        val newsList = _uiState.value.newsList
        val index: Int = newsList.indexOf(new)
        val updatedNewsList = newsList.toMutableList().apply {
            this[index] = new.copy(isFavorite = !new.isFavorite)
        }
        _uiState.update {
            it.copy(newsList = updatedNewsList)
        }
    }*/
    /*
    override fun onCleared() {
        super.onCleared()
        client.closeClient()
    }*/
}