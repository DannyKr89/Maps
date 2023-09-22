package ru.dk.maps.data

import android.app.Activity
import android.content.Context
import android.location.Location
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.yandex.mapkit.geometry.Point

fun Location.toPoint(): Point {
    return Point(this.latitude, this.longitude)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}