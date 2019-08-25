package kartollika.recipesbook.data.remote.api.data

import io.reactivex.Single
import kartollika.recipesbook.data.remote.NetworkConstants
import kartollika.recipesbook.data.remote.api.data.response.RecipeEquipmentResponse
import kartollika.recipesbook.data.remote.api.data.response.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DataApi {
    @GET(NetworkConstants.Data.recipeInformation)
    fun getRecipeInformation(@Path(value = "id") id: Int): Single<RecipeResponse>

    @GET(NetworkConstants.Data.recipeRequiredEquipment)
    fun getRecipeRequiredEquipment(@Path(value = "id") id: Int): Single<RecipeEquipmentResponse>
}