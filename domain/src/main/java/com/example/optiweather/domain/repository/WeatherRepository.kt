package com.example.optiweather.domain.repository

import com.example.optiweather.domain.model.CoordinatesData
import com.example.optiweather.domain.model.WeatherData

interface WeatherRepository {
    suspend fun getWeather(coordinatesData: CoordinatesData): Result<WeatherData>
}