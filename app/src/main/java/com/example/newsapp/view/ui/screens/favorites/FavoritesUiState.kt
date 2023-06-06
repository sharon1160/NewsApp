package com.example.newsapp.view.ui.screens.favorites

import com.example.newsapp.service.model.New

data class FavoritesUiState(
    val favoritesList: List<New> = emptyList()
)
