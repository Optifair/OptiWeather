package com.example.optiweather.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.optiweather.domain.model.CoordinatesData
import com.example.optiweather.domain.usecase.GetWeatherUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val sharedViewModel: SharedViewModel
) : ViewModel() {

    fun getWeather(coordinatesData: CoordinatesData) {
        viewModelScope.launch {
            val result = getWeatherUseCase(coordinatesData)

            result.onSuccess { weatherData ->
                sharedViewModel.setWeatherData(weatherData)
            }.onFailure { error ->
                sharedViewModel.setErrorMessage(error.message ?: "Unknown error")
            }
        }
    }
}