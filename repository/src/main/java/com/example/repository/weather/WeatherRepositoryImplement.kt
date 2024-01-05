package com.example.repository.weather

import com.example.local.weather.dao.WeatherDao
import com.example.local.weather.entities.WeatherEntity
import com.example.models.weather.WeatherModel
import com.example.remote.common.NetworkErrorType
import com.example.remote.common.NetworkResult
import com.example.remote.weather.WeathetService
import com.example.repository.location.mappers.toEntity
import com.example.repository.weather.mappers.toEntity
import com.example.repository.weather.mappers.toModel
import com.example.repository.weather.utils.toThreeDigits
import com.example.utilities.ResultType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImplement @Inject constructor(
    val weatherLocal: WeatherDao,
    val weathetService: WeathetService
) : WeatherRepositoy {

    override fun getWeather(latitud: Double, longitud: Double, appid: String): Flow<ResultType<WeatherModel>> =
        flow {
            when (val response = weathetService.getWeatherByLatAndLon(latitud, longitud, appid)) {
                is NetworkResult.NetWorkSuccess -> {
                    saveWeather(response.result.toModel())
                    emit(ResultType.Success(data = response.result.toModel()))
                }

                is NetworkResult.NetworkFailure -> {
                    when (val type = response.networkError.type) {
                        NetworkErrorType.CONNECTION_ERROR -> {
                            weatherLocal.getWeatherByLatAndLon(latitud.toThreeDigits(), longitud.toThreeDigits())
                                .catch { emit(ResultType.Error(message = "No hay datos para mostrar.")) }
                                .collect {
                                    emit(ResultType.Success(data = it.toModel()))
                                }
                        }

                        else -> {
                            emit(ResultType.Error("Error de red: ${type.name}"))
                        }
                    }
                }
            }

        }

    override suspend fun saveWeather(item: WeatherModel): Long = weatherLocal.insertOrIgnoreWheater(item.toEntity())
}
