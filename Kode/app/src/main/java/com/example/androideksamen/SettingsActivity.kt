package com.example.androideksamen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.get
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

//    lateinit var userSettingsDbInstance: AppDatabase
    lateinit var userSettingsDbInstance: UserSettingsDatabase

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

        userSettingsDbInstance = Room.databaseBuilder(this, UserSettingsDatabase::class.java, "UserSettings").build()
        GlobalScope.launch(Dispatchers.IO) {
            if(userSettingsDbInstance.UserSettingsDao().getAllUserSettings().isNotEmpty())
            updateGlobalUserSettings(userSettingsDbInstance)
        }

//        GlobalScope.launch(Dispatchers.IO) {
//            val newUserSettings = UserSettingsEntity(0, 2500f, 8, "Low-Calories", 50, "Dinner")
//            userSettingsDbInstance.UserSettingsDao().addUserSettings(newUserSettings)
//            userSettingsDbInstance.UserSettingsDao().getUserSettings(1)
//        }


//        saveBtn.setOnClickListener {
//            updateGlobalUserSettings(userSettingsDbInstance)
//            Log.i("testGlobal", "Settings after update onclick: " + UserSettings.toString())
//        }


//            AdapterView.OnItemSelectedListener {
        dietsSelector.adapter =  createAdapter(UserSettings.dietTypes) //referred to in report
        prioritySelector.adapter = createAdapter(UserSettings.priorities)

        //load in settings from global
        dailyIntake.setText(String.format("%.1f", UserSettings.dailyIntake) + " cal")
        maxItems.setText("${UserSettings.maxShowItems}")
        dietMaxAmount.setText("${UserSettings.dietMaxAmount} g")
        dietsSelector.setSelection(UserSettings.getDietTypeIndex())
        prioritySelector.setSelection(UserSettings.getPriorityIndex())

        saveBtn.setOnClickListener {
            try {
//                UserSettings.maxShowItems = maxItems.text.toString().toInt()
                val tempIntake: Float? =
                    getFloatFromString(dailyIntake.text.toString())
                val tempDietMax: Int? =
                    getIntFromString(dietMaxAmount.text.toString())
                if (tempIntake != null && tempDietMax != null) {
//                    UserSettings.dailyIntake = tempIntake
//                    UserSettings.dietMaxAmount = tempDietMax

                } else {
                    throw java.lang.NumberFormatException("Some values are invalid")
                }

                setUserSettingsInDatabase(dailyIntake, maxItems, dietMaxAmount, dietsSelector, prioritySelector)

//                UserSettings.setDietType(dietsSelector.selectedItemPosition)
//                UserSettings.setPriority(prioritySelector.selectedItemPosition)
                //  feedback to user?
                Log.i(this.localClassName, "Settings saved $UserSettings")
                startActivity(Intent(applicationContext, MainActivity::class.java))
            } catch (e: NumberFormatException) {
                //  display errors to user
                Log.w("error occured", "user typed invalid numbers $e")
            }

            // update settings in database

        }
    }

//    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//        if (UserSettings.dietType != UserSettings.dietTypes[p2]) {
////                    Settings.globalDietType = Settings.dietTypes[p2]
//            Log.i("Settings are changed", UserSettings.toString())
//
            //When save, update global vars

//    }

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

    fun updateGlobalUserSettings(dbInstance: UserSettingsDatabase) {
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
                // update existing user settings
//                    val uptatedUserSettings = UserSettingsEntity(
//                        0,
//                        getFloatFromString(dailyIntake.text.toString())!!,
//                        getIntFromString(maxItems.toString())!!,
//                        "diet type - update",
//                        getIntFromString(dietMaxAmount.text.toString())!!,
//                        "meal priority - update"
                updateUserSettingsInDatabase(dailyIntake, maxItems, dietMaxAmount, dietType, mealPriority)

            }
            updateGlobalUserSettings(userSettingsDbInstance)
        }
    }

    fun createNewUserSettingsInDatabase(dailyIntake: EditText, maxItems: EditText, dietMaxAmount: EditText, dietType: Spinner, mealPriority: Spinner){
        // add new user settings
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
}

