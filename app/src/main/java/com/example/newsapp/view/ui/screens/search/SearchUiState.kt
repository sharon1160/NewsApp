package com.example.newsapp.view.ui.screens.search

import com.example.newsapp.service.model.New

data class SearchUiState(
    val newsList: MutableList<New> = mutableListOf(),
)