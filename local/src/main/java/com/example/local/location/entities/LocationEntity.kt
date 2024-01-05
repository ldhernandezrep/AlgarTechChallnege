package com.example.local.location.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "location"
)
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val latitud: Double,
    val longitud: Double,
    val name: String,
)
