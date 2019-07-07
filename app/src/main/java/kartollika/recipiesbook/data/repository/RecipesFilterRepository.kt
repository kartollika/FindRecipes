package kartollika.recipiesbook.data.repository

import io.reactivex.Observable
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipiesbook.data.entities.Ingredient
import kartollika.recipiesbook.data.models.FoodItem
import kartollika.recipiesbook.data.models.IngredientChosenType
import kartollika.recipiesbook.data.remote.extract.ExtractApi
import kartollika.recipiesbook.data.remote.extract.response.mapToFoodItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesFilterRepository
@Inject constructor(
    private val ingredientsRepository: IngredientsRepository,
    private val extractApi: ExtractApi
) {
    fun getIncludedIngredients() = ingredientsRepository.getIncludedIngredients()

    fun getExcludedIngredients() = ingredientsRepository.getExcludedIngredients()

    fun addNewIngredients(ingredientRawText: String, type: IngredientChosenType) {
        parseFoodInText(ingredientRawText)
            .filter { it.tag == "ingredient" }
            .subscribeBy(onNext = { foodItem ->
                ingredientsRepository.addIngredient(
                    Ingredient(
                        foodItem.name,
                        type,
                        true
                    )
                )
            })
    }

    fun switchActivateIngredient(ingredient: String, state: Boolean) {
        ingredientsRepository.switchActivateIngredient(ingredient, state)
    }

    fun deleteIngredient(ingredient: String) {
        ingredientsRepository.deleteIngredient(ingredient)
    }

    private fun parseFoodInText(text: String): Observable<FoodItem> =
        extractApi.detectFoodInText(text)
            .subscribeOn(IoScheduler())
            .map { it.annotations }
            .flatMapIterable { it }
            .map { it.mapToFoodItem() }
}
