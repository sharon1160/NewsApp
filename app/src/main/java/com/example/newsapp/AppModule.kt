package com.example.newsapp

import com.example.newsapp.service.data.datastore.StoreFilters
import com.example.newsapp.service.repository.*
import com.example.newsapp.viewmodel.FavoritesViewModel
import com.example.newsapp.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::NewsRepositoryImpl) { bind<NewsRepository>() }
    singleOf(::NewsServiceImpl) { bind<NewsService>() }
    single { FavoritesRepository(get()) }
    single { StoreFilters(get()) }
    viewModelOf(::SearchViewModel)
    viewModelOf(::FavoritesViewModel)
}
