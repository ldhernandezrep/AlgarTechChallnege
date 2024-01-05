package com.example.algartechchallenge.viewmodel

import com.example.models.location.LocationModel
import com.example.models.weather.WeatherModel

sealed class MainViewState {
    object Loading: MainViewState()
    class ErrorLoadingItem(val message: String) : MainViewState()
    class ItemWeatherSearch(val weather: WeatherModel) : MainViewState()
    class ItemsLocationSearch(val location: List<LocationModel>) : MainViewState()
}
