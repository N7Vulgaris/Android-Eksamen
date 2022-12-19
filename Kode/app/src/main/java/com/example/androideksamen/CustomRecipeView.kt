package com.example.androideksamen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible

// Referred to in report (reference 17)
class CustomRecipeView: LinearLayout {

    var recipeImage: ImageView? = null
    var recipeName: TextView? = null
    var selectRecipeBtn: Button? = null
    var recipeMealType: TextView? = null
    var recipeFavorite: ImageView? = null
    lateinit var linear1: LinearLayout
    lateinit var linear2: LinearLayout
    lateinit var linear3: LinearLayout

    constructor(context: Context): super(context){
        recipeImage = ImageView(context)
        selectRecipeBtn = Button(context)
        recipeName = TextView(context)
        recipeMealType = TextView(context)
        recipeFavorite = ImageView(context)

        linear1 = LinearLayout(context)
        linear2 = LinearLayout(context)
        linear3 = LinearLayout(context)

        this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        linear1.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        linear2.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        linear3.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        this.setPadding(0, 0, 0 ,30)
        this.orientation = LinearLayout.VERTICAL
        linear1.orientation = LinearLayout.HORIZONTAL
        linear2.orientation = LinearLayout.HORIZONTAL
        linear3.orientation = LinearLayout.HORIZONTAL

        recipeName?.textSize = 20f
        recipeName?.layoutParams = LinearLayout.LayoutParams(500,ViewGroup.LayoutParams.MATCH_PARENT)

        linear1.addView(recipeImage)
        linear1.addView(recipeName)
        linear1.addView(recipeFavorite)

        recipeMealType?.setPadding(5, 0, 5, 0)
        recipeMealType?.setTextSize(15f)

        linear2.addView(recipeMealType)

        selectRecipeBtn?.text = "Select"
        selectRecipeBtn?.width = 100
        selectRecipeBtn?.height = ViewGroup.LayoutParams.MATCH_PARENT

        linear3.addView(selectRecipeBtn)

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

    fun changeFavoriteIcon(favorite: Boolean){
        if(favorite){
            recipeFavorite!!.setImageResource(R.drawable.favorite_true)
        }else{
            recipeFavorite!!.setImageResource(R.drawable.favorite_false)
        }

    }

    // Referred to in report (Reference 18)
    fun setBackgroundColor(recipeCalories: Float?, maxDailyCalories: Float, btn: Button?){
        if (recipeCalories != null){
            if(recipeCalories > maxDailyCalories!!){
                this.setBackgroundColor(Color.parseColor("#FFCCCB"))
                btn?.isVisible = false
            }else{
                this.setBackgroundColor(Color.LTGRAY)
            }
        }
    }
}