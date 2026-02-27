package com.example.optiweather.repository

import com.example.optiweather.model.WeatherData
import com.example.optiweather.network.RetrofitClient
import com.example.optiweather.network.WeatherApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepository {
    private val api: WeatherApi = RetrofitClient.api

    fun getWeather(lat: Double, lon: Double, callback: WeatherCallback) {
        val call = api.getCurrentWeather(lat, lon, true)
        call!!.enqueue(object : Callback<WeatherData?> {
            override fun onResponse(call: Call<WeatherData?>, response: Response<WeatherData?>) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body())
                } else {
                    callback.onError("Response error: " + response.code())
                }
            }

            override fun onFailure(call: Call<WeatherData?>, t: Throwable) {
                callback.onError(t.message)
            }
        })
    }

    interface WeatherCallback {
        fun onSuccess(weatherData: WeatherData?)
        fun onError(error: String?)
    }
}