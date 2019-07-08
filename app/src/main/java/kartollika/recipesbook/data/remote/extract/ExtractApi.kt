package kartollika.recipesbook.data.remote.extract

import io.reactivex.Observable
import kartollika.recipesbook.data.remote.NetworkConstants
import kartollika.recipesbook.data.remote.extract.response.DetectFoodResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ExtractApi {

    @FormUrlEncoded
    @POST(NetworkConstants.Extract.detectFoodInText)
    fun detectFoodInText(@Field(value = "text") text: String): Observable<DetectFoodResponse>
}