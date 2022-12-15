package com.example.androideksamen

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.Image

class RecipeData {
    var recipeImage: Bitmap? = null
    var recipeName: String? = null
    //    var yield: String? = null
    var recipeMealType: String? = null
    var recipeDietLabels: ArrayList<String> = ArrayList()
    var recipeCalories: Float = 0f
    var recipeIsFavorited: Boolean = false
    var recipeExternalWebsite: String? = null
}