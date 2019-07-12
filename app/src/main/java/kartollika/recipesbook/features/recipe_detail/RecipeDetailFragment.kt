package kartollika.recipesbook.features.recipe_detail

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import kartollika.recipesbook.App
import kartollika.recipesbook.R
import kartollika.recipesbook.common.base.BaseFragment
import kartollika.recipesbook.common.utils.injectViewModel
import kartollika.recipesbook.data.models.Recipe
import kartollika.recipesbook.features.recipe_detail.adapters.IngredientsRequireAdapter
import kotlinx.android.synthetic.main.recipe_detail_layout.*

class RecipeDetailFragment : BaseFragment() {

    private lateinit var ingredientsRequireAdapter: IngredientsRequireAdapter

    private val viewModel by injectViewModel { App.diManager.applicationComponent!!.recipeDetailViewModel }

    override fun getLayoutRes(): Int = R.layout.recipe_detail_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeIngredientsRecyclerView()
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        Handler().post { initializeObservers() }

        val args = RecipeDetailFragmentArgs.fromBundle(arguments)
        viewModel.loadRecipeById(args.recipeId)
    }

    private fun initializeIngredientsRecyclerView() {
        ingredientsRequireAdapter =
            IngredientsRequireAdapter(IngredientsRequireAdapter.DEFAULT_DIFF_CALLBACK)
        recipeDetailRequiredIngredients.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ingredientsRequireAdapter
        }
    }

    private fun initializeObservers() {
        viewModel.getRecipeDetail().observe(this, Observer {
            fillRecipeInformation(it)
        })

        viewModel.getIsLoading().observe(this, Observer {
            switchLoadingUiState(it)
        })
    }

    private fun switchLoadingUiState(isLoading: Boolean) {
        if (isLoading) {
            content.visibility = View.GONE
            recipeDetailContentLoadingProgressView.show()
        } else {
            content.visibility = View.VISIBLE
            recipeDetailContentLoadingProgressView.hide()
        }
    }

    private fun fillRecipeInformation(it: Recipe) {
        Glide.with(this).load(it.image).centerCrop().into(recipeDetailImage)
        collapsingToolbar.title = it.title
        ingredientsRequireAdapter.setIngredientsList(it.requiredIngredients)
    }
}