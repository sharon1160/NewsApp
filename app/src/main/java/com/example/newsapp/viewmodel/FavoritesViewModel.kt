package com.example.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.service.model.New
import com.example.newsapp.service.repository.FavoritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoriteRepository: FavoritesRepository
) : ViewModel() {

    val favoritesNews =  favoriteRepository.getAllFavorites().flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


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
}
