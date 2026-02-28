package com.example.optiweather.di

import com.example.optiweather.data.repository.WeatherRepositoryImpl
import com.example.optiweather.domain.repository.WeatherRepository
import org.koin.dsl.module

val dataModule = module {

    single<WeatherRepository> {
        WeatherRepositoryImpl()
    }

}