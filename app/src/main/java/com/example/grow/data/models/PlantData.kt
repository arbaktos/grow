package com.example.grow.data.models

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.grow.data.Converter


@Entity(tableName = "plant_table")
data class PlantData(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String = "",
    var watering: Int = 1,
    var caredays: String = "",
    var additionalcare: String = "",
    var plantPicture: Bitmap?
)
