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
//    var recipeDietLabels1: TextView? = null
//    var recipeDietLabels2: TextView? = null
    lateinit var linear1: LinearLayout
    lateinit var linear2: LinearLayout
    lateinit var linear3: LinearLayout

    constructor(context: Context): super(context){
        recipeImage = ImageView(context)
        selectRecipeBtn = Button(context)
        recipeName = TextView(context)
        recipeMealType = TextView(context)
//        recipeDietLabels1 = TextView(context)
//        recipeDietLabels2 = TextView(context)
        linear1 = LinearLayout(context)
        linear2 = LinearLayout(context)
        linear3 = LinearLayout(context)

        this.orientation = LinearLayout.VERTICAL
        linear1.orientation = LinearLayout.HORIZONTAL
        linear2.orientation = LinearLayout.HORIZONTAL
        linear3.orientation = LinearLayout.HORIZONTAL

        // LinearLayout1
        // Recipe Name
        recipeName?.textSize = 20f

        linear1.addView(recipeImage)
        linear1.addView(recipeName)

        // LinearLayout2

        // Recipe Meal Type
        recipeMealType?.setPadding(5, 0, 5, 0)
        recipeMealType?.setTextSize(15f)
        // Recipe Diet Labels
//        recipeDietLabels1?.setTextSize(15f)
//        recipeDietLabels2?.setTextSize(15f)


        linear2.addView(recipeMealType)

        // LinearLayout3
        // Recipe Select Button
        selectRecipeBtn?.text = "Select"
        selectRecipeBtn?.width = 100
        selectRecipeBtn?.height = ViewGroup.LayoutParams.MATCH_PARENT
        selectRecipeBtn?.setOnClickListener {
            Log.i("btn test", "TEST BUTTON WOO!")
        }

        linear3.addView(selectRecipeBtn)

//        linear2.addView(recipeDietLabels1)
//        linear2.addView(recipeDietLabels2)

        addView(linear1)
        addView(linear2)
        addView(linear3)
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

    fun setRecipeDietLabels1(dietLabels: String?){
//        recipeDietLabels1?.setText(dietLabels)
    }

    fun setRecipeDietLabels2(dietLabels: String?){
//        recipeDietLabels2?.setText(dietLabels)
    }

}