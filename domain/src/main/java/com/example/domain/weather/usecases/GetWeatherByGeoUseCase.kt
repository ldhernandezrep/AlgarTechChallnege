package com.example.domain.weather.usecases

import com.example.models.weather.WeatherModel
import com.example.repository.weather.WeatherRepositoy
import com.example.utilities.ResultType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface IGetWeatherByGeoUseCase{
    suspend fun invoke(lat: Double, lon: Double, appid: String) : Flow<ResultType<WeatherModel>>
}

class GetWeatherByGeoUseCase @Inject constructor (private val weatherRepositoy: WeatherRepositoy) : IGetWeatherByGeoUseCase {
    override suspend fun invoke(lat: Double, lon: Double, appid: String): Flow<ResultType<WeatherModel>> = weatherRepositoy.getWeather(lat,lon, appid)


}