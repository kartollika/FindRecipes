package kartollika.recipiesbook.features.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipiesbook.data.entities.Ingredient
import kartollika.recipiesbook.data.models.IngredientChosenType
import kartollika.recipiesbook.data.repository.RecipesFilterRepository
import javax.inject.Inject

class FilterRecipesViewModel
@Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val recipesFilterRepository: RecipesFilterRepository
    ) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val includedIngredientsList = MutableLiveData<List<Ingredient>>()
    private val excludedIngredientsList = MutableLiveData<List<Ingredient>>()
    private val intoleranceIngredientsList = MutableLiveData<List<Ingredient>>()

    init {
        compositeDisposable.addAll(
            loadIncludedIngredients(),
            loadExcludedIngredients()
        )
    }

    fun getIncludedIngredients(): LiveData<List<Ingredient>> = includedIngredientsList
    fun getExcludedIngredients(): LiveData<List<Ingredient>> = excludedIngredientsList
    fun getIntolerancesIngredients(): LiveData<List<Ingredient>> = intoleranceIngredientsList

    fun addNewIngredients(ingredient: String, type: IngredientChosenType) {
        recipesFilterRepository.addNewIngredients(ingredient, type)
    }

    fun switchActiveIngredient(ingredient: String, state: Boolean) {
        recipesFilterRepository.switchActivateIngredient(ingredient, state)
    }

    fun deleteIngredient(ingredient: String) {
        recipesFilterRepository.deleteIngredient(ingredient)
    }

    fun onQueryInput(query: String) {

    }

    private fun loadIncludedIngredients() =
        recipesFilterRepository.getIncludedIngredients()
            .subscribeBy(onNext = { includedIngredientsList.postValue(it) })

    private fun loadExcludedIngredients() =
        recipesFilterRepository.getExcludedIngredients()
            .subscribeBy(onNext = { excludedIngredientsList.postValue(it) }, onError = {})
}