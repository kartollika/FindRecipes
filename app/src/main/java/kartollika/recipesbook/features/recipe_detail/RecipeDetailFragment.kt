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
import kartollika.recipesbook.common.ui.PaddingSpaceItemDecoration
import kartollika.recipesbook.common.utils.injectViewModel
import kartollika.recipesbook.data.models.Recipe
import kartollika.recipesbook.features.MainActivity
import kartollika.recipesbook.features.PhotoViewFragment
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
        viewModel.loadRecipeById(requireContext(), recipeId)
    }

    private fun initInfoAdapter() {
        recipeDetailContentRecyclerView.apply {
            addItemDecoration(PaddingSpaceItemDecoration(context, 8, 8))
            adapter = recipeInfoAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.color_surface)
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
        viewModel.getRecipeDetail().observe(this, Observer {
            fillRecipeInformation(it)
        })

        viewModel.getIsLoading().observe(this, Observer {
            switchLoadingUiState(it)
        })


        viewModel.getIsRecipeFavorite().observe(this, Observer { isFavorite ->
            if (isFavorite) {
                recipeDetailMakeFavoriteFab.setImageResource(R.drawable.ic_star_white_24dp)
            } else {
                recipeDetailMakeFavoriteFab.setImageResource(R.drawable.ic_star_border_white_24dp)
            }

            Handler().post {
                FavoriteAnimator.animateFavoriteView(recipeDetailMakeFavoriteFab, isFavorite)
            }
        })

        viewModel.getRecipeInfoAdapterList().observe(this, Observer {
            recipeInfoAdapter.submitList(it)
            recipeInfoAdapter.notifyDataSetChanged()
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

    private fun fillRecipeInformation(recipe: Recipe) {
        Glide.with(this).asBitmap()
            .centerCrop().load(recipe.image).into(object : CustomTarget<Bitmap>() {

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    recipeDetailImage.setImageBitmap(resource)

                    Palette.Builder(resource).setRegion(0, 0, resource.width, 50).generate { palette ->
                        palette!!.getDarkVibrantColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.color_surface
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

        collapsingToolbar.title = recipe.title
        recipeDetailImage.setOnClickListener { view ->
            (activity as MainActivity).navigateFullScreen(
                PhotoViewFragment.newInstance(recipe.image, recipe.title),
                sharedElements = mapOf(recipeDetailImage to "recipe_detail_image")
            )
        }
    }
}