package com.example.androideksamen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class CustomRecipeView: LinearLayout {

    var recipeImage: ImageView? = null
    var recipeName: TextView? = null
    var selectRecipeBtn: Button? = null
    var recipeMealType: TextView? = null
    var recipeDietLabels: TextView? = null
    lateinit var linear1: LinearLayout
    lateinit var linear2: LinearLayout

    constructor(context: Context): super(context){
        recipeImage = ImageView(context)
        selectRecipeBtn = Button(context)
        recipeName = TextView(context)
        recipeMealType = TextView(context)
        recipeDietLabels = TextView(context)
        linear1 = LinearLayout(context)
        linear2 = LinearLayout(context)

        this.orientation = LinearLayout.VERTICAL
        linear1.orientation = LinearLayout.HORIZONTAL
        linear2.orientation = LinearLayout.HORIZONTAL

        // LinearLayout1
        // Recipe Name
        recipeName?.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        recipeName?.textSize = 20f
        recipeName?.maxWidth = 400

        linear1.addView(recipeImage)
        linear1.addView(recipeName)

        // LinearLayout2
        // Recipe Select Button
        selectRecipeBtn?.text = "Select"
        selectRecipeBtn?.width = 100
        selectRecipeBtn?.height = ViewGroup.LayoutParams.MATCH_PARENT
        selectRecipeBtn?.setOnClickListener {
            Log.i("btn test", "TEST BUTTON WOO!")
        }
        // Recipe Meal Type
        recipeMealType?.setPadding(20, 20, 20, 20)
        recipeMealType?.setTextSize(15f)
        // Recipe Diet Labels
        recipeDietLabels?.setTextSize(15f)

        linear2.addView(selectRecipeBtn)
        linear2.addView(recipeMealType)
        linear2.addView(recipeDietLabels)

        addView(linear1)
        addView(linear2)
    }

    fun setRecipeName(name: String?){
        recipeName?.setText(name)
    }

    fun setRecipeImage(image: Bitmap?){
        recipeImage?.setImageBitmap(image)
    }

    fun setRecipeMealType(mealType: String?){
        recipeMealType?.setText(mealType)
    }

    fun setRecipeDietLabels(dietLabels: String?){
        recipeDietLabels?.setText(dietLabels)
    }

}