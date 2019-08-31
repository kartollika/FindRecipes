package kartollika.recipesbook.features.recipe_detail.adapters.recipe_info

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kartollika.recipesbook.R
import kartollika.recipesbook.common.base.lists.recycler.BaseRecyclerHolder
import kartollika.recipesbook.data.models.Equipment
import kartollika.recipesbook.data.models.IngredientDetail
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.RecipeDetailInfoItemHelper.INFO_LIST_BLOCK_EQUIPMENT
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.RecipeDetailInfoItemHelper.INFO_LIST_BLOCK_INGREDIENTS
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.RecipeDetailInfoItemHelper.INFO_TEXT
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.holders.equipment.RequiredEquipmentItemInfoHolder
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.holders.ingredients.RequiredIngredientsItemInfoHolder
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.holders.text.ImageTextInfoHolder
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.models.ImageTextModel
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.models.ListBlockModel

class RecipeInfoAdapter(diffCallback: DiffUtil.ItemCallback<RecipeDetailInfoItem>) :
    ListAdapter<RecipeDetailInfoItem, BaseRecyclerHolder<Any>>(diffCallback) {

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerHolder<Any> {
        val context = parent.context
        val holder = when (viewType) {
            INFO_TEXT -> {
                ImageTextInfoHolder(
                    inflateLayout(context, R.layout.imaged_text_item_layout, parent)
                )
            }
            INFO_LIST_BLOCK_INGREDIENTS -> {
                RequiredIngredientsItemInfoHolder(
                    inflateLayout(context, R.layout.list_block, parent)
                )
            }
            INFO_LIST_BLOCK_EQUIPMENT -> {
                RequiredEquipmentItemInfoHolder(
                    inflateLayout(context, R.layout.list_block, parent)
                )
            }
            else -> throw IllegalArgumentException()
        }

        return holder as BaseRecyclerHolder<Any>
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: BaseRecyclerHolder<Any>, position: Int) {
        val item = getItem(position)
        val data = item.data.data

        when (item.type) {
            INFO_TEXT -> {
                (holder as ImageTextInfoHolder).bind(data as ImageTextModel)
            }
            INFO_LIST_BLOCK_INGREDIENTS -> {
                (holder as RequiredIngredientsItemInfoHolder).bind(data as ListBlockModel<IngredientDetail>)
            }
            INFO_LIST_BLOCK_EQUIPMENT -> {
                (holder as RequiredEquipmentItemInfoHolder).bind(data as ListBlockModel<Equipment>)
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

    private fun inflateLayout(context: Context, @LayoutRes layout: Int, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(layout, parent, false)
    }
}