package kartollika.recipesbook.features.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kartollika.recipesbook.R
import kartollika.recipesbook.data.models.Recipe
import kotlinx.android.synthetic.main.recipe_list_item.view.*

class RecipesSearchAdapter(diffCallback: DiffUtil.ItemCallback<Recipe>) :
    ListAdapter<Recipe, RecipesSearchAdapter.RecipeSearchViewHolder>(diffCallback) {

    var onRecipeActionListener: OnRecipeActionListener? = null

    interface OnRecipeActionListener {
        fun onItemClicked(recipe: Recipe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeSearchViewHolder {
        return RecipeSearchViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recipe_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecipeSearchViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }

    fun setRecipesList(recipes: List<Recipe>) {
        submitList(recipes)
    }

    inner class RecipeSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val recipeNameView = itemView.recipeListItemNameTextView
        private val recipeImageView = itemView.recipeListItemImageView

        fun bindView(recipe: Recipe) {
            recipeNameView.text = recipe.title
            Glide.with(recipeImageView).load(recipe.image).centerCrop().into(recipeImageView)

            itemView.setOnClickListener {
                onRecipeActionListener?.onItemClicked(recipe)
            }
        }
    }

    companion object {
        val DEFAULT_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
                oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem.areContentsTheSame(newItem)
            }
        }
    }
}