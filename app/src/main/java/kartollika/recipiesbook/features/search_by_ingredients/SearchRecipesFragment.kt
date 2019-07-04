package kartollika.recipiesbook.features.search_by_ingredients

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kartollika.recipiesbook.App
import kartollika.recipiesbook.R
import kartollika.recipiesbook.common.ui.BaseFragment
import kartollika.recipiesbook.common.utils.injectViewModel
import kotlinx.android.synthetic.main.search_recipes_layout.*

class SearchRecipesFragment : BaseFragment() {

    override fun getContentView(): Int = R.layout.search_recipes_layout

    private val viewModel by injectViewModel { App.diManager.applicationComponent!!.searchRecipesViewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.diManager.applicationComponent?.inject(this)
        initializeObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        fabOpenRecipesFilters.setOnClickListener {
            openFilters()
        }
    }

    private fun openFilters() {
        TestBottomSheetDialogFragment().apply {
            addCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {
                }

                override fun onStateChanged(p0: View, p1: Int) {
                    if (p1 == BottomSheetBehavior.STATE_COLLAPSED) {
                        dismiss()
                    }
                }
            })
        }.show(childFragmentManager, "FiltersDialog")
    }

    private fun initializeObservers() {
        viewModel.getIngredients().observe(this,
            Observer { })

        viewModel.getRecipes().observe(this, Observer { })
    }

}