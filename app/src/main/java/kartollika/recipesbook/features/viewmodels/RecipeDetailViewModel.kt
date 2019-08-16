package kartollika.recipesbook.features.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipesbook.data.models.IngredientDetail
import kartollika.recipesbook.data.models.Recipe
import kartollika.recipesbook.data.repository.RecipeDetailRepository
import kartollika.recipesbook.data.repository.RecipesRepository
import javax.inject.Inject

class RecipeDetailViewModel
@Inject constructor(
    private val recipeDetailRepository: RecipeDetailRepository,
    private val searchRecipesRepository: RecipesRepository
) : ViewModel() {

    private var currentRecipeId = -1
    private val isLoadingLiveData = MutableLiveData<Boolean>()
    private val recipeDetail = MutableLiveData<Recipe>()
    private val recipeIngredientsLiveData = MutableLiveData<List<IngredientDetail>>()
    private val compositeDisposable = CompositeDisposable()
    private val isRecipeFavorite = MutableLiveData<Boolean>(false)

    fun getRecipeDetail(): LiveData<Recipe> = recipeDetail
    fun getIsLoading(): LiveData<Boolean> = isLoadingLiveData
    fun getIngredientsList(): LiveData<List<IngredientDetail>> = recipeIngredientsLiveData
    fun getIsRecipeFavorite(): LiveData<Boolean> = isRecipeFavorite

    fun loadRecipeById(id: Int) {
        currentRecipeId = id
        isLoadingLiveData.postValue(false)
        compositeDisposable.addAll(
            loadRecipeData(id),
            loadIsRecipeFavorite(id)
        )
    }

    private fun setRecipeFavorite() {
        recipeDetailRepository.addRecipeToFavorite(currentRecipeId)
    }

    private fun setRecipeUnfavorite() {
        recipeDetailRepository.removeRecipeFromFavorite(currentRecipeId)
    }

    private fun loadRecipeData(id: Int): Disposable =
        searchRecipesRepository.getRecipeMainInformation(id)
            .doOnEvent { _, _ -> isLoadingLiveData.postValue(false) }
            .subscribeBy(
                onSuccess = {
                    recipeDetail.postValue(it)
                    recipeIngredientsLiveData.postValue(it.requiredIngredients)
                },
                onError = { it.printStackTrace() }
            )

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun onSetFavoriteClicked() {
        if (isRecipeFavorite.value == true) {
            setRecipeUnfavorite()
        } else {
            setRecipeFavorite()
        }
    }

    private fun loadIsRecipeFavorite(id: Int): Disposable? =
        recipeDetailRepository.isRecipeFavorite(id)
            .subscribeBy(
                onNext = { isRecipeFavorite.postValue(it.isNotEmpty()) },
                onError = { it.printStackTrace() })
}