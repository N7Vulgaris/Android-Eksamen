package com.example.androideksamen

import android.app.Application
import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// Global variables
//class UserSettings(){
//
//    val dietTypes = arrayOf("Low Carb", "Low Calorie", "Low Fat", "Atkins")
//    val priorities = arrayOf("Breakfast","Lunch","Dinner")
//
//    val dbInstance = Room.databaseBuilder(context, AppDatabase::class.java, "UserSettings").build()
//
//    //Initial settings values
//    var globalDailyIntake: Float = 0f
//    var globalMaxShowItems: Int = 0
//    var globalDietType: String = ""
//    var globalDietMaxAmount: Int = 0
//    var globalMealPriority: String = ""
//
//    fun initializeUserSettings(){
//        var settings: UserSettingsEntity
//        GlobalScope.launch(Dispatchers.IO) {
//            val userSettings = dbInstance.UserSettingsDao().getUserSettings(1)
//            settings = UserSettingsEntity(0, userSettings.dailyIntake, userSettings.maxShowItems, userSettings.dietType, userSettings.dietMaxAmount, userSettings.mealPriority)
//            globalDailyIntake = settings.dailyIntake
//            }
//        }
//
//    override fun toString(): String {
//        return "Settings{"+ globalDailyIntake+","+
//                globalMaxShowItems+","+
//                globalDietType+","+
//                globalDietMaxAmount+","+
//                globalMealPriority+"}"
//    }
//}


//class UserSettingsYa: Application() {
//
////    var dbInstace: AppDatabase = Room.databaseBuilder()
//
//    companion object Settings {
//        //Standard options
//        val dietTypes = arrayOf("Low Carb", "Low Calorie", "Low Fat", "Atkins")
//        val priorities = arrayOf("Breakfast","Lunch","Dinner")
//
//        //Initial settings values
//        var globalDailyIntake: Float = 0f
//        var globalMaxShowItems: Int = 0
//        var globalDietType: String = ""
//        var globalDietMaxAmount: Int = 0
//        var globalMealPriority: String = ""
//
//        override fun toString(): String {
//            return "Settings{"+ globalDailyIntake+","+
//                    globalMaxShowItems+","+
//                    globalDietType+","+
//                    globalDietMaxAmount+","+
//                    globalMealPriority+"}"
//        }
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        // initialization code here
////        var setting: UserSettingsEntity
////
////        GlobalScope.launch(Dispatchers.IO) {
////            val userSettings = dbInstace.UserSettingsDao().getUserSettings(1)
////
//////            setting = UserSettingsEntity(0, userSettings.dailyIntake, userSettings.maxShowItems, userSettings.dietType, userSettings.dietMaxAmount, userSettings.mealPriority)
////            setting = userSettings
////            globalDailyIntake = setting.dailyIntake
////        }
//    }
//}