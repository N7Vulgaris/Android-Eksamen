package com.example.androideksamen

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SearchHistoryDao{
    @Insert fun addRecipe(recipe: SearchHistoryEntity)
    @Query("SELECT * FROM SearchHistory") fun getAll(): List<SearchHistoryEntity>
    @Query("UPDATE SearchHistory SET recipeIsFavorited = :isFavorited WHERE recipeName = :name") fun updateFavorited(isFavorited: Boolean, name: String?)
}

// Referred to in report (Reference 16)
@Dao
interface UserSettingsDao{
    @Insert fun addUserSettings(userSettings: UserSettingsEntity)
    @Query("SELECT * FROM UserSettings") fun getAllUserSettings(): List<UserSettingsEntity>
    @Query("SELECT * FROM UserSettings WHERE id = :id") fun getUserSettings(id: Int): UserSettingsEntity
    @Query("UPDATE UserSettings SET dailyIntake = :dailyIntake, maxShowItems = :maxShowItems, " +
            "dietType = :dietType, dietMaxAmount = :dietMaxAmount, " +
            "mealPriority = :mealPriority") fun updateUserSettings(dailyIntake: Float, maxShowItems: Int, dietType: String, dietMaxAmount: Int, mealPriority: String)
}