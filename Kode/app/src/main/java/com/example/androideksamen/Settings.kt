package com.example.androideksamen
//https://stackoverflow.com/questions/52844343/kotlin-set-value-of-global-variable
import android.app.Application

// Global variables
class UserSettings: Application() {
    companion object Settings {
        //Standard options
        val dietTypes = arrayOf("Low Carb", "Low Calorie", "Low Fat", "Atkins")
        val priorities = arrayOf("Breakfast","Lunch","Dinner")

        //Initial settings values
        var dailyIntake: Int = 2750
        var maxShowItems: Int = 8
        var dietType: String = "Low Carb"
        var dietMaxAmount: Int = 58
        var priority: String = "Dinner"

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
        // initialization code here
    }
}