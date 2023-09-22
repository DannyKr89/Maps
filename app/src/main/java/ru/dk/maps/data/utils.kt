package ru.dk.maps.data

import android.location.Location
import com.yandex.mapkit.geometry.Point

fun Location.toPoint(): Point {
    return Point(this.latitude, this.longitude)
}