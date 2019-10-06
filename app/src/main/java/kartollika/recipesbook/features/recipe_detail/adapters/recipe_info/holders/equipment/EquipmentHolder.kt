package kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.holders.equipment

import android.view.View
import com.google.android.material.chip.Chip
import kartollika.recipesbook.common.base.lists.chip.BaseChipHolder
import kartollika.recipesbook.data.models.Equipment

class EquipmentHolder(view: View) : BaseChipHolder<Equipment>(view) {

    private val chip = view as Chip

    fun bind(ingredientDetail: Equipment) {
        chip.text = ingredientDetail.name
    }
}