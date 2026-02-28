package com.example.optiweather.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.optiweather.R
import com.example.optiweather.domain.model.CoordinatesData
import com.example.optiweather.domain.model.WeatherData
import com.example.optiweather.presentation.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val vm by viewModel<MainViewModel>()

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

        vm.getWeatherData().observe(this, Observer { weatherData: WeatherData? ->
            progressBar!!.visibility = View.GONE
            if (weatherData != null) {
                val temp = weatherData.temperature
                val wind = weatherData.windSpeed
                tempTextView!!.text = "$temp °C"
                windTextView!!.text = "Wind: $wind m/s"
            }
        })

        vm.getErrorMessage().observe(this, Observer { error: String? ->
            progressBar!!.visibility = View.GONE
            Toast.makeText(this@MainActivity, "Error: $error", Toast.LENGTH_SHORT).show()
        })

        pickLocationButton!!.setOnClickListener { v: View? ->
            val intent = Intent(this@MainActivity, PlacePickerActivity::class.java)
            intent.putExtra(PlacePickerActivity.Companion.EXTRA_LATITUDE, currentLat)
            intent.putExtra(PlacePickerActivity.Companion.EXTRA_LONGITUDE, currentLon)
            startActivityForResult(intent, PLACE_PICKER_REQUEST)
        }

        updateLocationText()

        val coordinates = CoordinatesData(currentLat, currentLon)
        getWeather(coordinates)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                currentLat = data.getDoubleExtra(PlacePickerActivity.Companion.EXTRA_LATITUDE, currentLat)
                currentLon = data.getDoubleExtra(PlacePickerActivity.Companion.EXTRA_LONGITUDE, currentLon)
                val coordinates = CoordinatesData(currentLat, currentLon)

                updateLocationText()

                getWeather(coordinates)
            }
        }
    }

    private fun updateLocationText() {
        val text = String.format("Latitude: %.4f, Longitude: %.4f", currentLat, currentLon)
        selectedLocationTextView!!.text = text
    }

    private fun getWeather(coordinatesData: CoordinatesData) {
        progressBar!!.visibility = View.VISIBLE
        vm.getWeather(coordinatesData)
    }

    companion object {
        private const val PLACE_PICKER_REQUEST = 1
    }
}