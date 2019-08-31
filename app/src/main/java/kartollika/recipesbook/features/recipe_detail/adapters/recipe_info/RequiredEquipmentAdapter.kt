package kartollika.recipesbook.features.recipe_detail.adapters.recipe_info

import android.content.Context
import android.view.LayoutInflater
import kartollika.recipesbook.R
import kartollika.recipesbook.common.base.lists.chip.BaseChipAdapter
import kartollika.recipesbook.data.models.Equipment
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.holders.equipment.EquipmentHolder

class RequiredEquipmentAdapter(private val context: Context) :
    BaseChipAdapter<Equipment, EquipmentHolder>(context) {

    override fun onCreateChipViewHolder(): EquipmentHolder {
        return EquipmentHolder(
            LayoutInflater.from(context).inflate(R.layout.chip_detail, null)
        )
    }

    override fun onBindChipViewHolder(holder: EquipmentHolder, item: Equipment) {
        holder.bind(item)
    }
}