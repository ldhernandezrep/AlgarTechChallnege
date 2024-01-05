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
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocationGoogleRepositoryImpl @Inject constructor(
    val locationDao: LocationDao,
    val remote: PlaceGoogleService
) : LocationGoogleRepositoy {
    override fun getLocationsByName(
        query: String,
        apiKey: String
    ): Flow<ResultType<List<LocationModel>>> =
        flow {
            when (val response = remote.getLocationByQuery(query, apiKey)) {
                is NetworkResult.NetWorkSuccess -> {
                    // Emitir el resultado exitoso al flujo
                    saveLocations(response.result.results.map { it.geometry.location.toModel(it.formattedAddress) })
                    emit(ResultType.Success(data = response.result.results.map {
                        it.geometry.location.toModel(
                            it.formattedAddress
                        )
                    }))
                }

                is NetworkResult.NetworkFailure -> {
                    when (val type = response.networkError.type) {
                        NetworkErrorType.CONNECTION_ERROR -> {
                            // Intentar obtener el clima desde el almacenamiento local
                            locationDao.getLocationByName(query)
                                .catch { emit(ResultType.Error(message = "No hay datos para mostrar.")) }
                                .collect {
                                    emit(ResultType.Success(data = it.map { it.toModel() }))
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

    override suspend fun saveLocations(items: List<LocationModel>): List<Long> =
        locationDao.insertOrIgnoreCategory(items.map { it.toEntity() })
}