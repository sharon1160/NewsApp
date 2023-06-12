package com.example.newsapp.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreFilters(private val context: Context) {
    companion object {
        private const val FILTER_NAME = "Filter"
        private const val FILTER_KEY_NAME = "filter"
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(FILTER_NAME)
        val FILTER_KEY = stringPreferencesKey(FILTER_KEY_NAME)
    }

    val getDatastoreFilter: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[FILTER_KEY] ?: ""
        }

    suspend fun saveDatastoreFilter(filter: String) {
        context.dataStore.edit { preferences ->
            preferences[FILTER_KEY] = filter
        }
    }
}
