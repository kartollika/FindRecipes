package kartollika.recipesbook.features.recipe_detail

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kartollika.recipesbook.App
import kartollika.recipesbook.R
import kartollika.recipesbook.common.base.BaseFragment
import kartollika.recipesbook.common.utils.injectViewModel
import kartollika.recipesbook.data.models.IngredientDetail
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
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    private fun initializeObservers() {
        viewModel.getRecipeDetail().observe(this, Observer {
            fillRecipeInformation(it)
        })

        viewModel.getIsLoading().observe(this, Observer {
            switchLoadingUiState(it)
        })

        viewModel.getIngredientsList().observe(this, Observer {
            fillIngredientsInformation(it)
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
        Glide.with(this).asBitmap().centerCrop().load(it.image).into(object : CustomTarget<Bitmap>() {

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                recipeDetailImage.setImageBitmap(resource)

                Palette.Builder(resource).setRegion(0, 0, resource.width, 20).generate {
                    requireActivity().window.statusBarColor =
                        it!!.getDarkVibrantColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))
                }
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })

        collapsingToolbar.title = it.title
    }

    private fun fillIngredientsInformation(list: List<IngredientDetail>) {
        ingredientsRequireAdapter.setIngredientsList(list)
    }
}