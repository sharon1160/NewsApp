package com.example.newsapp.service.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.service.data.database.entities.Favorite

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteMovie: Favorite)

    @Query("DELETE FROM favorites WHERE webTitle = :webTitle")
    suspend fun delete(webTitle: String)

    @Query("SELECT * FROM favorites")
    suspend fun getAllFavorites():List<Favorite>

    @Query("DELETE FROM favorites")
    suspend fun deleteAllFavorites()
}