package kartollika.recipiesbook.data.remote.interceptors

import kartollika.recipiesbook.data.remote.secret.SecretConstants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class DefaultInterceptor
@Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.apply {
            addHeader("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
            addHeader("X-RapidAPI-Key", SecretConstants.API_KEY)
        }
        return chain.proceed(builder.build())
    }
}