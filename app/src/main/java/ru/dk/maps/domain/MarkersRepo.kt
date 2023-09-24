package ru.dk.maps.domain

import ru.dk.maps.data.room.MarkerEntity

interface MarkersRepo {

    suspend fun getMarkers(): List<MarkerEntity>
    suspend fun insertMarker(marker: MarkerEntity)
    suspend fun updateMarker(marker: MarkerEntity)
    suspend fun deleteMarker(marker: MarkerEntity)
}