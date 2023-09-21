package ru.dk.maps

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        MapKitFactory.setApiKey("d6ff1a58-36ea-4ec7-8f19-4ffc6a5096cd")
    }
}