package com.example.optiweather

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.optiweather.model.WeatherData
import com.example.optiweather.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private var viewModel: MainViewModel? = null

    private var tempTextView: TextView? = null
    private var windTextView: TextView? = null
    private var pickLocationButton: Button? = null
    private var selectedLocationTextView: TextView? = null
    private var progressBar: ProgressBar? = null

    private var currentLat = 55.75
    private var currentLon = 37.62

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tempTextView = findViewById(R.id.tempTextView)
        windTextView = findViewById(R.id.windTextView)
        pickLocationButton = findViewById(R.id.pickLocationButton)
        selectedLocationTextView = findViewById(R.id.selectedLocationTextView)
        progressBar = findViewById(R.id.progressBar)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel!!.getWeatherData().observe(this, Observer { weatherData: WeatherData? ->
            progressBar!!.visibility = View.GONE
            if (weatherData != null && weatherData.currentWeather != null) {
                val temp = weatherData.currentWeather.temperature
                val wind = weatherData.currentWeather.windspeed
                tempTextView!!.text = "$temp °C"
                windTextView!!.text = "Wind: $wind m/s"
            }
        })

        viewModel!!.getErrorMessage().observe(this, Observer { error: String? ->
            progressBar!!.visibility = View.GONE
            Toast.makeText(this@MainActivity, "Error: $error", Toast.LENGTH_SHORT).show()
        })

        pickLocationButton!!.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(this@MainActivity, PlacePickerActivity::class.java)
            intent.putExtra(PlacePickerActivity.EXTRA_LATITUDE, currentLat)
            intent.putExtra(PlacePickerActivity.EXTRA_LONGITUDE, currentLon)
            startActivityForResult(intent, PLACE_PICKER_REQUEST)
        })

        updateLocationText()

        loadWeather(currentLat, currentLon)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                val lat = data.getDoubleExtra(PlacePickerActivity.EXTRA_LATITUDE, currentLat)
                val lon = data.getDoubleExtra(PlacePickerActivity.EXTRA_LONGITUDE, currentLon)
                currentLat = lat
                currentLon = lon

                updateLocationText()

                loadWeather(lat, lon)
            }
        }
    }

    private fun updateLocationText() {
        val text = String.format("Latitude: %.4f, Longitude: %.4f", currentLat, currentLon)
        selectedLocationTextView!!.text = text
    }

    private fun loadWeather(lat: Double, lon: Double) {
        progressBar!!.visibility = View.VISIBLE
        viewModel!!.loadWeather(lat, lon)
    }

    companion object {
        private const val PLACE_PICKER_REQUEST = 1
    }
}