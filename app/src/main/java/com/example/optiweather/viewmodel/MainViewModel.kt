package com.example.optiweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.optiweather.model.WeatherData
import com.example.optiweather.repository.WeatherRepository
import com.example.optiweather.repository.WeatherRepository.WeatherCallback

class MainViewModel : ViewModel() {
    private val repository: WeatherRepository = WeatherRepository()
    private val weatherData = MutableLiveData<WeatherData?>()
    private val errorMessage = MutableLiveData<String?>()

    fun getWeatherData(): LiveData<WeatherData?> {
        return weatherData
    }

    fun getErrorMessage(): LiveData<String?> {
        return errorMessage
    }

    fun loadWeather(lat: Double, lon: Double) {
        repository.getWeather(lat, lon, object : WeatherCallback {
            override fun onSuccess(weatherData: WeatherData?) {
                this@MainViewModel.weatherData.postValue(weatherData)
            }

            override fun onError(error: String?) {
                errorMessage.postValue(error)
            }
        })
    }
}