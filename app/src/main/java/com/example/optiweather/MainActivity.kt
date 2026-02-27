package com.example.optiweather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.optiweather.viewmodel.WeatherViewModel;

public class MainActivity extends AppCompatActivity {

    private static final int PLACE_PICKER_REQUEST = 1;

    private WeatherViewModel viewModel;

    private TextView tempTextView;
    private TextView windTextView;
    private Button pickLocationButton;
    private TextView selectedLocationTextView;
    private ProgressBar progressBar;

    private double currentLat = 55.75;
    private double currentLon = 37.62;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempTextView = findViewById(R.id.tempTextView);
        windTextView = findViewById(R.id.windTextView);
        pickLocationButton = findViewById(R.id.pickLocationButton);
        selectedLocationTextView = findViewById(R.id.selectedLocationTextView);
        progressBar = findViewById(R.id.progressBar);

        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        viewModel.getWeatherData().observe(this, weatherData -> {
            progressBar.setVisibility(View.GONE);
            if (weatherData != null && weatherData.getCurrentWeather() != null) {
                double temp = weatherData.getCurrentWeather().getTemperature();
                double wind = weatherData.getCurrentWeather().getWindspeed();
                tempTextView.setText(temp + " °C");
                windTextView.setText("Wind: " + wind + " m/s");
            }
        });

        viewModel.getErrorMessage().observe(this, error -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
        });

        pickLocationButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlacePickerActivity.class);
            intent.putExtra(PlacePickerActivity.EXTRA_LATITUDE, currentLat);
            intent.putExtra(PlacePickerActivity.EXTRA_LONGITUDE, currentLon);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        });

        updateLocationText();

        loadWeather(currentLat, currentLon);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                double lat = data.getDoubleExtra(PlacePickerActivity.EXTRA_LATITUDE, currentLat);
                double lon = data.getDoubleExtra(PlacePickerActivity.EXTRA_LONGITUDE, currentLon);
                currentLat = lat;
                currentLon = lon;

                updateLocationText();

                loadWeather(lat, lon);
            }
        }
    }

    private void updateLocationText() {
        String text = String.format("Latitude: %.4f, Longitude: %.4f", currentLat, currentLon);
        selectedLocationTextView.setText(text);
    }

    private void loadWeather(double lat, double lon) {
        progressBar.setVisibility(View.VISIBLE);
        viewModel.loadWeather(lat, lon);
    }
}