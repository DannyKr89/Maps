package ru.dk.maps.ui.map

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.dk.maps.data.LocationState

class MapViewModel : ViewModel() {

    private val _stateflow = MutableStateFlow<LocationState>(LocationState.Loading)
    val stateFlow = _stateflow.asStateFlow()


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
}
