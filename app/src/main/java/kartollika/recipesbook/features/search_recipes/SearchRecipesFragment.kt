package kartollika.recipesbook.features.search_recipes

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kartollika.recipesbook.App
import kartollika.recipesbook.R
import kartollika.recipesbook.common.base.BaseFragment
import kartollika.recipesbook.common.utils.injectViewModel
import kartollika.recipesbook.data.models.Ranking
import kartollika.recipesbook.data.models.RecipePreview
import kartollika.recipesbook.features.HubFragmentDirections
import kartollika.recipesbook.features.search_recipes.adapters.RecipesSearchAdapter
import kotlinx.android.synthetic.main.search_recipe_item.view.*
import kotlinx.android.synthetic.main.search_recipes_layout.*


class SearchRecipesFragment : BaseFragment() {

    private val viewModel by injectViewModel { App.diManager.applicationComponent!!.searchRecipesViewModel }
    private lateinit var recipesSearchedAdapter: RecipesSearchAdapter

    override fun getLayoutRes(): Int = R.layout.search_recipes_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.diManager.applicationComponent?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (getView() != null) {
            initializeObservers()
            initViews()
        }
    }

    private fun initViews() {
        initListeners()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recipesSearchedAdapter =
            RecipesSearchAdapter(RecipesSearchAdapter.DEFAULT_DIFF_CALLBACK).apply {
                onRecipeActionListener = object : RecipesSearchAdapter.OnRecipeActionListener {
                    override fun onItemClicked(recipe: RecipePreview, view: View) {
                        val extras = FragmentNavigator.Extras.Builder()
                            .addSharedElement(view.recipeDetailImage, "recipe_image").build()


                        val direction =
                            HubFragmentDirections.Action_hubFragment_to_recipeDetailFragment()
                                .setRecipeId(recipe.id)

                        Navigation.findNavController(activity!!, R.id.navHostFragment)
                            .navigate(direction, extras)
                    }
                }
            }

        searchRecipesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recipesSearchedAdapter
        }
    }

    private fun initListeners() {
        fabOpenRecipesFilters.setOnClickListener {
            openFilters()
        }

        searchRecipesSwipeRefreshLayout.setOnRefreshListener { viewModel.performComplexSearch() }

        sortingLayoutView.setOnClickListener {
            PopupMenu(requireContext(), linearLayout).apply {
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

//    private fun updateSortingIndicator(sortTitle: CharSequence, sortIcon: Drawable?) {
//        sortingIndicatorTextView.text = sortTitle
//        sortingIndicatorImageView.setImageDrawable(sortIcon)
//    }

    private fun updateSortingIndicator(ranking: Ranking) {
        sortingIndicatorTextView.text = ranking.naming
        sortingIndicatorImageView.setImageResource(ranking.icon)
    }

    private fun openFilters() {
        val filtersFragment = RecipesFiltersDialogFragment().apply {
            addCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {
                }

                override fun onStateChanged(p0: View, state: Int) {
                    if (state == BottomSheetBehavior.STATE_HIDDEN
                        || state == BottomSheetBehavior.STATE_COLLAPSED
                    ) {
                        dismiss()
                        viewModel.performComplexSearch()
                    }
                }
            })
        }
        filtersFragment.show(fragmentManager!!, "FiltersDialog")
    }

    private fun initializeObservers() {
        viewModel.getRecipes()
            .observe(viewLifecycleOwner, Observer { recipesSearchedAdapter.setRecipesList(it) })

        viewModel.getRefreshingEvent().observe(viewLifecycleOwner, Observer {
            searchRecipesSwipeRefreshLayout.isRefreshing = it.getContentIfNotHandled() ?: false
        })

        viewModel.getRanking().observe(viewLifecycleOwner, Observer {
            updateSortingIndicator(it)
        })
    }

}