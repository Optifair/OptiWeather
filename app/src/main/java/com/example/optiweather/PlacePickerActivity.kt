package com.example.optiweather

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class PlacePickerActivity : AppCompatActivity() {
    private var mapView: MapView? = null
    private var selectedMarker: Marker? = null
    private var confirmButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().userAgentValue = packageName

        setContentView(R.layout.activity_place_picker)

        mapView = findViewById<MapView>(R.id.mapView)
        confirmButton = findViewById<Button>(R.id.confirm_button)

        mapView!!.setTileSource(TileSourceFactory.MAPNIK)
        mapView!!.setMultiTouchControls(true)
        mapView!!.setBuiltInZoomControls(true)

        val defaultLat = intent.getDoubleExtra(EXTRA_LATITUDE, 55.75)
        val defaultLon = intent.getDoubleExtra(EXTRA_LONGITUDE, 37.62)
        val defaultPoint = GeoPoint(defaultLat, defaultLon)

        mapView!!.controller.setZoom(10.0)
        mapView!!.controller.setCenter(defaultPoint)

        addMarker(defaultPoint)

        val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), mapView)
        locationOverlay.enableMyLocation()
        mapView!!.overlays.add(locationOverlay)

        mapView!!.setOnTouchListener { v: View?, event: MotionEvent? ->
            if (event!!.action == MotionEvent.ACTION_UP) {
                val point = mapView!!.getProjection().fromPixels(
                    event.x.toInt(),
                    event.y.toInt()
                ) as GeoPoint?
                addMarker(point)
            }
            false
        }

        confirmButton!!.setOnClickListener(View.OnClickListener { v: View? ->
            if (selectedMarker != null) {
                val position = selectedMarker!!.position
                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_LATITUDE, position.latitude)
                resultIntent.putExtra(EXTRA_LONGITUDE, position.longitude)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        })
    }

    private fun addMarker(point: GeoPoint?) {
        if (selectedMarker != null) {
            mapView!!.overlays.remove(selectedMarker)
        }

        selectedMarker = Marker(mapView)
        selectedMarker!!.setPosition(point)
        selectedMarker!!.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        selectedMarker!!.title = "Selected location"
        mapView!!.overlays.add(selectedMarker)
        mapView!!.invalidate()
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    companion object {
        const val EXTRA_LATITUDE: String = "extra_latitude"
        const val EXTRA_LONGITUDE: String = "extra_longitude"
    }
}