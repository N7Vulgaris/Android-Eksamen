package com.example.androideksamen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchHistoryActivity : AppCompatActivity() {

    lateinit var searchHistoryDbInstance: AppDatabase
    lateinit var searchHistoryDataList: List<SearchHistoryEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_history)

        val searchHistoryRv = findViewById<RecyclerView>(R.id.search_history_recycler_view)
        val breakfastFilterBtn = findViewById<Button>(R.id.breakfast_filter_btn)
        val brunchFilterBtn = findViewById<Button>(R.id.brunch_filter_btn)
        val dinnerFilterBtn = findViewById<Button>(R.id.dinner_filter_btn)
        // Referred to in report (Reference 2)
        val favoriteFilterBtn = findViewById<Button>(R.id.favorite_filter_btn)

        // Referred to in report (Reference 5)
        searchHistoryDbInstance = Room.databaseBuilder(this, AppDatabase::class.java, "AppDatabase").build()


        GlobalScope.launch(Dispatchers.IO) {

            searchHistoryDataList = searchHistoryDbInstance.searchHistoryDao().getAll()

            if (searchHistoryDataList.isNotEmpty()) {
                GlobalScope.launch(Dispatchers.Main) {
                    setAdapter(searchHistoryRv, searchHistoryDataList, searchHistoryDbInstance)
                }
            }
        }

        favoriteFilterBtn.setOnClickListener {
            filterSearchHistoryByFavorited(searchHistoryRv)
        }

        // Referred to in report (Reference 13)
        breakfastFilterBtn.setOnClickListener {
            filterSearchHistoryByMealType(breakfastFilterBtn, searchHistoryRv)
        }
        brunchFilterBtn.setOnClickListener {
            filterSearchHistoryByMealType(brunchFilterBtn, searchHistoryRv)
        }
        dinnerFilterBtn.setOnClickListener {
            filterSearchHistoryByMealType(dinnerFilterBtn, searchHistoryRv)
        }

    }

    fun setAdapter(view: RecyclerView, data: List<SearchHistoryEntity>, dbInstance: AppDatabase){
        val adapter = SearchHistoryAdapter(data, dbInstance)
        val layoutManager = LinearLayoutManager(this)
        view.layoutManager = layoutManager
        view.itemAnimator = DefaultItemAnimator()
        view.adapter = adapter
    }

    fun filterSearchHistoryByFavorited(searchHistoryRv: RecyclerView){
        val filteredList = searchHistoryDataList.filter { dbItem -> dbItem.recipeIsFavorited }
        if(filteredList.isNotEmpty()){
            GlobalScope.launch(Dispatchers.Main) {
                setAdapter(searchHistoryRv, filteredList, searchHistoryDbInstance)
            }
        }
    }

    // Referred to in report (Reference 13)
    fun filterSearchHistoryByMealType(filterBtn: Button, searchHistoryRv: RecyclerView){
        val filteredList = searchHistoryDataList.filter { dbItem -> dbItem.recipeMealType?.lowercase()?.contains(filterBtn.text.toString().lowercase())!!}
        if(filteredList.isNotEmpty()) {
            GlobalScope.launch(Dispatchers.Main) {
                setAdapter(searchHistoryRv, filteredList, searchHistoryDbInstance)
            }
        }
    }
}