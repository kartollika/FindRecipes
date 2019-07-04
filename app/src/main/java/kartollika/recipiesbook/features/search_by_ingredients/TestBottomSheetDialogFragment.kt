package kartollika.recipiesbook.features.search_by_ingredients

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.ChipGroup
import kartollika.recipiesbook.App
import kartollika.recipiesbook.R
import kartollika.recipiesbook.common.utils.injectViewModel
import kartollika.recipiesbook.features.adapters.IngredientActionsListener
import kartollika.recipiesbook.features.adapters.IngredientsAdapter
import kotlinx.android.synthetic.main.input_dialog_layout.view.*
import kotlinx.android.synthetic.main.search_recipes_filter_layout.*


class TestBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val callbacks: MutableList<BottomSheetBehavior.BottomSheetCallback> = mutableListOf()
    private val viewModel by injectViewModel { App.diManager.applicationComponent!!.filterRecipesViewModel }

    private val includedIngredientsViewGroup: ChipGroup by lazy { includedIngredientsChipGroup }
    private val addIncludedIngredientView by lazy { addNewIncludedIngredientsTextView }
    private lateinit var ingredientsAdapter: IngredientsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

        initObservers()
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

        ingredientsAdapter =
            IngredientsAdapter(requireContext(), includedIngredientsChipGroup, true).apply {
                ingredientActionsListener = object : IngredientActionsListener {
                    override fun onCheckedStateChanged(ingredient: String, isChecked: Boolean) {
                        Log.d("CHIP", "$ingredient checked state is $isChecked")
                    }

                    override fun onDeleteAction(ingredient: String) {
                        Log.d("CHIP", "$ingredient was removed")
                    }
                }
            }
        initListeners()
    }

    private fun initListeners() {
        addIncludedIngredientView.setOnClickListener {
            createInputIngredientDialog().show()
        }
    }

    fun addCallback(callback: BottomSheetBehavior.BottomSheetCallback) {
        callbacks.add(callback)
    }

    private fun createInputIngredientDialog(): AlertDialog =
        AlertDialog.Builder(context).apply {
            val dialogInnerView =
                LayoutInflater.from(context).inflate(R.layout.input_dialog_layout, null)
            val ingredientsTextField = dialogInnerView.inputDialogTextField

            setView(dialogInnerView)
            setPositiveButton("Add entered") { dialog, which ->
                viewModel.includeIngredient(
                    ingredientsTextField.text.toString()
                )
            }
            setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        }.create()

    private fun initObservers() {
        viewModel.getIncludedIngredients().observe(this, Observer {
            Log.d("OBSERVER", "size of list ${it.size}")
            ingredientsAdapter.setupIngredients(it)
        })
    }


}