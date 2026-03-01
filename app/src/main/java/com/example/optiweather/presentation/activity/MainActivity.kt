package com.example.optiweather.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.optiweather.R
import com.example.optiweather.domain.model.CoordinatesData
import com.example.optiweather.presentation.fragment.MapFragment
import com.example.optiweather.presentation.fragment.WeatherFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, WeatherFragment())
            }
        }
    }

    fun showMapPicker(coordinatesData: CoordinatesData) {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, MapFragment.newInstance(coordinatesData))
            addToBackStack(null)
        }
    }
}