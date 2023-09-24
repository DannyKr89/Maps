package ru.dk.maps.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.dk.maps.R
import ru.dk.maps.data.LocationState
import ru.dk.maps.data.hideKeyboard
import ru.dk.maps.data.room.MarkerEntity
import ru.dk.maps.data.toPoint
import ru.dk.maps.databinding.FragmentMapBinding


class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MapViewModel by activityViewModel()
    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var isAddButtonActive = false
    private var myPoint = Point(0.0, 0.0)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.map

        arguments?.let {
            val lat = it.getDouble("lat")
            val lon = it.getDouble("lon")
            moveToMarker(Point(lat, lon))
        }

        initViews()
        initViewModel()

    }

    private fun moveToMarker(point: Point) {
        mapView.map.move(
            CameraPosition(point, 16f, 0f, 0f),
            Animation(Animation.Type.SMOOTH, 0.5f),
            null
        )
    }

    private fun initViewModel() {
        viewModel.getMarkers()
        viewModel.getMyLocationRequest(fusedLocationClient)

        viewModel.markersLiveData.observe(viewLifecycleOwner) { markers ->
            markers.forEach {
                createMarker(it)
            }
        }

        lifecycleScope.launch() {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.stateFlow.collect {
                    when (it) {
                        is LocationState.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "${it.throwable.message}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        LocationState.Loading -> {
                            Toast.makeText(requireContext(), "Get position", Toast.LENGTH_SHORT)
                                .show()
                        }

                        is LocationState.Success -> {
                            val location = it.location?.toPoint()
                            if (location != null) {
                                mapView.map.mapObjects.addPlacemark(
                                    location,
                                    ImageProvider.fromResource(
                                        requireContext(),
                                        R.drawable.ic_my_loc
                                    )
                                )
                                myPoint = location
                            }
                        }
                    }
                }
            }
        }
    }

    private fun createMarker(marker: MarkerEntity) {
        val imageProvider =
            ImageProvider.fromResource(requireContext(), R.drawable.icon_point)

        val point = Point(marker.lat, marker.lon)
        val mark = mapView.map.mapObjects.addPlacemark(point, imageProvider)

        mark.addTapListener { _, _ ->
            Toast.makeText(context, marker.title, Toast.LENGTH_SHORT).show()
            mapView.map.move(
                CameraPosition(point, 16f, 0f, 0f),
                Animation(Animation.Type.SMOOTH, 0.3f),
                null
            )
            true
        }
    }

    private fun initViews() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding.apply {

            mapZoomIn.setOnClickListener {
                val zoom = mapView.map.cameraPosition.zoom + 1
                mapView.map.move(
                    CameraPosition(mapView.map.cameraPosition.target, zoom, 0f, 0f),
                    Animation(Animation.Type.SMOOTH, 0.3f),
                    null
                )
            }

            mapZoomOut.setOnClickListener {
                val zoom = mapView.map.cameraPosition.zoom - 1
                mapView.map.move(
                    CameraPosition(mapView.map.cameraPosition.target, zoom, 0f, 0f),
                    Animation(Animation.Type.SMOOTH, 0.3f),
                    null
                )
            }
            findMe.setOnClickListener {
                moveToMarker(myPoint)
            }
            addPlaceMark.setOnClickListener {

                isAddButtonActive = !isAddButtonActive

                if (isAddButtonActive) {
                    showAddLayer(isAddButtonActive)
                } else {
                    showAddLayer(isAddButtonActive)
                }

                btnAccept.setOnClickListener {
                    val newMarker = MarkerEntity(
                        title = etMarkerTitle.text.toString(),
                        lat = mapView.map.cameraPosition.target.latitude,
                        lon = mapView.map.cameraPosition.target.longitude
                    )
                    createMarker(newMarker)
                    viewModel.insertMarker(newMarker)
                    isAddButtonActive = !isAddButtonActive
                    showAddLayer(isAddButtonActive)
                    etMarkerTitle.text.clear()
                    requireContext().hideKeyboard(it)
                }


            }
        }

    }

    private fun showAddLayer(active: Boolean) {
        binding.apply {
            marker.isVisible = active
            etMarkerTitle.isVisible = active
            btnAccept.isVisible = active
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}