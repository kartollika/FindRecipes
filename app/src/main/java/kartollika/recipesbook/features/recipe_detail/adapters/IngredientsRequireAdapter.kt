package kartollika.recipesbook.features.recipe_detail.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kartollika.recipesbook.R
import kartollika.recipesbook.data.models.IngredientDetail
import kartollika.recipesbook.data.remote.NetworkConstants
import kotlinx.android.synthetic.main.ingredient_recipe_detail_item.view.*

class IngredientsRequireAdapter(diffCallback: DiffUtil.ItemCallback<IngredientDetail>) :
    ListAdapter<IngredientDetail, IngredientsRequireAdapter.RecipeDetailRequiredIngredientViewHolder>(
        diffCallback
    ) {

    companion object {
        val DEFAULT_DIFF_CALLBACK = object : DiffUtil.ItemCallback<IngredientDetail>() {
            override fun areItemsTheSame(
                oldItem: IngredientDetail,
                newItem: IngredientDetail
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: IngredientDetail,
                newItem: IngredientDetail
            ): Boolean {
                return oldItem.name == newItem.name
                    && oldItem.image == newItem.image
                    && oldItem.amount == newItem.amount
                    && oldItem.original == newItem.original
                    && oldItem.unit == newItem.unit
            }
        }
    }

    fun setIngredientsList(list: List<IngredientDetail>) {
        submitList(list)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeDetailRequiredIngredientViewHolder {
        return RecipeDetailRequiredIngredientViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ingredient_recipe_detail_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecipeDetailRequiredIngredientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RecipeDetailRequiredIngredientViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(ingredientDetail: IngredientDetail) {
            Glide.with(itemView.ingredientItemImageView)
                .load(NetworkConstants.IMAGE_BASE_URL + ingredientDetail.image)
                .into(itemView.ingredientItemImageView)

            itemView.ingredientItemTextView.text = ingredientDetail.original
        }

    }
}