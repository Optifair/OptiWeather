package com.example.optiweather.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.optiweather.R
import com.example.optiweather.domain.model.CoordinatesData
import com.example.optiweather.presentation.activity.MainActivity
import com.example.optiweather.presentation.viewmodel.MainViewModel
import com.example.optiweather.presentation.viewmodel.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherFragment : Fragment() {
    private val mainViewModel: MainViewModel by viewModel()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var tempTextView: TextView
    private lateinit var windTextView: TextView
    private lateinit var pickLocationButton: Button
    private lateinit var selectedLocationTextView: TextView
    private lateinit var progressBar: ProgressBar

    private var currentLat = 55.75
    private var currentLon = 37.62

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tempTextView = view.findViewById(R.id.tempTextView)
        windTextView = view.findViewById(R.id.windTextView)
        pickLocationButton = view.findViewById(R.id.pickLocationButton)
        selectedLocationTextView = view.findViewById(R.id.selectedLocationTextView)
        progressBar = view.findViewById(R.id.progressBar)

        setupObservers()
        setupListeners()

        updateLocationText()
        loadWeather()
    }

    private fun setupObservers() {
        sharedViewModel.weatherData.observe(viewLifecycleOwner, Observer { weatherData ->
            progressBar.visibility = View.GONE
            if (weatherData != null) {
                tempTextView.text = "${weatherData.temperature} °C"
                windTextView.text = "Wind: ${weatherData.windSpeed} m/s"
            }
        })

        sharedViewModel.errorMessage.observe(viewLifecycleOwner, Observer { error ->
            progressBar.visibility = View.GONE
            if (error != null) {
                Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_SHORT).show()
            }
        })

        sharedViewModel.selectedCoordinates.observe(viewLifecycleOwner, Observer { coordinates ->
            coordinates?.let {
                currentLat = it.latitude
                currentLon = it.longitude
                updateLocationText()
                loadWeather()
            }
        })
    }

    private fun setupListeners() {
        pickLocationButton.setOnClickListener {
            (requireActivity() as MainActivity).showMapPicker()
        }
    }

    private fun updateLocationText() {
        val text = String.format("Latitude: %.4f, Longitude: %.4f", currentLat, currentLon)
        selectedLocationTextView.text = text
    }

    private fun loadWeather() {
        progressBar.visibility = View.VISIBLE
        val coordinates = CoordinatesData(currentLat, currentLon)
        mainViewModel.getWeather(coordinates)
    }
}