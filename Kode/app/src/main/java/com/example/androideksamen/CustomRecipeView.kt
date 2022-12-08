package com.example.androideksamen

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class CustomRecipeView: LinearLayout {

    var recipeImage: ImageView? = null
    var recipeName: TextView? = null
    var selectRecipeBtn: Button? = null

    constructor(context: Context): super(context){
        recipeImage = ImageView(context)
        selectRecipeBtn = Button(context)
        recipeName = TextView(context)

        recipeName?.setPadding(20,20,20,20)
        recipeName?.setTextSize(20f)

        selectRecipeBtn?.text = "Select"
        selectRecipeBtn?.width = 100
        selectRecipeBtn?.height = 70
        selectRecipeBtn?.setOnClickListener {
            Log.i("btn test", "TEST BUTTON WOO!")
        }

        addView(recipeImage)
        addView(selectRecipeBtn)
        addView(recipeName)
    }

    fun setRecipeName(name: String?){
        recipeName?.setText(name)
    }

    fun setRecipeImage(image: Bitmap?){
        recipeImage?.setImageBitmap(image)
    }

}