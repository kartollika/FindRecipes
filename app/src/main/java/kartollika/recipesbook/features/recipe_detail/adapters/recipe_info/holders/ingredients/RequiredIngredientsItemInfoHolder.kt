package kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.holders.ingredients

import android.view.View
import kartollika.recipesbook.common.utils.lists.chip.BaseChipAdapter
import kartollika.recipesbook.data.models.IngredientDetail
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.RequiredIngredientsAdapter
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.holders.base.BaseBlockHolder

class RequiredIngredientsItemInfoHolder(itemView: View) :
    BaseBlockHolder<IngredientDetail, IngredientHolder>(itemView) {

    override val adapter: BaseChipAdapter<IngredientDetail, IngredientHolder> =
        RequiredIngredientsAdapter(itemView.context)
}