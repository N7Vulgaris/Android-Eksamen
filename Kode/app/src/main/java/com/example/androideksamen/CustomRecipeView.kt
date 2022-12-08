package com.example.androideksamen

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class CustomRecipeView: LinearLayout {

//    var recipeImage: ImageView? = null
    var recipeName: TextView? = null

    constructor(context: Context): super(context){
//        recipeImage = ImageView(context)
        recipeName = TextView(context)
        recipeName?.setPadding(20,20,20,20)
        recipeName?.setTextSize(20f)

        addView(recipeName)
    }

    fun setRecipeName(name: String?){
        recipeName?.setText(name)
        Log.i("testing", "testing set name: "+name)
    }

}