package kartollika.recipesbook.features.settings

import android.os.Bundle
import android.view.View
import kartollika.recipesbook.App
import kartollika.recipesbook.R
import kartollika.recipesbook.common.ui.ApplyingBottomSheetDialog
import kartollika.recipesbook.common.utils.injectViewModel
import kartollika.recipesbook.features.viewmodels.SettingsViewModel
import kotlinx.android.synthetic.main.intolerance_define_bottom_sheet_layout.*


class IntoleranceDefineDialogFragment : ApplyingBottomSheetDialog() {

    private val viewModel: SettingsViewModel by injectViewModel {
        App.diManager.applicationComponent!!.settingsViewModel
    }

    override fun getLayoutRes(): Int = R.layout.intolerance_define_bottom_sheet_layout

//    private lateinit var intoleranceIngredientsAdapter: BaseChipAdapter<IngredientSearch>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapters()
        initObservers()
        initListeners()
    }

    private fun initAdapters() {
       /* intoleranceIngredientsAdapter = IngredientsAdapter(requireContext()).apply {
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
        }*/
//        defineIntolerancesChipGroup.setAdapter(intoleranceIngredientsAdapter)
    }

    private fun initObservers() {
//        viewModel.getIntoleranceIngredients().observe(viewLifecycleOwner, Observer {
//            intoleranceIngredientsAdapter.setupList(it)
//        })
    }

    private fun initListeners() {
        intoleranceDefineApplyAction.setOnClickListener { super.onCloseDialogListener?.onApply() }
    }
}