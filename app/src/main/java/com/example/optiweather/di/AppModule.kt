package com.example.optiweather.di

import com.example.optiweather.presentation.viewmodel.MainViewModel
import com.example.optiweather.presentation.viewmodel.SharedViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<SharedViewModel> {
        SharedViewModel()
    }

    viewModel<MainViewModel> {
        MainViewModel( getWeatherUseCase = get(), sharedViewModel = get())
    }

}