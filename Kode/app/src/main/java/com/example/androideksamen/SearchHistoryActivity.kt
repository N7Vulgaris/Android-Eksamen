package com.example.androideksamen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchHistoryActivity : AppCompatActivity() {

//    lateinit var dbInstance: AppDatabase
    lateinit var dbInstance: SearchHistoryDatabase
    lateinit var searchHistoryDataList: List<SearchHistoryEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_history)

        val searchHistoryRv = findViewById<RecyclerView>(R.id.search_history_recycler_view)
        val breakfastFilterBtn = findViewById<Button>(R.id.breakfast_filter_btn)
        val brunchFilterBtn = findViewById<Button>(R.id.brunch_filter_btn)
        val dinnerFilterBtn = findViewById<Button>(R.id.dinner_filter_btn)

        dbInstance = Room.databaseBuilder(this, SearchHistoryDatabase::class.java, "SearchHistory").build()

        GlobalScope.launch(Dispatchers.IO) {

            searchHistoryDataList = dbInstance.searchHistoryDao().getAll()
//            Log.i("testDb", "DB list: "+searchHistoryDataList)

            if(searchHistoryDataList.isNotEmpty()) {
                GlobalScope.launch(Dispatchers.Main) {
                    setAdapter(searchHistoryRv, searchHistoryDataList, dbInstance)
                }
            }
        }

        breakfastFilterBtn.setOnClickListener {
            val filteredList = searchHistoryDataList.filter { dbItem -> dbItem.recipeMealType?.lowercase()?.contains(breakfastFilterBtn.text.toString().lowercase())!!}
            if(filteredList.isNotEmpty()) {
                GlobalScope.launch(Dispatchers.Main) {
                    setAdapter(searchHistoryRv, filteredList, dbInstance)
                }
            }
        }
        brunchFilterBtn.setOnClickListener {
            val filteredList = searchHistoryDataList.filter { dbItem -> dbItem.recipeMealType?.lowercase()?.contains(brunchFilterBtn.text.toString().lowercase())!!}
            if(filteredList.isNotEmpty()) {
                GlobalScope.launch(Dispatchers.Main) {
                    setAdapter(searchHistoryRv, filteredList, dbInstance)
                }
            }
        }
        dinnerFilterBtn.setOnClickListener {
            val filteredList = searchHistoryDataList.filter { dbItem -> dbItem.recipeMealType?.lowercase()?.contains(dinnerFilterBtn.text.toString().lowercase())!!}
            if(filteredList.isNotEmpty()) {
                GlobalScope.launch(Dispatchers.Main) {
                    setAdapter(searchHistoryRv, filteredList, dbInstance)
                }
            }
        }

    }

    fun setAdapter(view: RecyclerView, data: List<SearchHistoryEntity>, dbInstance: SearchHistoryDatabase){

        // Make setAdapter a global function to avoid repeating code?

        val adapter = SearchHistoryAdapter(data, dbInstance)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        view.layoutManager = layoutManager
        view.itemAnimator = DefaultItemAnimator()
        view.adapter = adapter
    }
}