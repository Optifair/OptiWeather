package com.example.optiweather.di

import com.example.optiweather.presentation.viewmodel.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<MainViewModel> {
        MainViewModel( getWeatherUseCase = get())
    }

}