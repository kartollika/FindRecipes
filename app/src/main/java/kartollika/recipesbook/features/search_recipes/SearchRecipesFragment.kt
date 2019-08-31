package kartollika.recipesbook.features.search_recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kartollika.recipesbook.App
import kartollika.recipesbook.R
import kartollika.recipesbook.common.base.BaseFragment
import kartollika.recipesbook.common.ui.ApplyingBottomSheetDialog
import kartollika.recipesbook.common.ui.EndlessScrollListener
import kartollika.recipesbook.common.ui.PaddingSpaceItemDecoration
import kartollika.recipesbook.common.utils.injectViewModel
import kartollika.recipesbook.data.models.Ranking
import kartollika.recipesbook.data.models.RecipePreview
import kartollika.recipesbook.features.MainActivity
import kartollika.recipesbook.features.recipe_detail.RecipeDetailFragment
import kartollika.recipesbook.features.search_recipes.adapters.search.RecipesSearchAdapter
import kotlinx.android.synthetic.main.search_recipe_item.view.*
import kotlinx.android.synthetic.main.search_recipes_layout.*

class SearchRecipesFragment : BaseFragment() {

    private val viewModel by injectViewModel { App.diManager.applicationComponent!!.searchRecipesViewModel }
    private lateinit var recipesSearchedAdapter: RecipesSearchAdapter

    private val endlessScrollListener = object : EndlessScrollListener() {
        override fun onLoadMoreItems() {
            viewModel.performComplexSearch(getTotal())
        }
    }

    override fun getLayoutRes(): Int = R.layout.search_recipes_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.diManager.applicationComponent?.inject(this)
        initAdapters()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (getView() != null) {
            initializeObservers()
            initViews()
            initEmptyView()
        }
    }

    private fun initEmptyView() {
        LayoutInflater.from(context).inflate(R.layout.empty_view, emptyViewContainer, true)
    }

    private fun initAdapters() {
        recipesSearchedAdapter =
            RecipesSearchAdapter(RecipesSearchAdapter.DEFAULT_DIFF_CALLBACK).apply {
                onRecipeActionListener = object : RecipesSearchAdapter.OnRecipeActionListener {
                    override fun onItemClicked(recipe: RecipePreview, view: View) {
                        (activity as MainActivity).navigateFullScreen(
                            RecipeDetailFragment.getInstance(recipe.id),
                            sharedElements = mapOf(view.recipeDetailImage to "recipe_detail_image")
                        )
                    }
                }
                onAdapterContentChangedListener =
                    object : RecipesSearchAdapter.OnAdapterContentChangedListener {
                        override fun onDataEmpty() {
                            emptyViewContainer.visibility = View.VISIBLE
                        }

                        override fun onDataNotEmpty() {
                            emptyViewContainer.visibility = View.GONE
                        }
                    }
            }
    }

    private fun initViews() {
        initListeners()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        searchRecipesRecyclerView.apply {
            addItemDecoration(
                PaddingSpaceItemDecoration(
                    requireContext(),
                    vertical = 8,
                    horizontal = 16
                )
            )
            addOnScrollListener(endlessScrollListener)
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recipesSearchedAdapter
        }
    }

    private fun initListeners() {
        fabOpenRecipesFilters.setOnClickListener {
            openFilters()
        }

        searchRecipesSwipeRefreshLayout.setOnRefreshListener {
            viewModel.performComplexSearch()
            endlessScrollListener.resetScrollingState()
        }

        sortingLayoutView.setOnClickListener {
            PopupMenu(requireContext(), sortingLayoutView).apply {
                inflate(R.menu.sorting_menu)
                setOnMenuItemClickListener {
                    val ranking: Ranking? =
                        when (it.itemId) {
                            R.id.sort_maximize_used_ingredients -> {
                                Ranking.MaxUsedIngredients
                            }
                            R.id.sort_minimize_missed_ingredients -> {
                                Ranking.MinMissingIngredients
                            }
                            R.id.sort_relevance -> {
                                Ranking.Relevance
                            }
                            else -> null
                        }
                    viewModel.applyRankingFilter(ranking!!)
                    true
                }
            }.show()
        }
    }

    private fun updateSortingIndicator(ranking: Ranking) {
        sortingIndicatorTextView.text = ranking.naming
        sortingIndicatorImageView.setImageResource(ranking.icon)
    }

    private fun openFilters() {
        val dialog = RecipesFiltersDialogFragment().apply {
            setCloseDialogListener(object : ApplyingBottomSheetDialog.OnCloseDialogListener {
                override fun onApply() {
                    viewModel.performComplexSearch()
                }

                override fun onCanceled() {
                }
            })
        }
        (activity as MainActivity).showDialogFragment(dialog)
    }

    private fun initializeObservers() {
        viewModel.getRecipes().observe(this, Observer {
            recipesSearchedAdapter.setRecipesList(it)
        })

        viewModel.getRefreshingEvent().observe(this, Observer {
            if (!it.hasBeenHandled) {
                searchRecipesSwipeRefreshLayout.isRefreshing =
                    it.peekContent() == LoadingState.Loading
            }
        })

        viewModel.getRanking().observe(this, Observer {
            updateSortingIndicator(it)
        })

        viewModel.getErrorObservable().observe(this, Observer {
            if (!it.hasBeenHandled) {

            }
            Snackbar.make(fabOpenRecipesFilters, it.peekContent(), Snackbar.LENGTH_LONG)
                .setAction("Retry") { viewModel.performComplexSearch() }.show()
        })
    }

    companion object {
        fun newInstance() = SearchRecipesFragment()
    }
}