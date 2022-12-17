package com.example.androideksamen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        // attached arrayAdapter to spinner
        dietsSelector.adapter = createAdapter(Settings.dietTypes) // referred to in report
        prioritySelector.adapter = createAdapter(Settings.priorities)

        //load in settings from global
        dailyIntake.setText(String.format("%.1f", Settings.dailyIntake) + " cal")
        maxItems.setText("${Settings.maxShowItems}")
        dietMaxAmount.setText("${Settings.dietMaxAmount} g")
        dietsSelector.setSelection(Settings.getDietTypeIndex())
        prioritySelector.setSelection(Settings.getPriorityIndex())

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
}