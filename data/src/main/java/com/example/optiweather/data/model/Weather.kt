package com.example.optiweather.data.model

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("current_weather")
    val currentWeather: CurrentWeather? = null
)