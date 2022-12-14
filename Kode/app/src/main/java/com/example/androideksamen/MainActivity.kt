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
import com.example.androideksamen.UserSettings.Settings
import java.io.ByteArrayOutputStream

@Dao
interface SearchHistoryDao{
    @Insert fun addRecipe(recipe: SearchHistory)
    @Query("SELECT * FROM SearchHistory") fun getAll(): List<SearchHistory>
    @Query("UPDATE SearchHistory SET recipeIsFavorited = :isFavorited WHERE recipeName = :name") fun updateFavorited(isFavorited: Boolean, name: String?)
}

@Database(entities = [SearchHistory::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase(){
    abstract fun searchHistoryDao(): SearchHistoryDao
}

class MainActivity : AppCompatActivity() {

//    lateinit var allData: ArrayList<RecipeData>
    lateinit var dbInstance: AppDatabase
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

        //Access to global settings variables here !!
        println(Settings)

        // Button onClick START
        searchBtn.setOnClickListener{
            GlobalScope.launch(Dispatchers.Main) {

                userInput = searchInput.text.toString()
                if(userInput != ""){

                    allRecipeData = downloadRecipes(userInput)

                    // Sorting of downloaded data START (Make this into its own function)
//                { recipe ->
//                    recipe.recipeMealType?.lowercase()?.contains(Settings.priority.lowercase())
//                }

                    allRecipeData.forEach { recipe->
                        Log.i("recipeTest", "recipeMealType: "+recipe.recipeMealType?.lowercase())
                        Log.i("recipeTest", "Settings: "+Settings.priority.lowercase())
                        Log.i("recipeTest", "BOOL: "+recipe.recipeMealType?.lowercase()?.contains(Settings.priority.lowercase()) as Boolean)
                    }
                    // Sorting of downloaded data END
                    addRecipeListToSearchHistoryDatabase(allRecipeData)
                    setAdapter(recipeRecyclerView, allRecipeData, dbInstance)
                }
            }
        }
        searchHistoryBtn.setOnClickListener {
            startActivity(Intent(applicationContext, SearchHistoryActivity::class.java))
        }
        settingsBtn.setOnClickListener {
            startActivity(Intent(applicationContext, SettingsActivity::class.java))
        }
        // Button onclick END

    }

    fun addRecipeListToSearchHistoryDatabase(recipeDataList: ArrayList<RecipeData>){
        // First create a search history database, then create the functionality
        // to add search results to the database
        // DB TEST START - Remember to switch back properties from the RecipeData class to the
        // AbstractRecipeData class (recipeImage and recipeDietLabels)

//        val typeConverter: Converters = Converters()

        dbInstance = Room.databaseBuilder(this, AppDatabase::class.java, "SearchHistory").build()

        GlobalScope.launch(Dispatchers.IO) {
            recipeDataList.forEach { searchHistory ->

                // Encode Bitmap image to ByteArray
                val stream = ByteArrayOutputStream()
                val searchHistoryImage = searchHistory.recipeImage
                searchHistoryImage?.compress(Bitmap.CompressFormat.PNG, 90, stream)
                val imageByteArray = stream.toByteArray()


                val newSearchHistoryItem = SearchHistory(0,
                imageByteArray,
                searchHistory.recipeName,
                searchHistory.recipeMealType,
                searchHistory.recipeDietLabels,
                searchHistory.recipeCalories,
                searchHistory.recipeIsFavorited,
                searchHistory.recipeExternalWebsite)
                dbInstance.searchHistoryDao().addRecipe(newSearchHistoryItem)
            }

        }
        // DB TEST END
    }

    fun showStartupRecipes(data: ArrayList<RecipeData>){
        // Function to show 10 random recipes depending on the time of day (dinner, breakfast etc)
        // when the app first launches
    }

    fun setAdapter(view: RecyclerView, data: ArrayList<RecipeData>, dbInstance: AppDatabase){
            val adapter = RecipeRowAdapter(data, dbInstance)
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

                // Download images START
                val images = (recipe as JSONObject).get("images")
                val smallImage = (images as JSONObject).get("SMALL")

//                val recipeImageURL = (smallImage as JSONObject).getString("url")

                val imageBytes = URL((smallImage as JSONObject).getString("url")).readBytes()
                val recipeImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                // Download images END

                val dietLabels: JSONArray = (recipe as JSONObject).get("dietLabels") as JSONArray
                val mealType: JSONArray = (recipe as JSONObject).get("mealType") as JSONArray
                val dietLabelsList: ArrayList<String> = ArrayList<String>()
                val mealTypeList: ArrayList<String> = ArrayList<String>()

                // For the report: mention why we create put the JSONArray in an ArrayList, and
                // then set the ArrayList to the RecipeData recipeDietLabels and recipe MealType

                if(dietLabels != null){
                    for (i in 0 until dietLabels.length()){
                        dietLabelsList.add(dietLabels.getString(i))
                        Log.i("testArray", "Array: "+dietLabels.getString(i))
                    }
                }
                if(mealType != null){
                    for (i in 0 until mealType.length()){
                        mealTypeList.add(mealType.getString(i))
                    }
                }

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

        allData.forEach { recipe ->
            Log.i("testing", "test: " + recipe.recipeName)
//            Log.i("testing", "test: " + recipe.yield)
        }
        return allData
    }

//    suspend fun downloadRecipeImage(recipeData: ArrayList<RecipeData>): ArrayList<RecipeData>{
//        val apiId: String = "2a4625b1"
//        val apiKey: String = "ba6e08d3cf44ce2e3e826635508ed960"
//
//        GlobalScope.async {
//            recipeData.forEach { recipe ->
//                val recipeURL = URL("")
//            }
//        }.await()
//    }
}