package kartollika.recipesbook.features.search_recipes.adapters.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kartollika.recipesbook.R
import kartollika.recipesbook.data.models.RecipePreview

class RecipesSearchAdapter(diffCallback: DiffUtil.ItemCallback<RecipePreview>) :
    ListAdapter<RecipePreview, RecipeSearchViewHolder>(diffCallback) {

    interface OnAdapterContentChangedListener {
        fun onDataEmpty()
        fun onDataNotEmpty()
    }

    interface OnRecipeActionListener {
        fun onItemClicked(recipe: RecipePreview, view: View)
    }

    var onRecipeActionListener: OnRecipeActionListener? = null
    var onAdapterContentChangedListener: OnAdapterContentChangedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeSearchViewHolder {
        return RecipeSearchViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.search_recipe_item,
                parent,
                false
            ), onRecipeActionListener
        )
    }

    override fun onBindViewHolder(holder: RecipeSearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setRecipesList(recipes: List<RecipePreview>) {
        submitList(recipes)

        if (recipes.isEmpty()) {
            onAdapterContentChangedListener?.onDataEmpty()
        } else {
            onAdapterContentChangedListener?.onDataNotEmpty()
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