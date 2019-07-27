package kartollika.recipesbook.features.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import kartollika.recipesbook.App
import kartollika.recipesbook.R
import kartollika.recipesbook.common.ui.ApplyingBottomSheetDialog
import kartollika.recipesbook.common.utils.injectViewModel
import kartollika.recipesbook.features.search_recipes.adapters.IngredientActionsListener
import kartollika.recipesbook.features.search_recipes.adapters.IngredientsAdapter
import kartollika.recipesbook.features.viewmodels.FilterRecipesViewModel
import kotlinx.android.synthetic.main.intolerance_define_bottom_sheet_layout.*


class IntoleranceDefineDialogFragment : ApplyingBottomSheetDialog() {

    private val viewModel: FilterRecipesViewModel by injectViewModel {
        App.diManager.applicationComponent!!.filterRecipesViewModel
    }

    override fun getLayoutRes(): Int = R.layout.intolerance_define_bottom_sheet_layout

    private lateinit var intoleranceIngredientsAdapter: IngredientsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapters()
        initObservers()
        initListeners()
    }

    private fun initAdapters() {
        intoleranceIngredientsAdapter = IngredientsAdapter(requireContext(), chipGroup, false).apply {
            ingredientActionsListener = object : IngredientActionsListener {
                override fun onCheckedStateChanged(ingredient: String, isChecked: Boolean) {

                }

                override fun onDeleteAction(ingredient: String) {
                    // Empty method stub
                }
            }
        }
    }

    private fun initObservers() {
        viewModel.getIntolerancesIngredients().observe(viewLifecycleOwner, Observer {
            intoleranceIngredientsAdapter.setupIngredients(it)
        })
    }

    private fun initListeners() {
        intoleranceDefineApplyAction.setOnClickListener { super.onCloseDialogListener?.onApply() }
    }
}