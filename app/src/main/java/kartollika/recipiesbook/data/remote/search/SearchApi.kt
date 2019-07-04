package kartollika.recipiesbook.data.remote.search

import io.reactivex.Single
import kartollika.recipiesbook.data.remote.NetworkConstants
import kartollika.recipiesbook.data.remote.NetworkConstants.Companion.IGNOREPANTRY_QUERY
import kartollika.recipiesbook.data.remote.NetworkConstants.Companion.INGREDIENTS_QUERY
import kartollika.recipiesbook.data.remote.NetworkConstants.Companion.NUMBER_QUERY
import kartollika.recipiesbook.data.remote.NetworkConstants.Companion.RANKING_QUERY
import kartollika.recipiesbook.data.remote.search.response.RecipeByIngredientResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET(NetworkConstants.Search.recipesByIngredients)
    fun searchRecipesByIngredients(
        @Query(INGREDIENTS_QUERY) ingredients: String,
        @Query(NUMBER_QUERY) number: Int = 5,
        @Query(
            IGNOREPANTRY_QUERY
        ) ignorePantry: Boolean = true,
        @Query(
            RANKING_QUERY
        ) ranking: Int = 1
    ): Single<List<RecipeByIngredientResponse>>
}