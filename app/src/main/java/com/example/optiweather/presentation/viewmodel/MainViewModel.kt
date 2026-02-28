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
    private val weatherData = MutableLiveData<WeatherData?>()
    private val errorMessage = MutableLiveData<String?>()

    fun getWeatherData(): LiveData<WeatherData?> {
        return weatherData
    }

    fun getErrorMessage(): LiveData<String?> {
        return errorMessage
    }

    fun getWeather(coordinatesData: CoordinatesData) {
        viewModelScope.launch {
            val result = getWeatherUseCase(coordinatesData)

            result.onSuccess { weatherData ->
                this@MainViewModel.weatherData.postValue(weatherData)
            }.onFailure { error ->
                errorMessage.postValue(error.message ?: "Unknown error")
            }
        }
    }
}