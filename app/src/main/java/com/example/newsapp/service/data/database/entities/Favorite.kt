package com.example.newsapp.service.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "idFavorite") val idFavorite: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "sectionId") val sectionId: String,
    @ColumnInfo(name = "sectionName") val sectionName: String,
    @ColumnInfo(name = "webPublicationDate") val webPublicationDate: String,
    @ColumnInfo(name = "webTitle") val webTitle: String,
    @ColumnInfo(name = "webUrl") val webUrl: String,
    @ColumnInfo(name = "apiUrl") val apiUrl: String,
    @ColumnInfo(name = "headline") val headline: String,
    @ColumnInfo(name = "trailText") val trailText: String,
    @ColumnInfo(name = "thumbnail") val thumbnail: String,
    @ColumnInfo(name = "bodyText") val bodyText: String,
    @ColumnInfo(name = "isHosted") val isHosted: Boolean,
    @ColumnInfo(name = "pillarId") val pillarId: String,
    @ColumnInfo(name = "pillarName") val pillarName: String
)
