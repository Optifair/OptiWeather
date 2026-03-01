package com.example.optiweather.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.optiweather.R
import com.example.optiweather.domain.model.CoordinatesData
import com.example.optiweather.presentation.viewmodel.SharedViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var mapView: MapView
    private lateinit var confirmButton: Button
    private var selectedMarker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Configuration.getInstance().userAgentValue = requireContext().packageName
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = view.findViewById(R.id.mapView)
        confirmButton = view.findViewById(R.id.confirm_button)

        setupMap()
        setupListeners()
    }

    private fun setupMap() {
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        val defaultLat = arguments?.getDouble(EXTRA_LATITUDE, 55.75) ?: 55.75
        val defaultLon = arguments?.getDouble(EXTRA_LONGITUDE, 37.62) ?: 37.62
        val defaultPoint = GeoPoint(defaultLat, defaultLon)

        mapView.controller.setZoom(10.0)
        mapView.controller.setCenter(defaultPoint)

        addMarker(defaultPoint)

        val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), mapView)
        locationOverlay.enableMyLocation()
        mapView.overlays.add(locationOverlay)
    }

    private fun setupListeners() {
        mapView.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                val point = mapView.projection.fromPixels(
                    event.x.toInt(),
                    event.y.toInt()
                ) as GeoPoint?
                point?.let { addMarker(it) }
            }
            false
        }

        confirmButton.setOnClickListener {
            selectedMarker?.let { marker ->
                val position = marker.position
                sharedViewModel.setCoordinates(
                    CoordinatesData(position.latitude, position.longitude)
                )
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun addMarker(point: GeoPoint) {
        selectedMarker?.let { mapView.overlays.remove(it) }

        selectedMarker = Marker(mapView).apply {
            position = point
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            title = "Selected location"
        }

        mapView.overlays.add(selectedMarker)
        mapView.invalidate()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    companion object {
        const val EXTRA_LATITUDE = "extra_latitude"
        const val EXTRA_LONGITUDE = "extra_longitude"

        fun newInstance(coordinatesData: CoordinatesData): MapFragment {
            return MapFragment().apply {
                arguments = Bundle().apply {
                    putDouble(EXTRA_LATITUDE, coordinatesData.latitude)
                    putDouble(EXTRA_LONGITUDE, coordinatesData.longitude)
                }
            }
        }
    }
}