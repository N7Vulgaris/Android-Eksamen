package com.example.androideksamen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecipeRowAdapter(val allData: ArrayList<RecipeData>, val searchHistoryDbIntance: AppDatabase) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rowView = CustomRecipeView(parent.context)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,position: Int) {
        val rowView = (holder.itemView as CustomRecipeView)
        rowView.setPadding(0, 50, 0, 0)
        rowView.setBackgroundColor(allData.get(position).recipeCalories, UserSettings.dailyIntake, rowView.selectRecipeBtn)

        rowView.recipeFavorite?.setOnClickListener {
            if (allData.get(position).recipeIsFavorited == false){
                allData.get(position).recipeIsFavorited = true
            }else if(allData.get(position).recipeIsFavorited == true){
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

        rowView.setRecipeImage(allData.get(position).recipeImage)

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

    fun updateItemInDb(favorited: Boolean, name: String?){
        GlobalScope.launch(Dispatchers.IO) {
            searchHistoryDbIntance.searchHistoryDao().updateFavorited(favorited, name)
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