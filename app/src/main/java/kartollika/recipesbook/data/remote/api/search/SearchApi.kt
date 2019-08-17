package kartollika.recipesbook.data.remote.api.search

import io.reactivex.Single
import kartollika.recipesbook.data.remote.NetworkConstants
import kartollika.recipesbook.data.remote.NetworkConstants.Companion.addRecipeInformation_QUERY
import kartollika.recipesbook.data.remote.NetworkConstants.Companion.excludeIngredients_QUERY
import kartollika.recipesbook.data.remote.NetworkConstants.Companion.includeIngredients_QUERY
import kartollika.recipesbook.data.remote.NetworkConstants.Companion.intolerances_QUERY
import kartollika.recipesbook.data.remote.NetworkConstants.Companion.limitLicense_QUERY
import kartollika.recipesbook.data.remote.NetworkConstants.Companion.number_QUERY
import kartollika.recipesbook.data.remote.NetworkConstants.Companion.offset_QUERY
import kartollika.recipesbook.data.remote.NetworkConstants.Companion.query_QUERY
import kartollika.recipesbook.data.remote.NetworkConstants.Companion.ranking_QUERY
import kartollika.recipesbook.data.remote.api.search.response.SearchRecipeComplexResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

//    ranking: Whether to
//    minimize missing ingredients (0),
//    maximize used ingredients (1) first, or
//    rank recipes by relevance (2).

    @GET(NetworkConstants.Search.recipesByIngredients)
    fun searchRecipesComplex(
        @Query(limitLicense_QUERY) limitLicense: Boolean = false,
        @Query(offset_QUERY) offset: Int = 0,
        @Query(number_QUERY) number: Int = 10,
        @Query(includeIngredients_QUERY) includedIngredients: String = "",
        @Query(excludeIngredients_QUERY) excludedIngredients: String = "",
        @Query(intolerances_QUERY) intolerances: String = "",
        @Query(ranking_QUERY) ranking: Int = 2,
        @Query(query_QUERY) query: String = "",
        @Query(addRecipeInformation_QUERY) addRecipeInformation: Boolean = true
    ): Single<SearchRecipeComplexResponse>

}