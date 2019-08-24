package kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.holders.equipment

import android.view.View
import kartollika.recipesbook.common.base.lists.chip.BaseChipAdapter
import kartollika.recipesbook.data.models.Equipment
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.RequiredEquipmentAdapter
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.holders.base.BaseBlockHolder

class RequiredEquipmentItemInfoHolder(itemView: View) : BaseBlockHolder<Equipment, EquipmentHolder>(itemView) {
    override val adapter: BaseChipAdapter<Equipment, EquipmentHolder> = RequiredEquipmentAdapter(itemView.context)
}