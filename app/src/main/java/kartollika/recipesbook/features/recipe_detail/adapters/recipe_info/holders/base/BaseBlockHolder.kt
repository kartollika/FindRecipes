package kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.holders.base

import android.view.View
import kartollika.recipesbook.common.base.BaseRecyclerHolder
import kartollika.recipesbook.common.utils.lists.chip.BaseChipAdapter
import kartollika.recipesbook.common.utils.lists.chip.BaseChipHolder
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.models.ListBlockModel
import kotlinx.android.synthetic.main.list_block.view.*

abstract class BaseBlockHolder<T, VH: BaseChipHolder<T>>(itemView: View) :
    BaseRecyclerHolder<ListBlockModel<T>>(itemView) {

    private val ingredientsListChipGroup = itemView.defineIntolerancesChipGroup
    private val blockTitleTextView = itemView.block_title

    abstract val adapter: BaseChipAdapter<T, VH>

    override fun bind(item: ListBlockModel<T>) {
        ingredientsListChipGroup.setAdapter(adapter)
        blockTitleTextView.text = item.title
        adapter.apply {
            checkedPredicate = { true }
            setupList(item.list)
        }
    }
}