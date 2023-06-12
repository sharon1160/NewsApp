package com.example.newsapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newsapp.data.database.dao.FavoriteDao
import com.example.newsapp.data.database.entities.Favorite

@Database(entities = [Favorite::class], version = 1)
abstract class FavoriteDatabase: RoomDatabase() {
    abstract val dao: FavoriteDao
    companion object{
        fun getFavoriteDatabase(context: Context): FavoriteDatabase {
            return Room.databaseBuilder(context, FavoriteDatabase::class.java, "favorites_db").build()
        }
    }
}
