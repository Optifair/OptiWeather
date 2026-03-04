package com.example.optiweather.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.optiweather.domain.model.CoordinatesData
import com.example.optiweather.domain.model.WeatherData
import com.example.optiweather.domain.usecase.GetWeatherUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherData?>()
    val weatherData: LiveData<WeatherData?> = _weatherData

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getWeather(coordinatesData: CoordinatesData) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = getWeatherUseCase(coordinatesData)

            result.onSuccess { weatherData ->
                _weatherData.value = weatherData
                _errorMessage.value = null
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Unknown error"
                _weatherData.value = null
            }
            _isLoading.value = false
        }
    }
}