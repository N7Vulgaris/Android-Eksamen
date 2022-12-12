package com.example.androideksamen

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SearchHistory")
class SearchHistory: AbstractRecipeData() {

    @PrimaryKey(autoGenerate = true)var id = 0
}