package kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.holders.ingredients

import android.view.View
import kartollika.recipesbook.common.utils.lists.chip.BaseChipHolder
import kartollika.recipesbook.data.models.IngredientDetail
import kotlinx.android.synthetic.main.as_chip_layout.view.*

class IngredientHolder(view: View) : BaseChipHolder<IngredientDetail>(view) {

    private val text = view.chipTextView

    fun bind(ingredientDetail: IngredientDetail) {
        text.text = ingredientDetail.name
    }
}