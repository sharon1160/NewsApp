package com.example.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.service.model.New
import com.example.newsapp.service.repository.FavoritesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoriteRepository: FavoritesRepository
): ViewModel() {

    private val _favoritesNews = MutableStateFlow<List<New>>(emptyList())
    val favoritesNews = _favoritesNews.asStateFlow()

    init {
        viewModelScope.launch {
            updateFavorites()
        }
    }

    fun insert(new: New) {
        viewModelScope.launch {
            favoriteRepository.insert(new)
            updateFavorites()
        }
    }

    fun delete(new: New) {
        viewModelScope.launch {
            favoriteRepository.delete(new)
            updateFavorites()
        }
    }

    private suspend fun updateFavorites() {
        favoriteRepository.getAllFavorites().onEach { favoritesList ->
            _favoritesNews.update {
                favoritesList
            }
        }.launchIn(viewModelScope)
    }
}
