package kartollika.recipesbook.features.search_recipes

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kartollika.recipesbook.App
import kartollika.recipesbook.R
import kartollika.recipesbook.common.ui.createSearchDelayedObservable
import kartollika.recipesbook.common.utils.injectViewModel
import kartollika.recipesbook.data.models.IngredientChosenType
import kartollika.recipesbook.features.adapters.IngredientActionsListener
import kartollika.recipesbook.features.adapters.IngredientsAdapter
import kartollika.recipesbook.features.viewmodels.FilterRecipesViewModel
import kotlinx.android.synthetic.main.input_dialog_layout.view.*
import kotlinx.android.synthetic.main.search_recipes_filter_layout.*


class RecipesFiltersDialogFragment : BottomSheetDialogFragment() {

    private val callbacks: MutableList<BottomSheetBehavior.BottomSheetCallback> = mutableListOf()
    private val viewModel: FilterRecipesViewModel by injectViewModel {
        App.diManager.applicationComponent!!.filterRecipesViewModel
    }

    private lateinit var includedIngredientsAdapter: IngredientsAdapter
    private lateinit var excludedIngredientsAdapter: IngredientsAdapter
    private lateinit var intoleranceIngredientsAdapter: IngredientsAdapter
    private val compositeDisposable = CompositeDisposable()
    private lateinit var queryDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_recipes_filter_layout, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet =
                d.findViewById(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            BottomSheetBehavior.from(bottomSheet!!).apply {
                isFitToContents = true
                peekHeight = 0
                state = BottomSheetBehavior.STATE_EXPANDED

                setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(p0: View, p1: Float) {
                        for (callback in callbacks) {
                            callback.onSlide(p0, p1)
                        }
                    }

                    override fun onStateChanged(p0: View, p1: Int) {
                        for (callback in callbacks) {
                            callback.onStateChanged(p0, p1)
                        }
                    }
                })
            }
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()
        initListeners()
        initObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun initAdapters() {
        includedIngredientsAdapter =
            IngredientsAdapter(requireContext(), includedIngredientsChipGroup, true).apply {
                ingredientActionsListener = object : IngredientActionsListener {
                    override fun onCheckedStateChanged(ingredient: String, isChecked: Boolean) {
                        viewModel.switchActiveIngredient(ingredient, isChecked)
                    }

                    override fun onDeleteAction(ingredient: String) {
                        viewModel.deleteIngredient(ingredient)
                    }
                }
            }

        excludedIngredientsAdapter =
            IngredientsAdapter(requireContext(), excludedIngredientsChipGroup, true).apply {
                ingredientActionsListener = object : IngredientActionsListener {
                    override fun onCheckedStateChanged(ingredient: String, isChecked: Boolean) {
                        viewModel.switchActiveIngredient(ingredient, isChecked)
                    }

                    override fun onDeleteAction(ingredient: String) {
                        viewModel.deleteIngredient(ingredient)
                    }
                }
            }

        intoleranceIngredientsAdapter =
            IngredientsAdapter(requireContext(), intoleranceIngredientsChipGroup, false).apply {
                ingredientActionsListener = object : IngredientActionsListener {
                    override fun onCheckedStateChanged(ingredient: String, isChecked: Boolean) {
                        viewModel.switchActiveIngredient(ingredient, isChecked)
                    }

                    override fun onDeleteAction(ingredient: String) {
                        // Empty stub
                    }
                }
            }
    }

    private fun initListeners() {
        addIncludedIngredientsTextView.setOnClickListener {
            createInputIngredientDialog(IngredientChosenType.Included).show()
        }

        addExcludedIngredientsTextView.setOnClickListener {
            createInputIngredientDialog(IngredientChosenType.Excluded).show()
        }

        compositeDisposable.add(recipeQueryFilterTextField.createSearchDelayedObservable(300L)
            .filter { it.isNotEmpty() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { viewModel.onQueryInput(it) }
        )

        saveFiltersActionView.setOnClickListener { dismiss() }
    }

    fun addCallback(callback: BottomSheetBehavior.BottomSheetCallback) {
        callbacks.add(callback)
    }

    private fun createInputIngredientDialog(type: IngredientChosenType): AlertDialog =
        AlertDialog.Builder(context).apply {
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
        }.create()

    private fun initObservers() {
        viewModel.getIncludedIngredients().observe(this, Observer {
            includedIngredientsAdapter.setupIngredients(it)
        })

        viewModel.getExcludedIngredients().observe(this, Observer {
            excludedIngredientsAdapter.setupIngredients(it)
        })

        viewModel.getIntolerancesIngredients().observe(this, Observer {
            intoleranceIngredientsAdapter.setupIngredients(it)
        })


        viewModel.getQueryText()
            .observe(this, Observer { s -> recipeQueryFilterTextField.setText(s.toString()) })
    }
}
