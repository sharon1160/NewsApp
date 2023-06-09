package com.example.newsapp.data.repository

import android.content.Context
import com.example.newsapp.data.database.FavoriteDatabase
import com.example.newsapp.data.database.dao.FavoriteDao
import com.example.newsapp.data.database.entities.Favorite
import com.example.newsapp.data.model.Fields
import com.example.newsapp.data.model.New
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepository(
    context: Context
) {
    private val favoriteDao: FavoriteDao = FavoriteDatabase.getFavoriteDatabase(context).dao
    suspend fun insert(new: New) {
        val entity = Favorite(
            idFavorite = new.id,
            type = new.type,
            sectionId = new.sectionId,
            sectionName = new.sectionName,
            webPublicationDate = new.webPublicationDate,
            webTitle = new.webTitle,
            webUrl = new.webUrl,
            apiUrl = new.apiUrl,
            headline = new.fields.headline,
            trailText = new.fields.trailText,
            thumbnail = new.fields.thumbnail,
            bodyText = new.fields.bodyText,
            isHosted = new.isHosted,
            pillarId = new.pillarId,
            pillarName = new.pillarName
        )
        favoriteDao.insert(entity)
    }

    suspend fun delete(new: New) {
        favoriteDao.delete(new.webTitle)
    }

    fun getAllFavorites(): Flow<List<New>> {
        val entities = favoriteDao.getAllFavorites()
        return entities.map { list ->
            list.map {favorite ->
                New(
                    id = favorite.idFavorite,
                    type = favorite.type,
                    sectionId = favorite.sectionId,
                    sectionName = favorite.sectionName,
                    webPublicationDate = favorite.webPublicationDate,
                    webTitle = favorite.webTitle,
                    webUrl = favorite.webUrl,
                    apiUrl = favorite.apiUrl,
                    fields = Fields(
                        headline = favorite.headline,
                        trailText = favorite.trailText,
                        thumbnail = favorite.thumbnail,
                        bodyText = favorite.bodyText
                    ),
                    isHosted = favorite.isHosted,
                    pillarId = favorite.pillarId,
                    pillarName = favorite.pillarName
                )
            }
        }
    }
}
