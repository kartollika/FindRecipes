package kartollika.recipesbook.features.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipesbook.data.models.IngredientDetail
import kartollika.recipesbook.data.models.Recipe
import kartollika.recipesbook.data.repository.RecipesRepository
import javax.inject.Inject

class RecipeDetailViewModel
@Inject constructor(
    private val searchRecipesRepository: RecipesRepository
) : ViewModel() {

    private val isLoadingLiveData = MutableLiveData<Boolean>()
    private val recipeDetail = MutableLiveData<Recipe>()
    private val recipeIngredientsLiveData = MutableLiveData<List<IngredientDetail>>()
    private val compositeDisposable = CompositeDisposable()

    fun getRecipeDetail(): LiveData<Recipe> = recipeDetail
    fun getIsLoading(): LiveData<Boolean> = isLoadingLiveData
    fun getIngredientsList(): LiveData<List<IngredientDetail>> = recipeIngredientsLiveData

    fun loadRecipeById(id: Int) {
        isLoadingLiveData.postValue(false)
        compositeDisposable.addAll(
            startLoadRecipeData(id)
        )
    }

    private fun startLoadRecipeData(id: Int): Disposable =
        searchRecipesRepository.getRecipeMainInformation(id)
            .doOnEvent { _, _ -> isLoadingLiveData.postValue(false) }
            .subscribeBy(
                onSuccess = {
                    recipeDetail.postValue(it)
                    recipeIngredientsLiveData.postValue(it.requiredIngredients)
                }
            )

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}