package kartollika.recipiesbook.features.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipiesbook.data.models.Ingredient
import kartollika.recipiesbook.data.repository.RecipesFilterRepository
import kartollika.recipiesbook.data.repository.RecipesRepository
import javax.inject.Inject

class FilterRecipesViewModel
@Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val recipesFilterRepository: RecipesFilterRepository,
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable += loadIncludedIngredients()
    }

    private val includedIngredientsList = MutableLiveData<List<Ingredient>>()

    fun getIncludedIngredients(): LiveData<List<Ingredient>> = includedIngredientsList

    private fun loadIncludedIngredients() = recipesFilterRepository.getIncludedIngredients()
        .subscribeBy(onNext = { includedIngredientsList.postValue(it) }, onError = {})

    fun includeIngredient(ingredientsRawText: String) {
        recipesFilterRepository.addNewIngredient(ingredientsRawText)
    }

    fun excludeIngredient(ingredient: String) {
    }

}