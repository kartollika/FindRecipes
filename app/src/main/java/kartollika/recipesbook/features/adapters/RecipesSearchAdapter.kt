package kartollika.recipesbook.features.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kartollika.recipesbook.R
import kartollika.recipesbook.data.models.RecipePreview
import kotlinx.android.synthetic.main.search_recipe_item.view.*

class RecipesSearchAdapter(diffCallback: DiffUtil.ItemCallback<RecipePreview>) :
    ListAdapter<RecipePreview, RecipesSearchAdapter.RecipeSearchViewHolder>(diffCallback) {

    var onRecipeActionListener: OnRecipeActionListener? = null

    interface OnRecipeActionListener {
        fun onItemClicked(recipe: RecipePreview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeSearchViewHolder {
        return RecipeSearchViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.search_recipe_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecipeSearchViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }

    fun setRecipesList(recipes: List<RecipePreview>) {
        submitList(recipes)
    }

    inner class RecipeSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val recipeNameView = itemView.recipe_item_name
        private val recipeImageView = itemView.recipe_item_image
        private val recipeUsedIngredientsCountView = itemView.recipe_item_used_ingredients
        private val recipeMissingIngredientsCountView = itemView.recipe_item_missed_ingredients

        fun bindView(recipe: RecipePreview) {
            recipeNameView.text = recipe.title
            Glide.with(recipeImageView).load(recipe.image).centerCrop().into(recipeImageView)
            if (recipe.usedIngredientCount != -1) {
                recipeUsedIngredientsCountView.text = itemView.context.getString(
                    R.string.used_ingredients_count,
                    recipe.usedIngredientCount
                )
            }

            if (recipe.missedIngredientCount != -1) {
                recipeMissingIngredientsCountView.text = itemView.context.getString(
                    R.string.missed_ingredients_count,
                    recipe.missedIngredientCount
                )
            }

            itemView.setOnClickListener {
                onRecipeActionListener?.onItemClicked(recipe)
            }
        }
    }

    companion object {
        val DEFAULT_DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecipePreview>() {
            override fun areItemsTheSame(oldItem: RecipePreview, newItem: RecipePreview): Boolean =
                oldItem.id == newItem.id


            override fun areContentsTheSame(
                oldItem: RecipePreview,
                newItem: RecipePreview
            ): Boolean {
                return oldItem.areContentsTheSame(newItem)
            }
        }
    }
}