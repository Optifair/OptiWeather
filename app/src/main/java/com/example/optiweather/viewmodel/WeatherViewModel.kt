package com.example.optiweather.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.optiweather.model.WeatherData;
import com.example.optiweather.repository.WeatherRepository;

public class WeatherViewModel extends ViewModel {

    private final WeatherRepository repository;
    private final MutableLiveData<WeatherData> weatherData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public WeatherViewModel() {
        repository = new WeatherRepository();
    }

    public LiveData<WeatherData> getWeatherData() {
        return weatherData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadWeather(double lat, double lon) {
        repository.getWeather(lat, lon, new WeatherRepository.WeatherCallback() {
            @Override
            public void onSuccess(WeatherData data) {
                weatherData.postValue(data);
            }

            @Override
            public void onError(String error) {
                errorMessage.postValue(error);
            }
        });
    }
}