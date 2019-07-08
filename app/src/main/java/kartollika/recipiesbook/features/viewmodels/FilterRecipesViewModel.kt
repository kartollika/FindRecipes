package kartollika.recipiesbook.features.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipiesbook.data.local.entities.IngredientEntity
import kartollika.recipiesbook.data.models.IngredientChosenType
import kartollika.recipiesbook.data.repository.RecipesFilterRepository
import javax.inject.Inject

class FilterRecipesViewModel
@Inject constructor(
    private val recipesFilterRepository: RecipesFilterRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val includedIngredientsList = MutableLiveData<List<IngredientEntity>>()
    private val excludedIngredientsList = MutableLiveData<List<IngredientEntity>>()
    private val intoleranceIngredientsList = MutableLiveData<List<IngredientEntity>>()
    private val queryText = MutableLiveData<String>()

    fun getQueryText(): LiveData<String> = queryText

    init {
        compositeDisposable.addAll(
            loadIncludedIngredients(),
            loadExcludedIngredients(),
            loadIntoleranceIngredients(),
            loadQueryRecipe()
        )
    }

    fun getIncludedIngredients(): LiveData<List<IngredientEntity>> = includedIngredientsList
    fun getExcludedIngredients(): LiveData<List<IngredientEntity>> = excludedIngredientsList
    fun getIntolerancesIngredients(): LiveData<List<IngredientEntity>> = intoleranceIngredientsList

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
        recipesFilterRepository.saveQueryRecipe(query)
    }

    private fun loadIncludedIngredients() =
        recipesFilterRepository.getIncludedIngredients()
            .subscribeBy(onNext = { includedIngredientsList.postValue(it) })

    private fun loadExcludedIngredients() =
        recipesFilterRepository.getExcludedIngredients()
            .subscribeBy(onNext = { excludedIngredientsList.postValue(it) }, onError = {})

    private fun loadIntoleranceIngredients() =
        recipesFilterRepository.getIntoleranceIngredients()
            .subscribeBy(onNext = { intoleranceIngredientsList.postValue(it) }, onError = {})

    private fun loadQueryRecipe() = recipesFilterRepository.getQueryRecipe()
        .subscribe { t -> queryText.postValue(t) }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}