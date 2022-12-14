package com.example.androideksamen

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class RecipeRowAdapter(val allData: ArrayList<RecipeData>, val dbInstance: AppDatabase) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rowView = CustomRecipeView(parent.context)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,position: Int) {
        val rowView = (holder.itemView as CustomRecipeView)
        rowView.setPadding(0, 50, 0, 0)

        rowView.recipeFavorite?.setOnClickListener {
            if (allData.get(position).recipeIsFavorited == false){
                allData.get(position).recipeIsFavorited = true
            }else if(allData.get(position).recipeIsFavorited == true){
                allData.get(position).recipeIsFavorited = false
            }
            val btnBool = allData.get(position).recipeIsFavorited
            Log.i("testBool", "button favotite:" + btnBool)

            rowView.changeFavoriteIcon(allData.get(position).recipeIsFavorited)
            updateItemInDb(allData.get(position).recipeIsFavorited, allData.get(position).recipeName)
        }

        rowView.changeFavoriteIcon(allData.get(position).recipeIsFavorited)

        rowView.setRecipeName(allData.get(position).recipeName)

        rowView.setRecipeImage(allData.get(position).recipeImage)

        rowView.setRecipeMealType(allData.get(position).recipeMealType)

        rowView.recipeName?.setOnClickListener {
            goToExternalRecipeWebsite(allData.get(position).recipeExternalWebsite, rowView.context)
        }
        rowView.recipeImage?.setOnClickListener {
            goToExternalRecipeWebsite(allData.get(position).recipeExternalWebsite, rowView.context)
        }


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
    fun updateItemInDb(favorited: Boolean, name: String?){
        GlobalScope.launch(Dispatchers.IO) {
            dbInstance.searchHistoryDao().updateFavorited(favorited, name)
        }
    }

    fun goToExternalRecipeWebsite(url: String?, context: Context){
        val recipeUri = Uri.parse(url)
        val externalWebsiteIntent = Intent(Intent.ACTION_VIEW, recipeUri)
        context.startActivity(externalWebsiteIntent)
    }

    override fun getItemCount(): Int {
        return allData.size
    }

}