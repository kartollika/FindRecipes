package kartollika.recipesbook.common.base.lists.chip

import android.view.View
import com.google.android.material.chip.Chip

interface ChipAdapterObservable {
    fun notifyDataSetChanged(list: List<View>)
    fun onItemInserted(position: Int, chip: Chip)
    fun onItemRemoved(position: Int, chip: Chip)
}