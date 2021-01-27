package com.udacity.locationreminder.database.typeconverter

import androidx.room.TypeConverter
import com.udacity.locationreminder.database.entity.Coordinate

class CoordinateTypeConverter {

    companion object {

        @JvmStatic
        @TypeConverter
        fun convertToString(coordinate: Coordinate): String = coordinate.toString()

        @JvmStatic
        @TypeConverter
        fun convertToCoordinate(value: String): Coordinate = Coordinate.fromString(value)
    }
}