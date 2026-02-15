package com.example.optiweather;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class PlacePickerActivity extends AppCompatActivity {

    private MapView mapView;
    private Marker selectedMarker;
    private Button confirmButton;

    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_place_picker);

        mapView = findViewById(R.id.mapView);
        confirmButton = findViewById(R.id.confirm_button);

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(true);

        double defaultLat = getIntent().getDoubleExtra(EXTRA_LATITUDE, 55.75);
        double defaultLon = getIntent().getDoubleExtra(EXTRA_LONGITUDE, 37.62);
        GeoPoint defaultPoint = new GeoPoint(defaultLat, defaultLon);

        mapView.getController().setZoom(10.0);
        mapView.getController().setCenter(defaultPoint);

        addMarker(defaultPoint);

        MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        locationOverlay.enableMyLocation();
        mapView.getOverlays().add(locationOverlay);

        mapView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                GeoPoint point = (GeoPoint) mapView.getProjection().fromPixels(
                        (int) event.getX(),
                        (int) event.getY()
                );
                addMarker(point);
            }
            return false;
        });

        confirmButton.setOnClickListener(v -> {
            if (selectedMarker != null) {
                GeoPoint position = selectedMarker.getPosition();
                Intent resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_LATITUDE, position.getLatitude());
                resultIntent.putExtra(EXTRA_LONGITUDE, position.getLongitude());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void addMarker(GeoPoint point) {
        if (selectedMarker != null) {
            mapView.getOverlays().remove(selectedMarker);
        }

        selectedMarker = new Marker(mapView);
        selectedMarker.setPosition(point);
        selectedMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        selectedMarker.setTitle("Selected location");
        mapView.getOverlays().add(selectedMarker);
        mapView.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}