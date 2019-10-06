package kartollika.recipesbook.features.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.NewThreadScheduler
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipesbook.R
import kartollika.recipesbook.data.models.Recipe
import kartollika.recipesbook.data.repository.RecipeDetailRepository
import kartollika.recipesbook.data.repository.RecipesRepository
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.Data
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.RecipeDetailInfoItem
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.RecipeDetailInfoItemHelper.INFO_LIST_BLOCK_EQUIPMENT
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.RecipeDetailInfoItemHelper.INFO_LIST_BLOCK_INGREDIENTS
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.RecipeDetailInfoItemHelper.INFO_TEXT
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.models.ImageTextModel
import kartollika.recipesbook.features.recipe_detail.adapters.recipe_info.models.ListBlockModel
import java.lang.ref.WeakReference
import javax.inject.Inject

class RecipeDetailViewModel
@Inject constructor(
    private val recipeDetailRepository: RecipeDetailRepository,
    private val searchRecipesRepository: RecipesRepository
) : ViewModel() {

    private var currentRecipeId = -1
    private val isLoadingLiveData = MutableLiveData<Boolean>()
    private val recipeDetail = MutableLiveData<Recipe>()
    private val compositeDisposable = CompositeDisposable()
    private val isRecipeFavoriteLiveData = MutableLiveData<Boolean>(false)
    private val recipeInfoAdapterListLiveData = MutableLiveData<List<RecipeDetailInfoItem>>()
    private val recipeInfoAdapterList = mutableListOf<RecipeDetailInfoItem>()
    private lateinit var context: WeakReference<Context>

    fun getRecipeDetail(): LiveData<Recipe> = recipeDetail
    fun getIsLoading(): LiveData<Boolean> = isLoadingLiveData
    fun getIsRecipeFavorite(): LiveData<Boolean> = isRecipeFavoriteLiveData
    fun getRecipeInfoAdapterList(): LiveData<List<RecipeDetailInfoItem>> =
        recipeInfoAdapterListLiveData

    fun loadRecipeById(context: Context, id: Int) {
        this.context = WeakReference(context)

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
                    parseRecipeInformationForAdapter(it)
                },
                onError = { it.printStackTrace() }
            )

    private fun parseRecipeInformationForAdapter(it: Recipe) {
        recipeDetail.postValue(it)

        parseCommonInformation(it)
        parseIngredientsInformation(it)
        parseEquipmentInformation(it)
    }

    private fun parseIngredientsInformation(recipe: Recipe) {
        if (recipe.requiredIngredients.isNotEmpty()) {
            insertNewInfoItem(
                2,
                ListBlockModel("Ingredients", recipe.requiredIngredients),
                INFO_LIST_BLOCK_INGREDIENTS
            )
        }
    }

    private fun parseCommonInformation(recipe: Recipe) {
        context.get()?.let { context ->
            Single.fromCallable {
                insertNewInfoItem(
                    0,
                    ImageTextModel(
                        context.getString(R.string.cooking_time, recipe.cookingTime),
                        R.drawable.ic_access_time_black_24dp
                    ),
                    INFO_TEXT
                )
                insertNewInfoItem(
                    1,
                    ImageTextModel(
                        context.getString(R.string.price_per_serving, recipe.pricePerServing),
                        R.drawable.abc_ic_star_black_36dp
                    ),
                    INFO_TEXT
                )
            }
                .subscribeOn(NewThreadScheduler())
                .subscribe()
        }
    }

    private fun parseEquipmentInformation(recipe: Recipe) {
        searchRecipesRepository.getRecipeRequiredEquipment(recipe.id)
            .subscribeOn(NewThreadScheduler())
            .subscribeBy(onSuccess = { list ->
                if (list.isNotEmpty()) {
                    insertNewInfoItem(
                        3,
                        ListBlockModel("Equipment", list),
                        INFO_LIST_BLOCK_EQUIPMENT
                    )
                }
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun onSetFavoriteClicked() {
        if (isRecipeFavoriteLiveData.value == true) {
            setRecipeUnfavorite()
        } else {
            setRecipeFavorite()
        }
    }

    private fun insertNewInfoItem(index: Int, any: Any, type: Int) {
        recipeInfoAdapterListLiveData.postValue(recipeInfoAdapterList.also {
            val element = wrapAnyToRecipeDetailInfoItem(any, type)
            if (index > it.size) {
                it.add(element)
            } else {
                it.add(index, element)
            }
        })
    }

    private fun loadIsRecipeFavorite(id: Int): Disposable? =
        recipeDetailRepository.isRecipeFavorite(id)
            .subscribeBy(
                onNext = { isRecipeFavoriteLiveData.postValue(it.isNotEmpty()) },
                onError = { it.printStackTrace() })

    private fun wrapAnyToRecipeDetailInfoItem(any: Any, type: Int) =
        RecipeDetailInfoItem(
            Data(
                any
            ), type
        )
}