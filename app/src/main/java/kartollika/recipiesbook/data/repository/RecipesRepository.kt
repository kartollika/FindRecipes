package kartollika.recipiesbook.data.repository

import kartollika.recipiesbook.data.remote.search.SearchApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesRepository
@Inject constructor(
    private val searchApi: SearchApi
) {

    fun searchByIngredients(ingredients: String) =
        searchApi.searchRecipesByIngredients(ingredients)
            .map { it -> it.map { it.mapToRecipeModel() } }

}