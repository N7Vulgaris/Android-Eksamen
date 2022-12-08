package com.example.androideksamen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

// Global variable imports
import com.example.androideksamen.UserSettings.Settings

class MainActivity : AppCompatActivity() {

//    lateinit var allData: ArrayList<RecipeData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var allRecipeData = ArrayList<RecipeData>()
        val searchBtn = findViewById<Button>(R.id.search_btn)
        val searchInput = findViewById<EditText>(R.id.search_input)
        val searchHistoryBtn = findViewById<Button>(R.id.search_history_btn)
        val settingsBtn = findViewById<Button>(R.id.settings_btn)
        val recipeRecyclerView = findViewById<RecyclerView>(R.id.recipeRecyclerView)
        var userInput: String
        //Access to global settings variables here !!
        println(Settings)

        // Button onClick START
        searchBtn.setOnClickListener{
            GlobalScope.launch(Dispatchers.Main) {
                userInput = searchInput.text.toString()
                allRecipeData = downloadRecipes(userInput)
                setAdapter(recipeRecyclerView, allRecipeData)
            }
        }
        searchHistoryBtn.setOnClickListener {
            startActivity(Intent(applicationContext, SearchHistoryActivity::class.java))
        }
        settingsBtn.setOnClickListener {
            startActivity(Intent(applicationContext, SettingsActivity::class.java))
        }
        // Button onclick END

//        GlobalScope.launch(Dispatchers.Main) {
//            allData = downloadRecipes("chicken")
//            recipeRecyclerView.adapter = ItemAdapter(allData)
//        }

    }

    fun setAdapter(view: RecyclerView, data: ArrayList<RecipeData>){
            val adapter = ItemAdapter(data)
            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
            view.layoutManager = layoutManager
            view.itemAnimator = DefaultItemAnimator()
            view.adapter = adapter
    }

    suspend fun downloadRecipes(userInput: String): ArrayList<RecipeData>{

        val apiId: String = "2a4625b1"
        val apiKey: String = "ba6e08d3cf44ce2e3e826635508ed960"
//        val searchQuery = userInput

        val allData = ArrayList<RecipeData>()
        GlobalScope.async {
            val assetData = URL("https://api.edamam.com/api/recipes/v2?type=public&q=${userInput}&app_id=${apiId}&app_key=${apiKey}").readText().toString()
            val recipeDataArray = (JSONObject(assetData).get("hits") as JSONArray)
            (0 until recipeDataArray.length()).forEach { recipeNr ->
                val dataItem = RecipeData()

                val assetItem = recipeDataArray.get(recipeNr)
                val recipe = (assetItem as JSONObject).get("recipe")
                dataItem.recipeName = (recipe as JSONObject).getString("label")
//                dataItem.yield = (recipe as JSONObject).getString("yield")

                allData.add(dataItem)
            }

        }.await()

        allData.forEach { recipe ->
            Log.i("testing", "test: " + recipe.recipeName)
//            Log.i("testing", "test: " + recipe.yield)
        }
        return allData
    }
}