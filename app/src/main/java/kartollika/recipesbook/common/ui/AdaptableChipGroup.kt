package kartollika.recipesbook.common.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kartollika.recipesbook.common.base.lists.chip.BaseChipAdapter
import kartollika.recipesbook.common.base.lists.chip.ChipAdapterObservable

class AdaptableChipGroup(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    ChipGroup(context, attrs, defStyleAttr) {

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?) : this(context, null)

    private var adapter: BaseChipAdapter<*, *>? = null
    private val observable: ChipAdapterObservable = object :
        ChipAdapterObservable {
        override fun notifyDataSetChanged(list: List<View>) {
            updateItems(list)
        }

        override fun onItemInserted(position: Int, chip: Chip) {
        }

        override fun onItemRemoved(position: Int, chip: Chip) {
        }
    }

    fun setAdapter(adapter: BaseChipAdapter<*, *>) {
        this.adapter = adapter
        adapter.setObserver(observable)
    }

    private fun updateItems(list: List<View>) {
        removeAllViews()
        for (item in list) {
            addView(item)
        }
    }
}