package com.example.optiweather.model

import com.google.gson.annotations.SerializedName

class WeatherData {
    @JvmField
    @SerializedName("current_weather")
    val currentWeather: CurrentWeather? = null

    class CurrentWeather {
        @JvmField
        val temperature: Double = 0.0
        @JvmField
        val windspeed: Double = 0.0
    }
}