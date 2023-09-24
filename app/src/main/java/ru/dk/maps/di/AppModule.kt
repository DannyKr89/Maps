package ru.dk.maps.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.dk.maps.App
import ru.dk.maps.data.repository.MarkersRepositoryImpl
import ru.dk.maps.data.room.MarkersRoomDao
import ru.dk.maps.data.room.MarkersRoomDataBase
import ru.dk.maps.domain.MarkersRepo
import ru.dk.maps.ui.map.MapViewModel

val appModule = module {
    single {
        androidApplication().applicationContext as App
    }
    single<MarkersRoomDataBase> {
        Room.databaseBuilder(
            androidApplication(),
            MarkersRoomDataBase::class.java,
            "markersDB"
        ).build()
    }
    single<MarkersRoomDao> { get<MarkersRoomDataBase>().markersDao() }
    single<MarkersRepo> { MarkersRepositoryImpl(get()) }
    viewModel { MapViewModel(get()) }
}