package ru.dk.maps.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarkerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("title")
    var title: String,
    @ColumnInfo("lat")
    val lat: Double,
    @ColumnInfo("lon")
    val lon: Double
)