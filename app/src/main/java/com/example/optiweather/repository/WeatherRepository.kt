package com.example.optiweather.repository;

import androidx.annotation.NonNull;

import com.example.optiweather.model.WeatherData;
import com.example.optiweather.network.RetrofitClient;
import com.example.optiweather.network.WeatherApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {

    private final WeatherApi api;

    public WeatherRepository() {
        api = RetrofitClient.getApi();
    }

    public void getWeather(double lat, double lon, final WeatherCallback callback) {
        Call<WeatherData> call = api.getCurrentWeather(lat, lon, true);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<WeatherData> call, @NonNull Response<WeatherData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherData> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface WeatherCallback {
        void onSuccess(WeatherData weatherData);
        void onError(String error);
    }
}