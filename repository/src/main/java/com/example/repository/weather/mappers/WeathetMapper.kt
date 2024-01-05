package com.example.repository.weather.mappers

import com.example.local.weather.entities.WeatherEntity
import com.example.models.weather.WeatherModel
import com.example.remote.weather.response.Root

fun WeatherModel.toEntity() = WeatherEntity(
    main = this.main,
    latitud = this.latitud,
    longitud = this.longitud,
    name = this.name,
    temp = this.temp,
)

fun WeatherEntity.toModel() = WeatherModel(
    main = this.main,
    latitud = this.latitud,
    longitud = this.longitud,
    name = this.name,
    temp = this.temp
)

fun Root.toModel() = WeatherModel(
    main = this.weather[0].main,
    latitud = this.coord.lat,
    longitud = this.coord.lon,
    name = this.name,
    temp = this.main.temp
)