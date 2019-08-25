package kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.holders.ingredients

import android.view.View
import com.google.android.material.chip.Chip
import kartollika.recipesbook.common.base.lists.chip.BaseChipHolder
import kartollika.recipesbook.data.models.IngredientDetail

class IngredientHolder(view: View) : BaseChipHolder<IngredientDetail>(view) {

    private val text = view as Chip

    fun bind(ingredientDetail: IngredientDetail) {
        text.text = ingredientDetail.name
    }
}