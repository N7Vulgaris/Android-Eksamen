package com.example.androideksamen

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SearchHistory")
data class SearchHistoryEntity (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "recipeImage") val recipeImage: ByteArray?,
    @ColumnInfo(name = "recipeName") val recipeName: String?,
    @ColumnInfo(name = "recipeMealType") val recipeMealType: String?,
    @ColumnInfo(name = "recipeDietLabels") val recipeDietLabels: ArrayList<String>,
    @ColumnInfo(name = "recipeCalories") val recipeCalories: Float,
    @ColumnInfo(name = "recipeIsFavorited")var recipeIsFavorited: Boolean,
    @ColumnInfo(name = "recipeExternalWebsite") var recipeExternalWebsite: String?
)

@Entity(tableName = "UserSettings")
data class UserSettingsEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "dailyIntake") val dailyIntake: Float,
    @ColumnInfo(name = "maxShowItems") val maxShowItems: Int,
    @ColumnInfo(name = "dietType") val dietType: String,
    @ColumnInfo(name = "dietMaxAmount") val dietMaxAmount: Int,
    @ColumnInfo(name = "mealPriority") val mealPriority: String
)