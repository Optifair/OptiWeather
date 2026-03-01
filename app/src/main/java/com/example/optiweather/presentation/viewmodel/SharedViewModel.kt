package com.example.optiweather.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.optiweather.domain.model.CoordinatesData
import com.example.optiweather.domain.model.WeatherData

class SharedViewModel : ViewModel() {
    private val _selectedCoordinates = MutableLiveData<CoordinatesData>()
    val selectedCoordinates: LiveData<CoordinatesData> = _selectedCoordinates

    private val _weatherData = MutableLiveData<WeatherData?>()
    val weatherData: LiveData<WeatherData?> = _weatherData

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun setCoordinates(coordinatesData: CoordinatesData) {
        _selectedCoordinates.value = coordinatesData
    }

    fun setWeatherData(weatherData: WeatherData?) {
        _weatherData.value = weatherData
    }

    fun setErrorMessage(error: String?) {
        _errorMessage.value = error
    }
}