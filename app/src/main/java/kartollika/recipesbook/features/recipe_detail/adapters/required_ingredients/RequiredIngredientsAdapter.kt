package kartollika.recipesbook.features.recipe_detail.adapters.required_ingredients

import android.content.Context
import android.view.LayoutInflater
import kartollika.recipesbook.R
import kartollika.recipesbook.common.utils.lists.chip.BaseChipAdapter
import kartollika.recipesbook.data.models.IngredientDetail
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.holders.IngredientItemInfoHolder

class RequiredIngredientsAdapter(private val context: Context) :
    BaseChipAdapter<IngredientDetail, IngredientItemInfoHolder>(context) {

    override fun onCreateChipViewHolder(): IngredientItemInfoHolder {
        return IngredientItemInfoHolder(LayoutInflater.from(context).inflate(R.layout.as_chip_layout, null))
    }

    override fun onBindChipViewHolder(holder: IngredientItemInfoHolder, item: IngredientDetail) {
        holder.bind(item)
    }
}