package kartollika.recipiesbook.data.repository.recipies

import kartollika.recipiesbook.data.remote.recipies.RecipesApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesRepository
@Inject constructor(private val recipesApi: RecipesApi) {

    fun searchByIngredients(ingredients: String) =
        recipesApi.searchRecipesByIngredients(ingredients)
            .map { t -> t.map { it.mapToRecipeModel() } }

}