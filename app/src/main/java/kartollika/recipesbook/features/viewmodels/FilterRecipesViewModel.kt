package kartollika.recipesbook.features.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipesbook.data.local.entities.mapToIngredientSearchModel
import kartollika.recipesbook.data.models.IngredientChosenType
import kartollika.recipesbook.data.models.IngredientSearch
import kartollika.recipesbook.data.repository.FilterRecipesRepository
import javax.inject.Inject

class FilterRecipesViewModel
@Inject constructor(
    private val filterRecipesRepository: FilterRecipesRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val includedIngredientsList = MutableLiveData<List<IngredientSearch>>()
    private val excludedIngredientsList = MutableLiveData<List<IngredientSearch>>()
    private val intoleranceIngredientsList = MutableLiveData<List<IngredientSearch>>()
    private val queryText = MutableLiveData<String>()
    private val usePredefinedIntolerance = MutableLiveData<Boolean>()

    init {
        compositeDisposable.addAll(
            loadIncludedIngredients(),
            loadExcludedIngredients(),
            loadIntoleranceIngredients(),
            loadQueryRecipe(),
            loadPredefinedState()
        )
    }

    fun getIncludedIngredients(): LiveData<List<IngredientSearch>> = includedIngredientsList
    fun getExcludedIngredients(): LiveData<List<IngredientSearch>> = excludedIngredientsList
    fun getIntolerancesIngredients(): LiveData<List<IngredientSearch>> = intoleranceIngredientsList
    fun getQueryText(): LiveData<String> = queryText
    fun getUsePredefinedState(): LiveData<Boolean> = usePredefinedIntolerance


    fun addNewIngredients(ingredient: String, type: IngredientChosenType) {
        filterRecipesRepository.addNewIngredients(ingredient, type)
    }

    fun switchActiveIngredient(ingredient: String, type: IngredientChosenType, state: Boolean) {
        filterRecipesRepository.switchNotPredefinedIngredientsState(
            IngredientSearch(
                ingredient,
                type
            ), state
        )
    }

    fun deleteIngredient(ingredient: String, type: IngredientChosenType) {
        filterRecipesRepository.deleteIngredient(
            IngredientSearch(
                ingredient,
                type
            )
        )
    }

    fun onQueryInput(query: String) {
        filterRecipesRepository.saveQueryRecipe(query)
    }

    private fun loadIncludedIngredients() =
        filterRecipesRepository.getIncludedIngredients()
            .map { it.map { it.mapToIngredientSearchModel() } }
            .subscribeBy(onNext = { includedIngredientsList.postValue(it) })

    private fun loadExcludedIngredients() =
        filterRecipesRepository.getExcludedIngredients()
            .map { it.map { it.mapToIngredientSearchModel() } }
            .subscribeBy(onNext = { excludedIngredientsList.postValue(it) }, onError = {})

    private fun loadIntoleranceIngredients() =
        filterRecipesRepository.getIntoleranceIngredients()
            .map { it.map { it.mapToIngredientSearchModel() } }
            .subscribeBy(onNext = { intoleranceIngredientsList.postValue(it) }, onError = {})

    private fun loadQueryRecipe() =
        filterRecipesRepository.getQueryRecipe()
            .subscribe { t -> queryText.postValue(t) }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun onUsePredefinedIntoleranceChanged(state: Boolean) {
        filterRecipesRepository.saveUsePredefinedIntoleranceSetting(state)
    }

    private fun loadPredefinedState() =
        filterRecipesRepository.getUsePredefinedIntoleranceObservable()
            .subscribeBy(onNext = { usePredefinedIntolerance.postValue(it) })
}