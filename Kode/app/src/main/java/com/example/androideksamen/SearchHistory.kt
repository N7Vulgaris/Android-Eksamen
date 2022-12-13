package com.example.androideksamen

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SearchHistory")
data class SearchHistory (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "recipeImage") val recipeImage: ByteArray?,
    @ColumnInfo(name = "recipeName") val recipeName: String?,
    @ColumnInfo(name = "recipeMealType") val recipeMealType: String?,
    @ColumnInfo(name = "recipeDietLabels") val recipeDietLabels: ArrayList<String>,
    @ColumnInfo(name = "recipeCalories") val recipeCalories: Float?
)