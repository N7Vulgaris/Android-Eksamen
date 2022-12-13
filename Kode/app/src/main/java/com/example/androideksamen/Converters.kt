package com.example.androideksamen

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converters {

    @TypeConverter
//    fun listToJson(value: List<String>) = Gson().toJson(value)
    fun fromString(value: String): ArrayList<String>{
        val listType = object: TypeToken<ArrayList<String>>(){}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
//    fun jsonToList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()
    fun fromArrayList(list: ArrayList<String?>): String{
        return Gson().toJson(list)
    }

}

// Converter for list of objects. Remember to change TypeConverters of database
//class ObjectTypeConverter {
//
//    @TypeConverter
//    fun listToJson(value: List<Ojbect>?) = Gson().toJson(value)
//
//    @TypeConverter
//    fun jsonToList(value: String) = Gson().fromJson(value, Array<Object>::class.java).toList()
//
//}