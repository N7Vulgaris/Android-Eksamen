package com.example.androideksamen

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipeRowAdapter(val allData: ArrayList<RecipeData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rowView = CustomRecipeView(parent.context)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,position: Int) {
        val rowView = (holder.itemView as CustomRecipeView)
        rowView.setPadding(0, 0, 0, 50)

        rowView.setRecipeName(allData.get(position).recipeName)
        rowView.setRecipeImage(allData.get(position).recipeImage)
        rowView.setRecipeMealType(allData.get(position).recipeMealType)


//        (0 until allData.get(position).recipeDietLabels.size).forEach { label ->
//            val txt: TextView = TextView(rowView.context)
//            txt.setText(allData.get(position).recipeDietLabels[label])
//            txt.setTextSize(15f)
//            Log.i("testArray", "from adapter: " + txt.text)
//            rowView.linear2.addView(txt)
//        }

        for (i in 0 until allData.get(position).recipeDietLabels.size){
            Log.i("testArray", "test size : "+ allData.get(position).recipeDietLabels.size)
            val txtView = TextView(rowView.context)
            txtView.setText(allData.get(position).recipeDietLabels[i])
            txtView.setTextSize(15f)
            txtView.setPadding(0, 0, 5, 0,)
            txtView.isSingleLine = false
            txtView.maxLines = 2
            txtView.setLines(2)
//            txtView.setBackgroundColor(Color.GRAY)
            rowView.linear2.addView(txtView)
        }

//        rowView.setRecipeDietLabels1(allData.get(position).recipeDietLabels.get(0))

//        Log.i("testArray", "in adapter: "+allData.get(position).recipeDietLabels.get(0))
//
//        rowView.setRecipeDietLabels2(allData.get(position).recipeDietLabels.get(1))
//
//        Log.i("testArray", "in adapter: "+allData.get(position).recipeDietLabels.get(1))

    }

    override fun getItemCount(): Int {
        return allData.size
    }

}