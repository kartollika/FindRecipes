package kartollika.recipesbook.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import kartollika.recipesbook.data.remote.DefaultHttpClient
import kartollika.recipesbook.data.remote.NetworkConstants
import kartollika.recipesbook.data.remote.data.DataApi
import kartollika.recipesbook.data.remote.extract.ExtractApi
import kartollika.recipesbook.data.remote.interceptors.DefaultInterceptor
import kartollika.recipesbook.data.remote.search.SearchApi
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
            return GsonBuilder().create()
        }

        @Provides
        @JvmStatic
        fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
            GsonConverterFactory.create(gson)

        @Provides
        @JvmStatic
        fun provideRxJavaCallAdapter(): RxJava2CallAdapterFactory =
            RxJava2CallAdapterFactory.create()

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
        fun provideRecipesApi(retrofit: Retrofit): SearchApi {
            return retrofit.create(SearchApi::class.java)
        }

        @Provides
        @JvmStatic
        fun provideExtractApi(retrofit: Retrofit): ExtractApi {
            return retrofit.create(ExtractApi::class.java)
        }

        @Provides
        @JvmStatic
        fun provideDataApi(retrofit: Retrofit): DataApi {
            return retrofit.create(DataApi::class.java)
        }
    }

    @Binds
    abstract fun provideDefaultInterceptor(interceptor: DefaultInterceptor): Interceptor
}