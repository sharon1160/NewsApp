package com.example.newsapp.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.model.New
import com.example.newsapp.data.repository.FavoritesRepository
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
