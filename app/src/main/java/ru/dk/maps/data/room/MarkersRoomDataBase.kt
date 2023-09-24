package ru.dk.maps.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MarkerEntity::class], version = 1, exportSchema = false)
abstract class MarkersRoomDataBase : RoomDatabase() {
    abstract fun markersDao(): MarkersRoomDao
}