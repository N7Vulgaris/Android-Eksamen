package com.example.androideksamen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity(){

    lateinit var userSettingsDbInstance: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        userSettingsDbInstance = Room.databaseBuilder(this, AppDatabase::class.java, "UserSettings").build()

        updateGlobalUserSettings(userSettingsDbInstance)

        // spinner id
        val spinner2 = findViewById<Spinner>(R.id.spinner2)
        val spinner = findViewById<Spinner>(R.id.spinner)

        val saveBtn = findViewById<Button>(R.id.button2)

        // attached arrayAdapter to spinner
        spinner2.adapter = createAdapter(UserSettings.dietTypes)
        spinner.adapter = createAdapter(UserSettings.priorities)




        saveBtn.setOnClickListener {
            updateGlobalUserSettings(userSettingsDbInstance)
            Log.i("testGlobal", "Settings after update onclick: " + UserSettings.toString())
        }

        spinner2.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(UserSettings.globalDietType != UserSettings.dietTypes[p2]) {
//                    Settings.globalDietType = Settings.dietTypes[p2]
                    Log.i("Settings are changed", UserSettings.toString())
                }

            }

        }

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

        }
        Log.i("testGlobal", "Settings before update onclick: " + UserSettings.toString())
    }

    // arrayAdapter
    private fun createAdapter(list: Array<String>): SpinnerAdapter {
        return ArrayAdapter(
            this,android.R.layout.simple_spinner_dropdown_item, list)
    }

    fun updateGlobalUserSettings(dbInstance: AppDatabase){
        GlobalScope.launch(Dispatchers.IO) {
            val newUserSettings = dbInstance.UserSettingsDao().getUserSettings(1)
            UserSettings.globalDailyIntake = newUserSettings.dailyIntake
            UserSettings.globalMaxShowItems = newUserSettings.maxShowItems
            UserSettings.globalDietType = newUserSettings.dietType
            UserSettings.globalDietMaxAmount = newUserSettings.dietMaxAmount
            UserSettings.globalMealPriority = newUserSettings.mealPriority
        }
    }
}