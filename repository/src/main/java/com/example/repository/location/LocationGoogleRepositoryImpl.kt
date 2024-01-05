package com.example.repository.location

import com.example.local.location.dao.LocationDao
import com.example.models.location.LocationModel
import com.example.remote.common.NetworkErrorType
import com.example.remote.common.NetworkResult
import com.example.remote.location.PlaceGoogleService
import com.example.repository.location.mappers.toEntity
import com.example.repository.location.mappers.toModel
import com.example.repository.weather.mappers.toModel
import com.example.utilities.ResultType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class LocationGoogleRepositoryImpl @Inject constructor(
    val locationDao: LocationDao,
    val remote: PlaceGoogleService
) : LocationGoogleRepositoy {
    override fun getLocationsByName(
        query: String,
        apiKey: String
    ): Flow<ResultType<List<LocationModel>>> = flow {
        when (val response = remote.getLocationByQuery(query, apiKey)) {
            is NetworkResult.NetWorkSuccess -> {
                // Emitir el resultado exitoso al flujo
                locationDao.insertOrIgnoreCategory(response.result.results.map { it.geometry.location.toModel(it.formattedAddress).toEntity() })

                emitAll(
                    flowOf(ResultType.Success(data = response.result.results.map {
                        it.geometry.location.toModel(it.formattedAddress)
                    }))
                )
            }

            is NetworkResult.NetworkFailure -> {
                when (val type = response.networkError.type) {
                    NetworkErrorType.CONNECTION_ERROR -> {
                        // Solo intentar obtener datos locales si no has obtenido datos del servidor
                        // Intentar obtener el clima desde el almacenamiento local
                        locationDao.getLocationByName(query)
                            .catch { emit(ResultType.Error(message = "No hay datos para mostrar.")) }
                            .collect {
                                emitAll(
                                    flowOf(ResultType.Success(data = it.map { it.toModel() }))
                                )
                            }
                    }

                    else -> {
                        // Si es otro tipo de error
                        emit(ResultType.Error("Error desconocido"))
                    }
                }
            }
        }
    }.flatMapLatest { result ->
        flow {
            emit(result)
        }
    }


    override suspend fun saveLocations(items: List<LocationModel>): List<Long> =
        locationDao.insertOrIgnoreCategory(items.map { it.toEntity() })
}