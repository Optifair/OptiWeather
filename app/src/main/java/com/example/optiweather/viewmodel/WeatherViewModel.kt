package com.example.optiweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.optiweather.model.WeatherData
import com.example.optiweather.repository.WeatherRepository
import com.example.optiweather.repository.WeatherRepository.WeatherCallback

class WeatherViewModel : ViewModel() {
    private val repository: WeatherRepository
    private val weatherData = MutableLiveData<WeatherData?>()
    private val errorMessage = MutableLiveData<String?>()

    init {
        repository = WeatherRepository()
    }

    fun getWeatherData(): LiveData<WeatherData?> {
        return weatherData
    }

    fun getErrorMessage(): LiveData<String?> {
        return errorMessage
    }

    fun loadWeather(lat: Double, lon: Double) {
        repository.getWeather(lat, lon, object : WeatherCallback {
            override fun onSuccess(data: WeatherData?) {
                weatherData.postValue(data)
            }

            override fun onError(error: String?) {
                errorMessage.postValue(error)
            }
        })
    }
}