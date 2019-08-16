package kartollika.recipesbook.features.recipe_detail.adapters.recipe_info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kartollika.recipesbook.R
import kartollika.recipesbook.common.base.BaseRecyclerHolder
import kartollika.recipesbook.data.models.IngredientDetail
import kartollika.recipesbook.features.recipe_detail.adapters.required_ingredients.holders.RequiredIngredientsHolder

class RecipeInfoAdapter(diffCallback: DiffUtil.ItemCallback<RecipeDetailInfoItem>) :
    ListAdapter<RecipeDetailInfoItem, BaseRecyclerHolder<Any>>(diffCallback) {

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerHolder<Any> {
        val layoutInflater = LayoutInflater.from(parent.context)

        val holder = when (viewType) {
            RecipeDetailInfoItemHelper.INFO_INGREDIENTS -> {
                RequiredIngredientsHolder(layoutInflater.inflate(R.layout.list_block, parent, false))
            }
            else -> throw IllegalArgumentException()
        }

        return holder as BaseRecyclerHolder<Any>
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: BaseRecyclerHolder<Any>, position: Int) {
        val item = getItem(position).data.data

        when (getItemViewType(position)) {
            RecipeDetailInfoItemHelper.INFO_INGREDIENTS -> {
                (holder as RequiredIngredientsHolder).bind(item as List<IngredientDetail>)
            }
            else -> throw java.lang.IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    companion object {
        val DEFAULT_DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecipeDetailInfoItem>() {
            override fun areItemsTheSame(oldItem: RecipeDetailInfoItem, newItem: RecipeDetailInfoItem): Boolean =
                oldItem.type == newItem.type


            override fun areContentsTheSame(
                oldItem: RecipeDetailInfoItem,
                newItem: RecipeDetailInfoItem
            ): Boolean {
                return oldItem.data == newItem.data
            }
        }
    }
}