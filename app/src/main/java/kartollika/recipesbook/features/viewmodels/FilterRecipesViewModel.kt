package kartollika.recipesbook.features.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipesbook.data.local.entities.IngredientEntity
import kartollika.recipesbook.data.models.IngredientChosenType
import kartollika.recipesbook.data.models.IngredientSearch
import kartollika.recipesbook.data.repository.RecipesFilterRepository
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

    fun switchActiveIngredient(ingredient: String, type: IngredientChosenType, state: Boolean) {
        recipesFilterRepository.switchActivateIngredient(
            IngredientSearch(
                ingredient,
                type
            ), state)
    }

    fun deleteIngredient(ingredient: String, type: IngredientChosenType) {
        recipesFilterRepository.deleteIngredient(
            IngredientSearch(
                ingredient,
                type
            )
        )
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