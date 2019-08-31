package kartollika.recipesbook.features.search_recipes.adapters.filter

import android.view.View
import com.google.android.material
    .chip.Chip
import kartollika.recipesbook.common.base.lists.chip.BaseChipHolder
import kartollika.recipesbook.data.models.IngredientSearch

class IngredientsFilterHolder(itemView: View) : BaseChipHolder<IngredientSearch>(itemView) {

    private val chip: Chip = itemView as Chip

    fun bind(
        item: IngredientSearch,
        isChipCloseIconVisible: Boolean,
        checkedPredicate: (IngredientSearch) -> Boolean,
        actionListener: ChipActionListener?
    ) {
        chip.apply {
            text = item.name
            isCloseIconVisible = isChipCloseIconVisible
            isChecked = checkedPredicate(item)
            setOnCheckedChangeListener { _, isChecked ->
                actionListener?.onCheckedStateChanged(
                    this.text as String,
                    isChecked
                )
            }

            setOnCloseIconClickListener {
                actionListener?.onDeleteAction(text as String)
            }
        }
    }
}