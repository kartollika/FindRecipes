package kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.holders.equipment

import android.view.View
import kartollika.recipesbook.common.base.lists.chip.BaseChipHolder
import kartollika.recipesbook.data.models.Equipment
import kotlinx.android.synthetic.main.as_chip_layout.view.*

class EquipmentHolder(view: View) : BaseChipHolder<Equipment>(view) {

    private val text = view.chipTextView

    fun bind(ingredientDetail: Equipment) {
        text.text = ingredientDetail.name
    }
}