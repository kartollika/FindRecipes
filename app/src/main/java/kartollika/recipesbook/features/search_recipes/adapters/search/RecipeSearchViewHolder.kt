package kartollika.recipesbook.features.search_recipes.adapters.search

import android.view.View
import com.bumptech.glide.Glide
import kartollika.recipesbook.R
import kartollika.recipesbook.common.base.lists.recycler.BaseRecyclerHolder
import kartollika.recipesbook.data.models.RecipePreview
import kotlinx.android.synthetic.main.search_recipe_item.view.*

class RecipeSearchViewHolder(
    itemView: View,
    private val onRecipeActionListener: RecipesSearchAdapter.OnRecipeActionListener?
) : BaseRecyclerHolder<RecipePreview>(itemView) {

    private val recipeNameView = itemView.recipeItemImage
    private val recipeImageView = itemView.recipeDetailImage
    private val recipeUsedIngredientsCountView = itemView.recipeItemUsedIngredients
    private val recipeMissingIngredientsCountView = itemView.recipeItemMissedIngredients
    private val recipeItemReadyInMinutes = itemView.recipeItemReadyInMinutes

    override fun bind(item: RecipePreview) {
        recipeNameView.text = item.title
        Glide.with(recipeImageView).load(item.image).centerCrop().into(recipeImageView)
        if (item.usedIngredientCount != -1) {
            recipeUsedIngredientsCountView.text = itemView.context.getString(
                R.string.used_ingredients_count,
                item.usedIngredientCount
            )
        } else {
            recipeUsedIngredientsCountView.visibility = View.GONE
        }

        if (item.missedIngredientCount != -1) {
            recipeMissingIngredientsCountView.text = itemView.context.getString(
                R.string.missed_ingredients_count,
                item.missedIngredientCount
            )
        } else {
            recipeMissingIngredientsCountView.visibility = View.GONE
        }

        if (item.readyInMinutes != null) {
            recipeItemReadyInMinutes.text = itemView.context.getString(R.string.ready_in_minutes, item.readyInMinutes)
        } else {
            recipeItemReadyInMinutes.visibility = View.GONE
        }

        itemView.setOnClickListener {
            onRecipeActionListener?.onItemClicked(item, itemView)
        }
    }
}

