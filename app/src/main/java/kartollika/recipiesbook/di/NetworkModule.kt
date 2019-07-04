package kartollika.recipiesbook.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import kartollika.recipiesbook.data.remote.DefaultHttpClient
import kartollika.recipiesbook.data.remote.NetworkConstants
import kartollika.recipiesbook.data.remote.interceptors.DefaultInterceptor
import kartollika.recipiesbook.data.remote.recipies.RecipesApi
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
abstract class NetworkModule {

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun provideGson(): Gson {
            return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        }

        @Provides
        @JvmStatic
        fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)

        @Provides
        @JvmStatic
        fun provideRxJavaCallAdapter(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

        @Provides
        @JvmStatic
        fun provideRetrofit(
            httpClient: DefaultHttpClient,
            rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
            gsonConverterFactory: GsonConverterFactory
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl(NetworkConstants.BASE_URL)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .addConverterFactory(gsonConverterFactory)
                .client(httpClient.httpClient!!)
                .build()
        }

        @Provides
        @JvmStatic
        fun provideDefaultHttpClient(interceptor: DefaultInterceptor): DefaultHttpClient {
            return DefaultHttpClient(interceptor)
        }

        @Provides
        @JvmStatic
        fun provideRecipesApi(retrofit: Retrofit): RecipesApi {
            return retrofit.create(RecipesApi::class.java)
        }
    }

    @Binds
    abstract fun provideDefaultInterceptor(interceptor: DefaultInterceptor): Interceptor
}