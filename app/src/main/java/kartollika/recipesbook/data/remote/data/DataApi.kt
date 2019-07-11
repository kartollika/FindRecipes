package kartollika.recipesbook.data.remote.data

import io.reactivex.Single
import kartollika.recipesbook.data.remote.NetworkConstants
import kartollika.recipesbook.data.remote.data.response.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DataApi {


    @GET(NetworkConstants.Data.recipeInformation)
    fun getRecipeInformation(@Path(value = "id") id: Int): Single<RecipeResponse>
}