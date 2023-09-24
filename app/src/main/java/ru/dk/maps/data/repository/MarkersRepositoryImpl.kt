package ru.dk.maps.data.repository

import ru.dk.maps.data.room.MarkerEntity
import ru.dk.maps.data.room.MarkersRoomDao
import ru.dk.maps.domain.MarkersRepo

class MarkersRepositoryImpl(private val markersRoomDao: MarkersRoomDao) : MarkersRepo {

    override suspend fun getMarkers() = markersRoomDao.getMarkers()

    override suspend fun insertMarker(marker: MarkerEntity) {
        markersRoomDao.insertMarker(marker)
    }

    override suspend fun updateMarker(marker: MarkerEntity) {
        markersRoomDao.updateMarker(marker)
    }

    override suspend fun deleteMarker(marker: MarkerEntity) {
        markersRoomDao.deleteMarker(marker)
    }

}