package kartollika.recipiesbook.data.remote

import kartollika.recipiesbook.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DefaultHttpClient
@Inject constructor(private val interceptor: Interceptor) {

    private companion object {
        private const val TIME_OUT_CONNECT = 10L
        private const val TIME_OUT_WRITE = 10L
        private const val TIME_OUT_READ = 0L
    }

    var httpClient: OkHttpClient? = null
        get() {
            if (field == null) {
                field = OkHttpClient.Builder().apply {
                    addInterceptor(interceptor)
                    connectTimeout(TIME_OUT_CONNECT, TimeUnit.SECONDS)
                    readTimeout(TIME_OUT_READ, TimeUnit.SECONDS)
                    writeTimeout(TIME_OUT_WRITE, TimeUnit.SECONDS)
                    retryOnConnectionFailure(true)

                    if (BuildConfig.DEBUG) {
                        val loggingInterceptor = HttpLoggingInterceptor()
                        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

                        addInterceptor(loggingInterceptor)
                    }
                }.build()
            }
            return field
        }
        private set

    fun destroy() {
        httpClient = null
    }
}