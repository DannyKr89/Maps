package ru.dk.maps.ui.map

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.launch
import ru.dk.maps.R
import ru.dk.maps.data.LocationState
import ru.dk.maps.data.hideKeyboard
import ru.dk.maps.data.toPoint
import ru.dk.maps.databinding.FragmentMapBinding

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MapViewModel by viewModels()
    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var isAddButtonActive = false

    private val placeMarkTapListener = MapObjectTapListener { _, point ->
        Toast.makeText(
            this@MapFragment.requireContext(),
            "Tapped the point (${point.longitude}, ${point.latitude})", Toast.LENGTH_SHORT
        ).show()
        true
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initViewModel()
    }

    private fun showLocation(location: Location?) {
        if (location != null) {
            mapView.map.mapObjects.addPlacemark(
                location.toPoint(),
                ImageProvider.fromResource(requireContext(), R.drawable.ic_my_loc)
            )
            mapView.map.move(
                CameraPosition(location.toPoint(), 18f, 0f, 0f),
                Animation(Animation.Type.SMOOTH, 0.5f),
                null
            )
        }
    }

    private fun initViewModel() {
        viewModel.getMyLocationRequest(fusedLocationClient)

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

                        is LocationState.Success -> showLocation(it.location)
                    }
                }
            }
        }
    }

    private fun initViews() {

        MapKitFactory.initialize(requireContext())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding.apply {
            mapView = map
            mapView.map.isZoomGesturesEnabled = true

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
                context?.let {
                    viewModel.getMyLocationRequest(fusedLocationClient)
                }
            }
            addPlaceMark.setOnClickListener {

                isAddButtonActive = !isAddButtonActive

                if (isAddButtonActive) {
                    showAddLayer(isAddButtonActive)
                } else {
                    showAddLayer(isAddButtonActive)
                }

                btnAccept.setOnClickListener {
                    val imageProvider =
                        ImageProvider.fromResource(requireContext(), R.drawable.icon_point)
                    val marker = mapView.map.mapObjects.addPlacemark(
                        mapView.map.cameraPosition.target,
                        imageProvider
                    )
                    marker.addTapListener(placeMarkTapListener)
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