package com.example.androideksamen

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SearchHistoryEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class SearchHistoryDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao

}

@Database(entities = [UserSettingsEntity::class], version = 1)
abstract class UserSettingsDatabase : RoomDatabase() {
    abstract fun UserSettingsDao(): UserSettingsDao
}