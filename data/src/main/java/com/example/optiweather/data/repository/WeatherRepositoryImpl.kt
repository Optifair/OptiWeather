package com.example.optiweather.data.repository

import com.example.optiweather.domain.model.CoordinatesData
import com.example.optiweather.domain.model.WeatherData
import com.example.optiweather.domain.repository.WeatherRepository
import com.example.optiweather.data.network.RetrofitClient
import com.example.optiweather.data.network.WeatherApi
import retrofit2.HttpException
import java.io.IOException

class WeatherRepositoryImpl : WeatherRepository {
    private val api: WeatherApi = RetrofitClient.api

    override suspend fun getWeather(coordinatesData: CoordinatesData): Result<WeatherData> {
        return try {
            val currentWeather = api.getCurrentWeather(
                latitude = coordinatesData.latitude,
                longitude = coordinatesData.longitude,
                currentWeather = true
            ).currentWeather
            val weatherData = WeatherData(currentWeather!!.temperature, currentWeather.windspeed)
            Result.success(weatherData)

        } catch (e: HttpException) {
            Result.failure(Exception("HTTP ${e.code()}: ${e.message()}"))

        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}