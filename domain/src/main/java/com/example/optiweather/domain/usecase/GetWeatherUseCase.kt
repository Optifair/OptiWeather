package com.example.optiweather.domain.usecase

import com.example.optiweather.domain.model.CoordinatesData
import com.example.optiweather.domain.model.WeatherData
import com.example.optiweather.domain.repository.WeatherRepository

class GetWeatherUseCase(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(coordinatesData: CoordinatesData): Result<WeatherData> {
        return weatherRepository.getWeather(coordinatesData)
    }
}