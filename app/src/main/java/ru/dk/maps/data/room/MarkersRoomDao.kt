package ru.dk.maps.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MarkersRoomDao {

    @Query("SELECT * FROM MarkerEntity")
    suspend fun getMarkers(): List<MarkerEntity>

    @Insert()
    suspend fun insertMarker(marker: MarkerEntity)

    @Update()
    suspend fun updateMarker(marker: MarkerEntity)

    @Delete()
    suspend fun deleteMarker(markerEntity: MarkerEntity)
}