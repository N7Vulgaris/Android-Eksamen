package com.example.androideksamen

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecipeRowAdapter(val allData: ArrayList<RecipeData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rowView = CustomRecipeView(parent.context)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val rowView = (holder.itemView as CustomRecipeView)
        rowView.setPadding(0, 0, 0, 50)
        rowView.setRecipeName(allData.get(position).recipeName)
        rowView.setRecipeImage(allData.get(position).recipeImage)
        rowView.setRecipeMealType(allData.get(position).recipeMealType)
        rowView.setRecipeDietLabels(allData.get(position).recipeDietLabels)

    }

    override fun getItemCount(): Int {
        return allData.size
    }

}