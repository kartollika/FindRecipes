package kartollika.recipiesbook.features.search_recipes

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kartollika.recipiesbook.App
import kartollika.recipiesbook.R
import kartollika.recipiesbook.common.base.BaseFragment
import kartollika.recipiesbook.common.utils.injectViewModel
import kartollika.recipiesbook.data.models.Ranking
import kartollika.recipiesbook.features.adapters.RecipesSearchAdapter
import kotlinx.android.synthetic.main.search_recipes_layout.*

class SearchRecipesFragment : BaseFragment() {

    private val viewModel by injectViewModel { App.diManager.applicationComponent!!.searchRecipesViewModel }

    private lateinit var recipesSearchedAdapter: RecipesSearchAdapter

    override fun getContentView(): Int = R.layout.search_recipes_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.diManager.applicationComponent?.inject(this)
        initializeObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initRecyclerView()

    }

    private fun initRecyclerView() {
        recipesSearchedAdapter = RecipesSearchAdapter(RecipesSearchAdapter.DEFAULT_DIFF_CALLBACK)

        recipesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recipesSearchedAdapter
        }
    }

    private fun initListeners() {
        fabOpenRecipesFilters.setOnClickListener {
            openFilters()
        }

        testSearchRecipes.setOnClickListener {
            viewModel.performComplexSearch()
        }

        sortingLayoutView.setOnClickListener {
            PopupMenu(requireContext(), sortingLayoutView).apply {
                inflate(R.menu.sorting_menu)
                setOnMenuItemClickListener {
                    lateinit var ranking: Ranking
                    updateSortingIndicator(it.title, it.icon)
                    when (it.itemId) {
                        R.id.sort_maximize_used_ingredients -> {
                            viewModel.applyRankingFilter(Ranking.MaxUsedIngredients)
                            true
                        }
                        R.id.sort_minimize_missed_ingredients -> {
                            viewModel.applyRankingFilter(Ranking.MinMissingIngredients)
                            true
                        }
                        R.id.sort_relevance -> {
                            viewModel.applyRankingFilter(Ranking.Relevance)
                            true
                        }
                        else -> false
                    }

                }
            }.show()
        }
    }

    private fun updateSortingIndicator(sortTitle: CharSequence, sortIcon: Drawable?) {
        sortingIndicatorTextView.text = sortTitle
        sortingIndicatorImageView.setImageDrawable(sortIcon)
    }

    private fun openFilters() {
        RecipesFiltersDialogFragment().apply {
            addCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {
                }

                override fun onStateChanged(p0: View, state: Int) {
                    if (state == BottomSheetBehavior.STATE_COLLAPSED) {
                        dismiss()
                    }

                    if (state == BottomSheetBehavior.STATE_HIDDEN) {
                        viewModel.performComplexSearch()
                    }
                }
            })
        }.show(childFragmentManager, "FiltersDialog")
    }

    private fun initializeObservers() {
        viewModel.getRecipes()
            .observe(this, Observer { recipesSearchedAdapter.setRecipesList(it) })
    }

}