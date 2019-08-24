package kartollika.recipesbook.features.recipe_detail.adapters.recipe_info

import android.content.Context
import android.view.LayoutInflater
import kartollika.recipesbook.R
import kartollika.recipesbook.common.base.lists.chip.BaseChipAdapter
import kartollika.recipesbook.data.models.IngredientDetail
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.holders.ingredients.IngredientHolder

class RequiredIngredientsAdapter(private val context: Context) :
    BaseChipAdapter<IngredientDetail, IngredientHolder>(context) {

    override fun onCreateChipViewHolder(): IngredientHolder {
        return IngredientHolder(
            LayoutInflater.from(context).inflate(R.layout.as_chip_layout, null)
        )
    }

    override fun onBindChipViewHolder(holder: IngredientHolder, item: IngredientDetail) {
        holder.bind(item)
    }
}