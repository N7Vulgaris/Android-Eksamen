package com.example.androideksamen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBtn = findViewById<Button>(R.id.search_btn)
        val searchInput = findViewById<EditText>(R.id.search_input)
        var userInput: String
        searchBtn.setOnClickListener{
            userInput = searchInput.text.toString()
            GlobalScope.launch(Dispatchers.Main) {
                downloadRecipes(userInput)
            }
            Log.i("testing", "test user input: "+ userInput)
        }

    }

    suspend fun downloadRecipes(userInput: String): ArrayList<RecipeData>{

        val apiId: String = "2a4625b1"
        val apiKey: String = "ba6e08d3cf44ce2e3e826635508ed960"
        val searchQuery = userInput

        val allData = ArrayList<RecipeData>()
        GlobalScope.async {
            val assetData = URL("https://api.edamam.com/api/recipes/v2?type=public&q=${searchQuery}&app_id=${apiId}&app_key=${apiKey}").readText().toString()
//            Log.i("testing", "testing api: "+assetData)
            val recipeDataArray = (JSONObject(assetData).get("hits") as JSONArray)
            (0 until recipeDataArray.length()).forEach { recipeNr ->
                val dataItem = RecipeData()

                val assetItem = recipeDataArray.get(recipeNr)
                val recipe = (assetItem as JSONObject).get("recipe")
                dataItem.recipeName = (recipe as JSONObject).getString("label")

                allData.add(dataItem)
            }

        }.await()

        allData.forEach { recipe ->
            Log.i("testing", "test: " + recipe.recipeName)
        }
        return allData
    }
}