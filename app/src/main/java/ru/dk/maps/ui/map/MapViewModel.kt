package ru.dk.maps.ui.map

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.dk.maps.data.LocationState
import ru.dk.maps.data.room.MarkerEntity
import ru.dk.maps.domain.MarkersRepo

class MapViewModel(private val markersRepository: MarkersRepo) : ViewModel() {

    private val _stateflow = MutableStateFlow<LocationState>(LocationState.Loading)
    val stateFlow = _stateflow.asStateFlow()

    val markersLiveData = MutableLiveData<List<MarkerEntity>>()


    @SuppressLint("MissingPermission")
    fun getMyLocationRequest(fusedLocationProviderClient: FusedLocationProviderClient) {

        viewModelScope.launch {
            _stateflow.emit(LocationState.Loading)

            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener {
                    if (it != null) {
                        viewModelScope.launch {
                            _stateflow.emit(LocationState.Success(it))
                        }
                    } else {
                        viewModelScope.launch {
                            _stateflow.emit(LocationState.Error(Throwable("Null")))
                        }
                    }
                }
        }
    }

    fun getMarkers() {
        CoroutineScope(Dispatchers.IO).launch {
            markersLiveData.postValue(markersRepository.getMarkers())
        }
    }

    fun insertMarker(marker: MarkerEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            markersRepository.insertMarker(marker)
            getMarkers()
        }
    }

    fun deleteMarker(marker: MarkerEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            markersRepository.deleteMarker(marker)
            getMarkers()
        }
    }

    fun updateMarker(marker: MarkerEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            markersRepository.updateMarker(marker)
            getMarkers()
        }
    }
}
