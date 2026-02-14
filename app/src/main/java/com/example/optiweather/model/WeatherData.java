package com.example.optiweather.model;

import com.google.gson.annotations.SerializedName;

public class WeatherData {

    @SerializedName("current_weather")
    private CurrentWeather currentWeather;

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public static class CurrentWeather {
        private double temperature;
        private double windspeed;

        public double getTemperature() {
            return temperature;
        }

        public double getWindspeed() {
            return windspeed;
        }
    }
}