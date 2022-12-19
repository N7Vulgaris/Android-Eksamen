package com.example.androideksamen
//https://stackoverflow.com/questions/52844343/kotlin-set-value-of-global-variable
import android.app.Application
//Class for storing user settings for the application.
//Contains an array of diet types, priorities, and preset initial settings.
// Global variables
//referred to in report
class UserSettings: Application() {
    companion object Settings {
        //Standard options
        val dietTypes = arrayOf("Low Carb", "Low Calorie", "Low Fat", "Atkins")
        val priorities = arrayOf("Breakfast","Lunch","Dinner")

        //Initial settings values
        var dailyIntake: Float = 2750f
        var maxShowItems: Int = 8
        var dietType: String = "Low Carb"
        var dietMaxAmount: Int = 58
        var priority: String = "Dinner"

        fun getDietTypeIndex(): Int {
            return dietTypes.indexOf(dietType)
        }

        fun getPriorityIndex(): Int {
            return priorities.indexOf(priority)
        }

        override fun toString(): String {
            return "Settings{"+ dailyIntake+","+
                    maxShowItems+","+
                    dietType+","+
                    dietMaxAmount+","+
                    priority+"}"
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}