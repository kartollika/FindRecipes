package kartollika.recipiesbook.data.remote.recipies

import io.reactivex.Single
import kartollika.recipiesbook.data.remote.NetworkConstants
import kartollika.recipiesbook.data.remote.recipies.request.RecipeByIngredientRequest
import kartollika.recipiesbook.data.remote.recipies.response.RecipeByIngredientResponse
import retrofit2.http.GET

interface RecipiesApi {

    @GET(NetworkConstants.Recipies.recipesByIngredients)
    fun searchRecipiesByIngredients(request: RecipeByIngredientRequest): Single<List<RecipeByIngredientResponse>>
}