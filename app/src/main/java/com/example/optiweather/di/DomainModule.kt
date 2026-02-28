package com.example.optiweather.di

import com.example.optiweather.domain.usecase.GetWeatherUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<GetWeatherUseCase> {
        GetWeatherUseCase(weatherRepository = get())
    }

}