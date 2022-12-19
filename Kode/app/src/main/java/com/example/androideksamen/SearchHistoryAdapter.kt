package com.example.androideksamen

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchHistoryAdapter(val allData: List<SearchHistoryEntity>, val dbInstance: AppDatabase) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rowView = CustomRecipeView(parent.context)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val rowView = (holder.itemView as CustomRecipeView)
        rowView.setPadding(0, 50, 0, 0)

        rowView.setBackgroundColor(allData.get(position).recipeCalories, UserSettings.dailyIntake, rowView.selectRecipeBtn)
        val searchHistoryImage = BitmapFactory.decodeByteArray(allData.get(position).recipeImage, 0, allData.get(position).recipeImage!!.size)

        rowView.recipeFavorite?.setOnClickListener {
            if (!allData.get(position).recipeIsFavorited){
                allData.get(position).recipeIsFavorited = true
            }else if(allData.get(position).recipeIsFavorited){
                allData.get(position).recipeIsFavorited = false
            }

            rowView.changeFavoriteIcon(allData.get(position).recipeIsFavorited)
            updateItemInDb(allData.get(position).recipeIsFavorited, allData.get(position).recipeName)
        }

        rowView.selectRecipeBtn?.setOnClickListener {
            UserSettings.dailyIntake -= allData.get(position).recipeCalories
            rowView.setBackgroundColor(allData.get(position).recipeCalories, UserSettings.dailyIntake, rowView.selectRecipeBtn)
        }

        rowView.changeFavoriteIcon(allData.get(position).recipeIsFavorited)

        rowView.setRecipeName(allData.get(position).recipeName)

        rowView.setRecipeImage(searchHistoryImage)

        rowView.setRecipeMealType(allData.get(position).recipeMealType)

        // Referred to in report (Reference x)
        rowView.recipeName?.setOnClickListener {
            goToExternalRecipeWebsite(allData.get(position).recipeExternalWebsite, rowView.context)
        }
        // Referred to in report (Reference x)
        rowView.recipeImage?.setOnClickListener {
            goToExternalRecipeWebsite(allData.get(position).recipeExternalWebsite, rowView.context)
        }

        // Referred to in report (Reference x)
        for (i in 0 until allData.get(position).recipeDietLabels.size){
            val txtView = TextView(rowView.context)
            txtView.setText(allData.get(position).recipeDietLabels[i])
            txtView.setTextSize(15f)
            txtView.setPadding(0, 0, 5, 0,)
            txtView.isSingleLine = false
            txtView.maxLines = 2
            txtView.setLines(2)
            rowView.linear2.addView(txtView)
        }
    }

    // Referred to in report (Reference x)
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

    // Referred to in report (Reference x)
    override fun getItemCount(): Int {
        return if(UserSettings.maxShowItems > allData.size){
            allData.size
        }else{
            UserSettings.maxShowItems
        }
    }
}