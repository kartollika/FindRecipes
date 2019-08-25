package kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.holders.text

import android.view.View
import kartollika.recipesbook.common.base.BaseRecyclerHolder
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.models.ImageTextModel
import kotlinx.android.synthetic.main.imaged_text_item_layout.view.*

class ImageTextInfoHolder(itemView: View) : BaseRecyclerHolder<ImageTextModel>(itemView) {

    private val contentTextView = itemView.recipeDetailInfoTextView

    override fun bind(item: ImageTextModel) {
        contentTextView.text = item.text

        val drawable = itemView.context.getDrawable(item.drawableInt)
        contentTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
    }

}