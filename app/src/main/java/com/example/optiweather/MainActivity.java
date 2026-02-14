package com.example.optiweather;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.optiweather.viewmodel.WeatherViewModel;

public class MainActivity extends AppCompatActivity {

    private WeatherViewModel viewModel;

    private TextView tempTextView;
    private TextView windTextView;
    private EditText latEditText;
    private EditText lonEditText;

    private Button refreshButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempTextView = findViewById(R.id.tempTextView);
        windTextView = findViewById(R.id.windTextView);
        refreshButton = findViewById(R.id.refreshButton);
        progressBar = findViewById(R.id.progressBar);
        latEditText = findViewById(R.id.latEditText);
        lonEditText = findViewById(R.id.lonEditText);

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

        refreshButton.setOnClickListener(v -> loadWeather());

        loadWeather();
    }

    private void loadWeather() {
        String latStr = latEditText.getText().toString().trim();
        String lonStr = lonEditText.getText().toString().trim();


        if (latStr.isEmpty() || lonStr.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please set latitude and longitude", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double lat = Double.parseDouble(latStr);
            double lon = Double.parseDouble(lonStr);

            progressBar.setVisibility(View.VISIBLE);
            viewModel.loadWeather(lat, lon);

        } catch (NumberFormatException e) {
            Toast.makeText(MainActivity.this, "Incorrect number format", Toast.LENGTH_SHORT).show();
        }
    }
}