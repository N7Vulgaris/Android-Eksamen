package com.example.androideksamen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    lateinit var userSettingsDbInstance: AppDatabase

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

        // Referred to in report (Reference x)
        userSettingsDbInstance = Room.databaseBuilder(this, AppDatabase::class.java, "AppDatabase").build()
        setInitialUserSettings(userSettingsDbInstance)

        // Referred to in report (Reference x)
        dietsSelector.adapter =  createAdapter(UserSettings.dietTypes)
        prioritySelector.adapter = createAdapter(UserSettings.priorities)

        //load in settings from global
        updateUiElementsFromUserSettings(dailyIntake, maxItems, dietMaxAmount, dietsSelector, prioritySelector)

        // Referred to in report (Reference x)
        saveBtn.setOnClickListener {
            setUserSettingsInDatabase(dailyIntake, maxItems, dietMaxAmount, dietsSelector, prioritySelector)
            startActivity(Intent(applicationContext, MainActivity::class.java))
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

    private fun getIntFromString(param: String): Int? {
        val pattern = "\\d+"
        val numberRegex = Regex(pattern)
        val match = numberRegex.find(param)
        val numberString = match?.value
        return numberString?.toInt()
    }

    fun updateUiElementsFromUserSettings(dailyIntake: EditText, maxItems: EditText, dietMaxAmount: EditText, dietsSelector: Spinner, prioritySelector: Spinner){
        dailyIntake.setText(String.format("%.1f", UserSettings.dailyIntake) + " cal")
        maxItems.setText("${UserSettings.maxShowItems}")
        dietMaxAmount.setText("${UserSettings.dietMaxAmount} g")
        dietsSelector.setSelection(UserSettings.getDietTypeIndex())
        prioritySelector.setSelection(UserSettings.getPriorityIndex())
    }

    fun updateGlobalUserSettings(dbInstance: AppDatabase) {
        val newUserSettings = dbInstance.UserSettingsDao().getUserSettings(1)
        UserSettings.dailyIntake = newUserSettings.dailyIntake
        UserSettings.maxShowItems = newUserSettings.maxShowItems
        UserSettings.dietType = newUserSettings.dietType
        UserSettings.dietMaxAmount = newUserSettings.dietMaxAmount
        UserSettings.priority = newUserSettings.mealPriority
    }

    fun setUserSettingsInDatabase(dailyIntake: EditText, maxItems: EditText, dietMaxAmount: EditText, dietType: Spinner, mealPriority: Spinner){
        GlobalScope.launch(Dispatchers.IO) {
            if (userSettingsDbInstance.UserSettingsDao().getAllUserSettings().isEmpty()) {
                createNewUserSettingsInDatabase(dailyIntake, maxItems, dietMaxAmount, dietType, mealPriority)
            } else {
                updateUserSettingsInDatabase(dailyIntake, maxItems, dietMaxAmount, dietType, mealPriority)
            }
            updateGlobalUserSettings(userSettingsDbInstance)
        }
    }

    fun createNewUserSettingsInDatabase(dailyIntake: EditText, maxItems: EditText, dietMaxAmount: EditText, dietType: Spinner, mealPriority: Spinner){
        val newUserSettings = UserSettingsEntity(
            0,
            getFloatFromString(dailyIntake.text.toString())!!,
            getIntFromString(maxItems.text.toString())!!,
            dietType.selectedItem.toString(),
            getIntFromString(dietMaxAmount.text.toString())!!,
            mealPriority.selectedItem.toString()
        )
        userSettingsDbInstance.UserSettingsDao().addUserSettings(newUserSettings)
    }

    fun updateUserSettingsInDatabase(dailyIntake: EditText, maxItems: EditText, dietMaxAmount: EditText , dietType: Spinner, mealPriority: Spinner){
        userSettingsDbInstance.UserSettingsDao().updateUserSettings(
            getFloatFromString(dailyIntake.text.toString())!!,
            getIntFromString(maxItems.text.toString())!!,
            dietType.selectedItem.toString(),
            getIntFromString(dietMaxAmount.text.toString())!!,
            mealPriority.selectedItem.toString()
        )
    }

    fun setInitialUserSettings(dbInstance: AppDatabase){
        GlobalScope.launch(Dispatchers.IO) {
            if(dbInstance.UserSettingsDao().getAllUserSettings().isNotEmpty())
                updateGlobalUserSettings(dbInstance)
        }
    }
}

