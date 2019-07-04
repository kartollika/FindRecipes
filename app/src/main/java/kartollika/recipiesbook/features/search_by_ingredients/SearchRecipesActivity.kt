package kartollika.recipiesbook.features.search_by_ingredients

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import kartollika.recipiesbook.App
import kartollika.recipiesbook.R
import kartollika.recipiesbook.common.ui.BaseActivity
import kartollika.recipiesbook.common.utils.injectViewModel
import kartollika.recipiesbook.common.viewmodel.ViewModelFactory
import kartollika.recipiesbook.viewmodels.SearchRecipesViewModel
import kotlinx.android.synthetic.main.search_recipes.*
import javax.inject.Inject

class SearchRecipesActivity : BaseActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: SearchRecipesViewModel by lazy { this.injectViewModel<SearchRecipesViewModel>(factory) }

    override fun getLayoutRes(): Int = R.layout.search_recipes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.diManager.applicationComponent?.inject(this)

        initializeObservers()

        testLivedata.setOnClickListener { viewModel.includeIngredient("milk") }
    }

    private fun initializeObservers() {
        viewModel.getIngredients().observe(this,
            Observer { Toast.makeText(this, "Kek", Toast.LENGTH_SHORT).show() })

        viewModel.getRecipes().observe(this, Observer { })
    }

}