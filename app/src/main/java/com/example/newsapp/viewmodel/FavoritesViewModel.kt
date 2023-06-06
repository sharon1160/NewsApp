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
            _uiState.update {
                it.copy(favoritesList = favoriteRepository.getAllFavorites())
            }
        }
    }

    fun insert(new: New) {
        viewModelScope.launch {
            favoriteRepository.insert(new)
        }
    }

    fun delete(new: New) {
        viewModelScope.launch {
            favoriteRepository.delete(new)
        }
    }

    fun getAllFavorites(): List<New> {
        viewModelScope.launch {
            _uiState.update {
                it.copy(favoritesList = favoriteRepository.getAllFavorites())
            }
        }
        return uiState.value.favoritesList
    }

    fun deleteAllFavorites() {
        viewModelScope.launch {
            favoriteRepository.deleteAllFavorites()
            _uiState.update {
                it.copy(favoritesList = favoriteRepository.getAllFavorites())
            }
        }
    }
}
