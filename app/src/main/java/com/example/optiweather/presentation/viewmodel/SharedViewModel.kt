package com.example.optiweather.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.optiweather.domain.model.CoordinatesData

class SharedViewModel : ViewModel() {
    private val _selectedCoordinates = MutableLiveData<CoordinatesData>()
    val selectedCoordinates: LiveData<CoordinatesData> = _selectedCoordinates

    fun setCoordinates(coordinatesData: CoordinatesData) {
        _selectedCoordinates.value = coordinatesData
    }
}