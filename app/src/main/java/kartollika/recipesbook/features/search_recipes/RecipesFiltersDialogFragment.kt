package kartollika.recipesbook.features.search_recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kartollika.recipesbook.App
import kartollika.recipesbook.R
import kartollika.recipesbook.common.ui.ApplyingBottomSheetDialog
import kartollika.recipesbook.common.ui.createSearchDelayedObservable
import kartollika.recipesbook.common.utils.injectViewModel
import kartollika.recipesbook.data.models.IngredientChosenType
import kartollika.recipesbook.features.search_recipes.adapters.filter.ChipActionListener
import kartollika.recipesbook.features.search_recipes.adapters.filter.IngredientsAdapter
import kartollika.recipesbook.features.viewmodels.FilterRecipesViewModel
import kotlinx.android.synthetic.main.input_dialog_layout.view.*
import kotlinx.android.synthetic.main.search_recipes_filter_layout.*


class RecipesFiltersDialogFragment : ApplyingBottomSheetDialog() {

    private val viewModel: FilterRecipesViewModel by injectViewModel {
        App.diManager.applicationComponent!!.filterRecipesViewModel
    }

    override fun getLayoutRes(): Int = R.layout.search_recipes_filter_layout

    private lateinit var includedIngredientsAdapter: IngredientsAdapter
    private lateinit var excludedIngredientsAdapter: IngredientsAdapter
    private lateinit var intoleranceIngredientsAdapter: IngredientsAdapter
    private lateinit var queryDisposable: Disposable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()
        initListeners()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        queryDisposable = startQueryReactiveListener()
    }

    override fun onPause() {
        super.onPause()
        queryDisposable.dispose()
    }

    private fun startQueryReactiveListener(): Disposable =
        recipeQueryFilterTextField.createSearchDelayedObservable(300L)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { viewModel.onQueryInput(it) }

    private fun initAdapters() {
        includedIngredientsAdapter =
            IngredientsAdapter(requireContext()).apply {
                isCloseIconVisible = true
                checkedPredicate = { it.isActiveNormal() }
                actionListener = object :
                    ChipActionListener {
                    override fun onCheckedStateChanged(name: String, isChecked: Boolean) {
                        viewModel.switchActiveIngredient(
                            name,
                            IngredientChosenType.Included,
                            isChecked
                        )
                    }

                    override fun onDeleteAction(name: String) {
                        viewModel.deleteIngredient(name, IngredientChosenType.Included)
                    }
                }
            }
        includedIngredientsChipGroup.setAdapter(includedIngredientsAdapter)

        excludedIngredientsAdapter =
            IngredientsAdapter(requireContext()).apply {
                isCloseIconVisible = true
                checkedPredicate = { it.isActiveNormal() }
                actionListener = object :
                    ChipActionListener {
                    override fun onCheckedStateChanged(name: String, isChecked: Boolean) {
                        viewModel.switchActiveIngredient(
                            name,
                            IngredientChosenType.Excluded,
                            isChecked
                        )
                    }

                    override fun onDeleteAction(name: String) {
                        viewModel.deleteIngredient(name, IngredientChosenType.Excluded)
                    }
                }
            }
        excludedIngredientsChipGroup.setAdapter(excludedIngredientsAdapter)

        intoleranceIngredientsAdapter =
            IngredientsAdapter(requireContext()).apply {
                isCloseIconVisible = false
                checkedPredicate = { it.isActiveNormal() }
                actionListener = object :
                    ChipActionListener {
                    override fun onCheckedStateChanged(name: String, isChecked: Boolean) {
                        viewModel.switchActiveIngredient(
                            name,
                            IngredientChosenType.Intolerance,
                            isChecked
                        )
                    }

                    override fun onDeleteAction(name: String) {
                        // Empty stub
                    }
                }
            }
        intoleranceIngredientsChipGroup.setAdapter(intoleranceIngredientsAdapter)
    }

    private fun initListeners() {
        addIncludedIngredientsTextView.setOnClickListener {
            createInputIngredientDialog(IngredientChosenType.Included).show()
        }

        addExcludedIngredientsTextView.setOnClickListener {
            createInputIngredientDialog(IngredientChosenType.Excluded).show()
        }

        saveFiltersActionView.setOnClickListener {
            dismiss()
            super.onCloseDialogListener?.onApply()
        }

        intoleranceIngredientsUsePredefinedCheckBox.setOnCheckedChangeListener { _, checked ->
            viewModel.onUsePredefinedIntoleranceChanged(checked)
        }
    }

    private fun createInputIngredientDialog(type: IngredientChosenType): AlertDialog.Builder =
        AlertDialog.Builder(context!!).apply {
            val dialogInnerView =
                LayoutInflater.from(context).inflate(R.layout.input_dialog_layout, null)
            val ingredientsTextField = dialogInnerView.inputDialogTextField

            setView(dialogInnerView)
            setPositiveButton("Add entered") { dialog, which ->
                viewModel.addNewIngredients(
                    ingredientsTextField.text.toString(), type
                )
            }
            setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        }

    private fun initObservers() {
        viewModel.getIncludedIngredients().observe(this, Observer {
            includedIngredientsAdapter.setupList(it)
        })

        viewModel.getExcludedIngredients().observe(this, Observer {
            excludedIngredientsAdapter.setupList(it)
        })

        viewModel.getIntolerancesIngredients().observe(this, Observer {
            intoleranceIngredientsAdapter.setupList(it)
        })

        viewModel.getQueryText()
            .observe(this, Observer { s -> recipeQueryFilterTextField.setText(s.toString()) })

        viewModel.getUsePredefinedState().observe(this, Observer {
            intoleranceIngredientsUsePredefinedCheckBox.isChecked = it
            intoleranceIngredientsChipGroup.visibility =
                if (it) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
        })
    }
}
