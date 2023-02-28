package com.example.weatherapp.model.roomModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "weatherTable")
data class WeatherModelClass(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "temp") val temp: Int,
    @ColumnInfo(name = "humidity") val humidity: Int,
    @ColumnInfo(name = "name")val name: String,
)
