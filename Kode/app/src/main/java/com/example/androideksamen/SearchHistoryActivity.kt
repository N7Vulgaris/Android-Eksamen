package com.example.androideksamen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchHistoryActivity : AppCompatActivity() {

    lateinit var dbInstance: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_history)

        val searchHistoryRv = findViewById<RecyclerView>(R.id.search_history_recycler_view)

        dbInstance = Room.databaseBuilder(this, AppDatabase::class.java, "SearchHistory").build()

        GlobalScope.launch(Dispatchers.IO) {

            val searchHistoryDataList = dbInstance.searchHistoryDao().getAll()
            Log.i("testDb", "DB list: "+searchHistoryDataList)


            GlobalScope.launch(Dispatchers.Main) {
                setAdapter(searchHistoryRv, searchHistoryDataList)
            }
        }

    }

    fun setAdapter(view: RecyclerView, data: List<SearchHistory>){

        // Make setAdapter a global function to avoid repeating code?

        val adapter = SearchHistoryAdapter(data)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        view.layoutManager = layoutManager
        view.itemAnimator = DefaultItemAnimator()
        view.adapter = adapter
    }
}