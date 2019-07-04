package kartollika.recipiesbook.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kartollika.recipiesbook.data.models.Recipe
import kartollika.recipiesbook.data.repository.recipies.RecipesRepository
import javax.inject.Inject

class SearchRecipesViewModel
@Inject constructor(
    private val repository: RecipesRepository
) : ViewModel() {

    private val ingredients = mutableListOf<String>()

    private val ingredientsObservable: MutableLiveData<List<String>> = MutableLiveData()
    private val recipes: MutableLiveData<List<Recipe>> = MutableLiveData()

    fun getIngredients(): LiveData<List<String>> = ingredientsObservable
    fun getRecipes(): LiveData<List<Recipe>> = recipes

    fun includeIngredient(ingredient: String) {
        ingredients.add(ingredient)
        ingredientsObservable.postValue(ingredients)
    }

    fun excludeIngredient(ingredient: String) {
        ingredients.remove(ingredient)
        ingredientsObservable.postValue(ingredients)
    }

    fun performSearchByIngredients() {
        repository.searchByIngredients(ingredients.joinToString(","))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {
                recipes.postValue(it)
            })
    }
}