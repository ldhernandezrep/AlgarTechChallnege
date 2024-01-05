package com.example.repository.weather

import com.example.local.weather.dao.WeatherDao
import com.example.models.weather.WeatherModel
import com.example.remote.common.NetworkErrorType
import com.example.remote.common.NetworkResult
import com.example.remote.weather.WeathetService
import com.example.repository.common.ResultType
import com.example.repository.weather.mappers.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImplement @Inject constructor(
    val weatherLocal: WeatherDao,
    val weathetService: WeathetService
) : WeatherRepositoy {

    override fun getWeather(latitud: Double, longitud: Double): Flow<ResultType<WeatherModel>> =
        flow {
            when (val response = weathetService.getWeatherByLatAndLon(latitud, longitud)) {
                is NetworkResult.NetWorkSuccess -> {
                    // Emitir el resultado exitoso al flujo
                    emit(ResultType.Success(data = response.result.toModel()))
                }

                is NetworkResult.NetworkFailure -> {
                    when (val type = response.networkError.type) {
                        NetworkErrorType.CONNECTION_ERROR -> {
                            // Intentar obtener el clima desde el almacenamiento local
                            weatherLocal.getWeatherByLatAndLon(latitud, longitud)
                                .catch { emit(ResultType.Error(message = "No hay datos para mostrar.")) }
                                .collect {
                                    emit(ResultType.Success(data = it.toModel()))
                                }
                        }

                        else -> {
                            // Emitir un error de red al flujo
                            emit(ResultType.Error("Error de red: ${type.name}"))
                        }
                    }
                }
            }

        }
}