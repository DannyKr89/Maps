package ru.dk.maps.ui.markers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.dk.maps.R
import ru.dk.maps.databinding.FragmentMarkersBinding
import ru.dk.maps.ui.edit.EditMarkerDialogFragment
import ru.dk.maps.ui.map.MapViewModel

class MarkersFragment : Fragment() {

    private var _binding: FragmentMarkersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MapViewModel by activityViewModel()
    private val adapter = MarkersAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMarkersBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()

    }

    private fun initView() {

        binding.apply {
            rvMarkers.adapter = adapter
            adapter.deleteListener = { marker ->
                viewModel.deleteMarker(marker)
            }
            adapter.gotoListener = { marker ->
                findNavController().navigate(
                    R.id.action_fragment_markers_to_fragment_map2,
                    Bundle().apply {
                        putDouble("lat", marker.lat)
                        putDouble("lon", marker.lon)
                    })
            }
            adapter.editListener = { marker ->
                EditMarkerDialogFragment().apply {
                    arguments = Bundle().apply {
                        putString("edit", marker.title)
                    }
                    listener = {
                        marker.title = it
                        viewModel.updateMarker(marker)
                    }
                }.show(parentFragmentManager, null)
            }
        }
    }

    private fun initViewModel() {
        viewModel.markersLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}