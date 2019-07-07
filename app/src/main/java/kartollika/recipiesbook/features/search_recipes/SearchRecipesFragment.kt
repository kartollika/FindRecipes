package kartollika.recipiesbook.features.search_recipes

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kartollika.recipiesbook.App
import kartollika.recipiesbook.R
import kartollika.recipiesbook.common.base.BaseFragment
import kartollika.recipiesbook.common.utils.injectViewModel
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
    }

    private fun openFilters() {
        TestBottomSheetDialogFragment().apply {
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
        viewModel.getRecipes().observe(this, Observer { recipesSearchedAdapter.setRecipesList(it) })
    }

}