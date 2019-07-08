package kartollika.recipesbook.features.adapters

import android.content.Context
import android.view.LayoutInflater
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kartollika.recipesbook.R
import kartollika.recipesbook.data.local.entities.IngredientEntity

interface IngredientActionsListener {
    fun onCheckedStateChanged(ingredient: String, isChecked: Boolean)
    fun onDeleteAction(ingredient: String)
}

class IngredientsAdapter(private val context: Context, private val chipGroup: ChipGroup) {

    private var isCloseIconVisible = false

    constructor(context: Context, chipGroup: ChipGroup, isCloseIconVisible: Boolean) : this(
        context,
        chipGroup
    ) {
        this.isCloseIconVisible = isCloseIconVisible
    }

    var ingredientActionsListener: IngredientActionsListener? = null

    fun setupIngredients(ingredientsList: List<IngredientEntity>) {
        chipGroup.removeAllViews()

        for (ingredient in ingredientsList) {
            getNewChipInstance().apply {
                text = ingredient.name
                chipGroup.addView(this)
                isCloseIconVisible = this@IngredientsAdapter.isCloseIconVisible
                isChecked = ingredient.isActive

                setOnCheckedChangeListener { _, isChecked ->
                    ingredientActionsListener?.onCheckedStateChanged(
                        this.text as String,
                        isChecked
                    )
                }

                setOnCloseIconClickListener {
                    ingredientActionsListener?.onDeleteAction(this.text as String)
                    chipGroup.removeView(this)
                }
            }
        }
    }

    private fun getNewChipInstance(): Chip {
        return LayoutInflater.from(context).inflate(R.layout.chip_ingredient, null) as Chip
    }
}