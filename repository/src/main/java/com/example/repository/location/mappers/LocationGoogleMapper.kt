package com.example.repository.location.mappers

import com.example.local.location.entities.LocationEntity
import com.example.models.location.LocationModel
import com.example.remote.location.response.Location
import java.util.UUID

fun LocationModel.toEntity() = LocationEntity(
    latitud = this.latitud,
    longitud = this.longitud,
    name = this.name,
    id = UUID.randomUUID().toString()
)

fun LocationEntity.toModel() = LocationModel(
    latitud = this.latitud,
    longitud = this.longitud,
    name = this.name,
)

fun Location.toModel(name: String) = LocationModel(
    latitud = this.lat,
    longitud = this.lng,
    name = name,
)