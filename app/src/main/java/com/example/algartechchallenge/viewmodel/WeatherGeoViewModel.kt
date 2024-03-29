package com.example.algartechchallenge.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.location.IGetLocationGoogleUseCase
import com.example.domain.weather.usecases.IGetWeatherByGeoUseCase
import com.example.utilities.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherGeoViewModel @Inject constructor(
    private val getWeatherUseCase: IGetWeatherByGeoUseCase,
    private val locationGoogleUseCase: IGetLocationGoogleUseCase
) : ViewModel(), LifecycleObserver {

    private val _viewState = MutableLiveData<MainViewState>()
    fun getViewState() = _viewState

    private var locationjob: Job? = null
    private var weatherjob: Job? = null

    fun getWeather(lat: Double, lon: Double, appid: String, query: String) {
        weatherjob?.cancel()
        weatherjob = viewModelScope.launch {
            try {
                getWeatherUseCase.invoke(lat, lon, appid, query)
                    .onStart { _viewState.value = MainViewState.Loading }.catch {
                        _viewState.value =
                            MainViewState.ErrorLoadingItem(it.message ?: "Unknown error")
                    }.cancellable().collectLatest { result ->
                        when (result) {
                            is ResultType.Success -> {
                                _viewState.value =
                                    MainViewState.ItemWeatherSearch(weather = result.data)
                            }

                            is ResultType.Error -> {
                                _viewState.value = MainViewState.ErrorLoadingItem(result.message)
                            }

                            else -> {}
                        }
                    }
            } catch (exception: Exception) {
                _viewState.value =
                    MainViewState.ErrorLoadingItem(exception.message ?: "Unknown error")
            }
        }
    }

    fun getLocations(query: String, apiKey: String) {
        locationjob?.cancel()
        locationjob = viewModelScope.launch {
            locationGoogleUseCase.invoke(query, apiKey)
                .onStart { _viewState.value = MainViewState.Loading }.catch { exception ->
                    _viewState.value =
                        MainViewState.ErrorLoadingItem(exception.message ?: "Unknown error")
                }.cancellable().collectLatest { result ->
                    when (result) {
                        is ResultType.Success -> {
                            _viewState.value =
                                MainViewState.ItemsLocationSearch(location = result.data)
                        }

                        is ResultType.Error -> {
                            _viewState.value = MainViewState.ErrorLoadingItem(result.message)
                        }

                        else -> {}
                    }
                }
        }
    }


}