package kartollika.recipiesbook.data.remote.search

import io.reactivex.Single
import kartollika.recipiesbook.data.remote.NetworkConstants
import kartollika.recipiesbook.data.remote.NetworkConstants.Companion.excludeIngredients_QUERY
import kartollika.recipiesbook.data.remote.NetworkConstants.Companion.includeIngredients_QUERY
import kartollika.recipiesbook.data.remote.NetworkConstants.Companion.intolerances_QUERY
import kartollika.recipiesbook.data.remote.NetworkConstants.Companion.limitLicense_QUERY
import kartollika.recipiesbook.data.remote.NetworkConstants.Companion.number_QUERY
import kartollika.recipiesbook.data.remote.NetworkConstants.Companion.offset_QUERY
import kartollika.recipiesbook.data.remote.NetworkConstants.Companion.ranking_QUERY
import kartollika.recipiesbook.data.remote.search.response.SearchRecipeComplexResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

//    ranking: Whether to
//    minimize missing ingredients (0),
//    maximize used ingredients (1) first, or
//    rank recipes by relevance (2).

    @GET(NetworkConstants.Search.recipesByIngredients)
    fun searchRecipesComplex(
        @Query(value = limitLicense_QUERY) limitLicense: Boolean = false,
        @Query(value = offset_QUERY) offset: Int = 0,
        @Query(value = number_QUERY) number: Int = 10,
        @Query(value = includeIngredients_QUERY) includedIngredients: String = "",
        @Query(value = excludeIngredients_QUERY) excludedIngredients: String = "",
        @Query(value = intolerances_QUERY) intolerances: String = "",
        @Query(value = ranking_QUERY) ranking: Int = 2
    ): Single<SearchRecipeComplexResponse>

}