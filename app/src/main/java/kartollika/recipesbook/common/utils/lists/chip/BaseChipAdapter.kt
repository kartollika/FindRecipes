package kartollika.recipesbook.common.utils.lists.chip

import android.content.Context
import android.view.View
import kartollika.recipesbook.features.search_recipes.adapters.filter.ChipActionListener

abstract class BaseChipAdapter<T, VH : BaseChipHolder<T>>(private val context: Context) : ChipAdapter<T> {

    private lateinit var observer: ChipAdapterObservable
    var checkedPredicate: (T) -> Boolean = { false }
    var isCloseIconVisible = false
    var actionListener: ChipActionListener? = null

    fun setObserver(observable: ChipAdapterObservable) {
        observer = observable
    }

    fun getObserver() = observer

    override fun setupList(list: List<T>) {
        val chipsList = mutableListOf<View>()
        for (item in list) {
            onCreateChipViewHolder().apply { onBindChipViewHolder(this, item) }.apply {
                chipsList.add(this.getView())
            }
        }
        getObserver().notifyDataSetChanged(chipsList)
    }

    abstract fun onCreateChipViewHolder(): VH

    abstract fun onBindChipViewHolder(holder: VH, item: T)
}