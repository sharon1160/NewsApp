package com.example.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.newsapp.service.model.New
import com.example.newsapp.service.repository.NewsService
import com.example.newsapp.view.ui.screens.search.SearchUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val client = NewsService.create()

    fun searchNew(query: String){
        CoroutineScope(Dispatchers.IO).launch {
            val newsList = client.searchNew(query)
            _uiState.update {
                it.copy(newsList = newsList as MutableList<New>)
            }
        }
    }

    fun updateIsFavorite(new: New) {
        val newsList = _uiState.value.newsList
        val index: Int = newsList.indexOf(new)
        val updatedNewsList = newsList.toMutableList().apply {
            this[index] = new.copy(isFavorite = !new.isFavorite)
        }
        _uiState.update {
            it.copy(newsList = updatedNewsList)
        }
    }

    override fun onCleared() {
        super.onCleared()
        client.closeClient()
    }
}