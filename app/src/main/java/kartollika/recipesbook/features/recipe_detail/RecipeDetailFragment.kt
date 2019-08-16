package kartollika.recipesbook.features.recipe_detail

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kartollika.recipesbook.App
import kartollika.recipesbook.R
import kartollika.recipesbook.common.base.BaseFragment
import kartollika.recipesbook.common.ui.FavoriteAnimator
import kartollika.recipesbook.common.utils.injectViewModel
import kartollika.recipesbook.data.models.IngredientDetail
import kartollika.recipesbook.data.models.Recipe
import kartollika.recipesbook.features.MainActivity
import kartollika.recipesbook.features.PhotoViewFragment
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.Data
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.RecipeDetailInfoItem
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.RecipeDetailInfoItemHelper
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.RecipeInfoAdapter
import kartollika.recipesbook.features.viewmodels.RecipeDetailViewModel
import kotlinx.android.synthetic.main.recipe_detail_layout.*

class RecipeDetailFragment : BaseFragment() {

    companion object {
        fun getInstance(recipeId: Int): RecipeDetailFragment =
            RecipeDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt("RECIPE_ID", recipeId)
                }
            }
    }

    private val recipeInfoAdapter = RecipeInfoAdapter(RecipeInfoAdapter.DEFAULT_DIFF_CALLBACK)

    private val viewModel: RecipeDetailViewModel by injectViewModel { App.diManager.applicationComponent!!.recipeDetailViewModel }

    override fun getLayoutRes(): Int = R.layout.recipe_detail_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInfoAdapter()
        initListeners()
        initToolbar()

        Handler().post { initializeObservers() }

        val recipeId = arguments?.getInt("RECIPE_ID") ?: 0
        viewModel.loadRecipeById(recipeId)
    }

    private fun initInfoAdapter() {
        recipeDetailContentRecyclerView.apply {
            adapter = recipeInfoAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
    }

    private fun initListeners() {
        recipeDetailMakeFavoriteFab.setOnClickListener {
            viewModel.onSetFavoriteClicked()
        }
    }

    private fun initToolbar() {
        toolbar.setNavigationOnClickListener {
            (activity as MainActivity).navigateUpFullScreen()
        }
    }

    private fun initializeObservers() {
        viewModel.getRecipeDetail().observe(viewLifecycleOwner, Observer {
            fillRecipeInformation(it)
        })

        viewModel.getIsLoading().observe(viewLifecycleOwner, Observer {
            switchLoadingUiState(it)
        })

        viewModel.getIngredientsList().observe(viewLifecycleOwner, Observer {
            fillIngredientsInformation(it)
        })

        viewModel.getIsRecipeFavorite().observe(viewLifecycleOwner, Observer { isFavorite ->
            FavoriteAnimator.animateFavoriteView(recipeDetailMakeFavoriteFab, isFavorite)
            if (isFavorite) {
                recipeDetailMakeFavoriteFab.setImageResource(R.drawable.ic_star_white_24dp)
            } else {
                recipeDetailMakeFavoriteFab.setImageResource(R.drawable.ic_star_border_white_24dp)
            }
        })
    }

    private fun switchLoadingUiState(isLoading: Boolean) {
        if (isLoading) {
//            content.visibility = View.GONE
            recipeDetailContentLoadingProgressView.show()
        } else {
//            content.visibility = View.VISIBLE
            recipeDetailContentLoadingProgressView.hide()
        }
    }

    private fun fillRecipeInformation(it: Recipe) {
        Glide.with(this).asBitmap()
            .centerCrop().load(it.image).into(object : CustomTarget<Bitmap>() {

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    recipeDetailImage.setImageBitmap(resource)

                    Palette.Builder(resource).setRegion(0, 0, resource.width, 50).generate { palette ->
                        palette!!.getDarkVibrantColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.colorPrimaryDark
                            )
                        )
                            .also {
                                requireActivity().window.statusBarColor = it
                                collapsingToolbar?.setContentScrimColor(it)
                            }
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })

        collapsingToolbar.title = it.title
        recipeDetailImage.setOnClickListener { view ->
            (activity as MainActivity).navigateFullScreen(
                PhotoViewFragment.newInstance(it.image, it.title),
                sharedElements = mapOf(recipeDetailImage to "recipe_detail_image")
            )
        }

        recipeDetailCookingTime.text = getString(R.string.cooking_time, it.cookingTime)
        recipeDetailPricePerServing.text = getString(R.string.price_per_serving, it.pricePerServing)
        recipeDetailTotalServings.text = getString(R.string.total_servings, it.totalServings)
    }

    private fun fillIngredientsInformation(list: List<IngredientDetail>) {
        recipeInfoAdapter.submitList(mutableListOf(RecipeDetailInfoItem(Data(list), RecipeDetailInfoItemHelper.INFO_INGREDIENTS)))
//        recipeDetailIngredientsListCardView.visibility = View.VISIBLE
//        requiredIngredientsAdapter.setIngredientsList(list)
    }
}