package com.example.androideksamen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
<<<<<<< HEAD
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
=======
import android.widget.*
import com.example.androideksamen.UserSettings.Settings

class SettingsActivity : AppCompatActivity() {
    // initialize UI obj
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val dietsSelector = findViewById<Spinner>(R.id.diets)
        val prioritySelector = findViewById<Spinner>(R.id.priority)
        val saveBtn = findViewById<Button>(R.id.save_button)
        val dailyIntake = findViewById<EditText>(R.id.dailyintake)
        val maxItems = findViewById<EditText>(R.id.maxitems)
        val dietMaxAmount = findViewById<EditText>(R.id.dietamount)
>>>>>>> b839d6df26e4ab6be90fc3bfc57adc55e0ef310c

        val saveBtn = findViewById<Button>(R.id.button2)

        // attached arrayAdapter to spinner
<<<<<<< HEAD
        spinner2.adapter = createAdapter(UserSettings.dietTypes)
        spinner.adapter = createAdapter(UserSettings.priorities)




        saveBtn.setOnClickListener {
            updateGlobalUserSettings(userSettingsDbInstance)
            Log.i("testGlobal", "Settings after update onclick: " + UserSettings.toString())
        }

        spinner2.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
=======
        dietsSelector.adapter = createAdapter(Settings.dietTypes) // referred to in report
        prioritySelector.adapter = createAdapter(Settings.priorities)
>>>>>>> b839d6df26e4ab6be90fc3bfc57adc55e0ef310c

        //load in settings from global
        dailyIntake.setText(String.format("%.1f", Settings.dailyIntake) + " cal")
        maxItems.setText("${Settings.maxShowItems}")
        dietMaxAmount.setText("${Settings.dietMaxAmount} g")
        dietsSelector.setSelection(Settings.getDietTypeIndex())
        prioritySelector.setSelection(Settings.getPriorityIndex())

<<<<<<< HEAD
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(UserSettings.globalDietType != UserSettings.dietTypes[p2]) {
//                    Settings.globalDietType = Settings.dietTypes[p2]
                    Log.i("Settings are changed", UserSettings.toString())
=======
        //When save, update global vars
        saveBtn.setOnClickListener {
            try {
                Settings.maxShowItems = maxItems.text.toString().toInt()
                val tempIntake: Float? = getFloatFromString(dailyIntake.text.toString())
                val tempDietMax: Int? = getIntFromString(dietMaxAmount.text.toString())
                if (tempIntake != null && tempDietMax != null) {
                    Settings.dailyIntake = tempIntake
                    Settings.dietMaxAmount = tempDietMax
                } else {
                    throw java.lang.NumberFormatException("Some values are invalid")
>>>>>>> b839d6df26e4ab6be90fc3bfc57adc55e0ef310c
                }
                Settings.setDietType(dietsSelector.selectedItemPosition)
                Settings.setPriority(prioritySelector.selectedItemPosition)
                //TODO feedback to user?
                Log.i(this.localClassName, "Settings saved $Settings")
                startActivity(Intent(applicationContext, MainActivity::class.java))
            } catch (e: NumberFormatException) {
                //TODO display errors to user
                Log.w("error occured", "user typed invalid numbers $e")
            }


        }


<<<<<<< HEAD
        }
        Log.i("testGlobal", "Settings before update onclick: " + UserSettings.toString())
=======
>>>>>>> b839d6df26e4ab6be90fc3bfc57adc55e0ef310c
    }

    private fun createAdapter(list: Array<String>): SpinnerAdapter {
        return ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, list
        )
    }

    private fun getFloatFromString(param: String): Float? {
        val pattern = "\\d+(\\.\\d+)?"
        val numberRegex = Regex(pattern)
        val match = numberRegex.find(param)
        val numberString = match?.value
        return numberString?.toFloat()
    }

    private fun getIntFromString(param:String): Int? {
        val pattern = "\\d+"
        val numberRegex = Regex(pattern)
        val match = numberRegex.find(param)
        val numberString = match?.value
        return numberString?.toInt()
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