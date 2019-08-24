package kartollika.recipesbook.features.search_recipes.adapters.filter

import android.content.Context
import android.view.LayoutInflater
import kartollika.recipesbook.R
import kartollika.recipesbook.common.base.lists.chip.BaseChipAdapter
import kartollika.recipesbook.data.models.IngredientSearch

class IngredientsAdapter(private val context: Context) :
    BaseChipAdapter<IngredientSearch, IngredientsFilterHolder>(context) {

    override fun onCreateChipViewHolder(): IngredientsFilterHolder {
        return IngredientsFilterHolder(LayoutInflater.from(context).inflate(R.layout.chip_ingredient, null))
    }

    override fun onBindChipViewHolder(holder: IngredientsFilterHolder, item: IngredientSearch) {
        holder.bind(item, isCloseIconVisible, checkedPredicate, actionListener)
    }
}