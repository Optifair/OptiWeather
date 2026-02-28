package com.example.optiweather.data.repository

import com.example.optiweather.domain.model.CoordinatesData
import com.example.optiweather.domain.model.WeatherData
import com.example.optiweather.domain.repository.WeatherRepository
import com.example.optiweather.data.network.RetrofitClient
import retrofit2.HttpException
import java.io.IOException

class WeatherRepositoryImpl(private val retrofitClient: RetrofitClient) : WeatherRepository {

    override suspend fun getWeather(coordinatesData: CoordinatesData): Result<WeatherData> {
        return try {
            val currentWeather = retrofitClient.api.getCurrentWeather(
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