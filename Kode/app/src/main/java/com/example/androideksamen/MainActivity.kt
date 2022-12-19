package com.example.androideksamen

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

// Global variable imports
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    lateinit var searchHistoryDbInstance: AppDatabase
    lateinit var allRecipeData: ArrayList<RecipeData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBtn = findViewById<Button>(R.id.search_btn)
        val searchInput = findViewById<EditText>(R.id.search_input)
        val searchHistoryBtn = findViewById<Button>(R.id.search_history_btn)
        val settingsBtn = findViewById<Button>(R.id.settings_btn)
        val recipeRecyclerView = findViewById<RecyclerView>(R.id.recipeRecyclerView)
        var userInput: String

        searchHistoryBtn.setOnClickListener {
            startActivity(Intent(applicationContext, SearchHistoryActivity::class.java))
        }
        settingsBtn.setOnClickListener {
            startActivity(Intent(applicationContext, SettingsActivity::class.java))
        }

        searchBtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {

                userInput = searchInput.text.toString()
                if (userInput != "") {

                    allRecipeData = downloadRecipes(userInput)
                    sortRecipeDataByMealPriority(allRecipeData)
                    addRecipeListToSearchHistoryDatabase(allRecipeData)
                    setAdapter(recipeRecyclerView, allRecipeData, searchHistoryDbInstance)
                }
            }
        }
    }

    fun addRecipeListToSearchHistoryDatabase(recipeDataList: ArrayList<RecipeData>) {

        // Referred to in report (Reference x)
        searchHistoryDbInstance = Room.databaseBuilder(this, AppDatabase::class.java, "AppDatabase").build()

        GlobalScope.launch(Dispatchers.IO) {
            recipeDataList.forEach { searchHistory ->

                // Encode Bitmap image to ByteArray
                val stream = ByteArrayOutputStream()
                val searchHistoryImage = searchHistory.recipeImage
                searchHistoryImage?.compress(Bitmap.CompressFormat.PNG, 90, stream)
                val imageByteArray = stream.toByteArray()

                val newSearchHistoryItem = SearchHistoryEntity(0,
                imageByteArray,
                searchHistory.recipeName,
                searchHistory.recipeMealType,
                searchHistory.recipeDietLabels,
                searchHistory.recipeCalories,
                searchHistory.recipeIsFavorited,
                searchHistory.recipeExternalWebsite
                )
                searchHistoryDbInstance.searchHistoryDao().addRecipe(newSearchHistoryItem)

            }

        }
    }

    fun setAdapter(view: RecyclerView, data: ArrayList<RecipeData>, dbInstance: AppDatabase) {
        val adapter = RecipeRowAdapter(data, dbInstance)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        view.layoutManager = layoutManager
        view.itemAnimator = DefaultItemAnimator()
        view.adapter = adapter
    }


    suspend fun downloadRecipes(userInput: String): ArrayList<RecipeData> {

        val apiId: String = "2a4625b1"
        val apiKey: String = "ba6e08d3cf44ce2e3e826635508ed960"

        val allData = ArrayList<RecipeData>()
        GlobalScope.async {
            val assetData =
                URL("https://api.edamam.com/api/recipes/v2?type=public&q=${userInput}&app_id=${apiId}&app_key=${apiKey}").readText()
                    .toString()
            val recipeDataArray = (JSONObject(assetData).get("hits") as JSONArray)
            (0 until recipeDataArray.length()).forEach { recipeNr ->
                val dataItem = RecipeData()

                val assetItem = recipeDataArray.get(recipeNr)
                val recipe = (assetItem as JSONObject).get("recipe")

                val images = (recipe as JSONObject).get("images")
                val smallImage = (images as JSONObject).get("SMALL")
                val imageBytes = URL((smallImage as JSONObject).getString("url")).readBytes()
                val recipeImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                val dietLabels: JSONArray = (recipe as JSONObject).get("dietLabels") as JSONArray
                val mealType: JSONArray = (recipe as JSONObject).get("mealType") as JSONArray
                // Referred to in report (Reference x)
                val dietLabelsList: ArrayList<String> = ArrayList<String>()
                val mealTypeList: ArrayList<String> = ArrayList<String>()

                if (dietLabels.length() > 0) {
                    for (i in 0 until dietLabels.length()) {
                        dietLabelsList.add(dietLabels.getString(i))
                    }
                }
                if (mealType.length() > 0) {
                    for (i in 0 until mealType.length()) {
                        mealTypeList.add(mealType.getString(i))
                    }
                }

                // Referred to in report (Reference x)
                var recipeCalories = (recipe as JSONObject).getString("calories").toFloat()
                var recipeYield = (recipe as JSONObject).getString("yield").toFloat()

                dataItem.recipeExternalWebsite = (recipe as JSONObject).getString("url")
                dataItem.recipeDietLabels = dietLabelsList
                dataItem.recipeImage = recipeImage
                dataItem.recipeName = (recipe as JSONObject).getString("label")
                dataItem.recipeMealType = mealTypeList.get(0)
                dataItem.recipeCalories = recipeCalories / recipeYield
                allData.add(dataItem)
            }
        }.await()
        return allData
    }

    fun sortRecipeDataByMealPriority(recipeData: ArrayList<RecipeData>){
        recipeData.sortedBy { recipe ->
            recipe.recipeName?.lowercase()?.contains(UserSettings.priority.lowercase())
        }
    }
}