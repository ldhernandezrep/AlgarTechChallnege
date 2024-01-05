package com.example.local.location.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "locations"
)
data class LocationEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val latitud: Double,
    val longitud: Double,
    val name: String,
)
