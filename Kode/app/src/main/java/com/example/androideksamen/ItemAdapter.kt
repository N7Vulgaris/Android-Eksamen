package com.example.androideksamen

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(val allData: ArrayList<RecipeData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.i("bamboo", "onCreateViewHolder START")
        val rowView = CustomRecipeView(parent.context)
        Log.i("bamboo", "onCreateViewHolder END")
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.i("bamboo", "onBindViewHolder START")
        val rowView = (holder.itemView as CustomRecipeView)
        rowView.setRecipeName(allData.get(position).recipeName)
        Log.i("bamboo", "onBindViewHolder END")
    }

    override fun getItemCount(): Int {
        return allData.size
    }

}