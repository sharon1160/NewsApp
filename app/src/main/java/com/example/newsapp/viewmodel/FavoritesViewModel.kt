package com.example.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.service.model.New
import com.example.newsapp.service.repository.FavoritesRepository
import com.example.newsapp.view.ui.screens.favorites.FavoritesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoriteRepository: FavoritesRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState = _uiState.asStateFlow()

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
        _uiState.update {
            it.copy(favoritesList =  favoriteRepository.getAllFavorites() as MutableList<New>)
        }
    }
}
