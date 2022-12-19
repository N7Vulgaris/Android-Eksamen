package com.example.androideksamen

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// Referred to in report (Reference 19)
@Database(entities = [SearchHistoryEntity::class, UserSettingsEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun UserSettingsDao(): UserSettingsDao
}