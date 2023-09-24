package ru.dk.maps.data

import android.location.Location

sealed class LocationState {
    data class Success(val location: Location?) : LocationState()
    data class Error(val throwable: Throwable) : LocationState()
    object Loading : LocationState()
}