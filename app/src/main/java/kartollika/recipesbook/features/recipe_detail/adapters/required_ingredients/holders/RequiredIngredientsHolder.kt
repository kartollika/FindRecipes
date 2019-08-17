package kartollika.recipesbook.features.recipe_detail.adapters.required_ingredients.holders

import android.annotation.SuppressLint
import android.view.View
import kartollika.recipesbook.common.base.BaseRecyclerHolder
import kartollika.recipesbook.data.models.IngredientDetail
import kartollika.recipesbook.features.recipe_detail.adapters.required_ingredients.RequiredIngredientsAdapter
import kotlinx.android.synthetic.main.list_block.view.*

class RequiredIngredientsHolder(itemView: View) :
    BaseRecyclerHolder<List<IngredientDetail>>(itemView) {

    private val ingredientsListChipGroup = itemView.defineIntolerancesChipGroup
    private val blockTitleTextView = itemView.block_title

    val adapter = RequiredIngredientsAdapter(itemView.context)

    @SuppressLint("SetTextI18n")
    override fun bind(item: List<IngredientDetail>) {
        blockTitleTextView.text = "Ingredients"
        ingredientsListChipGroup.setAdapter(adapter)
        adapter.apply {
            checkedPredicate = { true }
            setupList(item)
        }
    }
}