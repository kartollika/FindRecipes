package kartollika.recipesbook.features.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import kartollika.recipesbook.App
import kartollika.recipesbook.R
import kartollika.recipesbook.common.ui.ApplyingBottomSheetDialog
import kartollika.recipesbook.common.utils.injectViewModel
import kartollika.recipesbook.data.models.IngredientChosenType
import kartollika.recipesbook.features.search_recipes.adapters.filter.ChipActionListener
import kartollika.recipesbook.features.search_recipes.adapters.filter.IngredientsAdapter
import kartollika.recipesbook.features.viewmodels.SettingsViewModel
import kotlinx.android.synthetic.main.intolerance_define_bottom_sheet_layout.*


class IntoleranceDefineDialogFragment : ApplyingBottomSheetDialog() {

    private val viewModel: SettingsViewModel by injectViewModel {
        App.diManager.applicationComponent!!.settingsViewModel
    }

    override fun getLayoutRes(): Int = R.layout.intolerance_define_bottom_sheet_layout

    private lateinit var intoleranceIngredientsAdapter: IngredientsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapters()
        initObservers()
    }

    private fun initAdapters() {
        intoleranceIngredientsAdapter = IngredientsAdapter(requireContext()).apply {
            isCloseIconVisible = false
            checkedPredicate = { it.isActivePredefined() }
            actionListener = object :
                ChipActionListener {
                override fun onCheckedStateChanged(name: String, isChecked: Boolean) {
                    viewModel.switchActiveIngredient(name, IngredientChosenType.Intolerance, isChecked)
                }

                override fun onDeleteAction(name: String) {
                    // Empty method stub
                }
            }
            defineIntolerancesChipGroup.setAdapter(this)
        }
    }

    private fun initObservers() {
        viewModel.getIntoleranceIngredients().observe(viewLifecycleOwner, Observer {
            intoleranceIngredientsAdapter.setupList(it)
        })
    }
}