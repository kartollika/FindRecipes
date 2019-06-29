package kartollika.recipiesbook.data.repository.recipies

import kartollika.recipiesbook.common.utils.parseToSearchByIngredientsRequest
import kartollika.recipiesbook.data.remote.recipies.RecipiesApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipiesRepository
@Inject constructor(private val recipiesApi: RecipiesApi) {

    fun searchByIngredients(ingredients: List<String>) {
        recipiesApi.searchRecipiesByIngredients(ingredients.parseToSearchByIngredientsRequest())
    }
}