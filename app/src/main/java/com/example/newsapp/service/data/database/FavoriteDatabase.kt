package com.example.newsapp.service.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.service.data.database.dao.FavoriteDao
import com.example.newsapp.service.data.database.entities.Favorite

@Database(entities = [Favorite::class], version = 1)
abstract class FavoriteDatabase: RoomDatabase() {
    abstract val dao: FavoriteDao
}
