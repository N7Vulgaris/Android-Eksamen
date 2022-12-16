package com.example.androideksamen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import com.example.androideksamen.UserSettings.Settings

class SettingsActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // spinner id
        val spinner2 = findViewById<Spinner>(R.id.spinner2)
        val spinner = findViewById<Spinner>(R.id.spinner)

        // attached arrayAdapter to spinner
        spinner2.adapter = createAdapter(Settings.dietTypes) // referred to in report
        spinner.adapter = createAdapter(Settings.priorities)

        spinner2.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(Settings.dietType != Settings.dietTypes[p2]) {
                    Settings.dietType = Settings.dietTypes[p2]
                    Log.i("Settings are changed", Settings.toString())
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
    }

    // arrayAdapter
    private fun createAdapter(list: Array<String>): SpinnerAdapter {
        return ArrayAdapter(
            this,android.R.layout.simple_spinner_dropdown_item, list)
    }
}