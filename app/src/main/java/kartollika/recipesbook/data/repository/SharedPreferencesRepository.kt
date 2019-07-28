package kartollika.recipesbook.data.repository

import android.content.SharedPreferences
import io.reactivex.Single
import io.reactivex.internal.schedulers.IoScheduler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesRepository
@Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun getSharedPreferences() = sharedPreferences

    fun <T> easyPut(putAction: SharedPreferences.Editor.() -> SharedPreferences.Editor) {
        sharedPreferences.edit()
            .putAction()
            .apply()
    }

    fun getInt(key: String, defaultValue: Int = 0) =
        Single.fromCallable { sharedPreferences.getInt(key, defaultValue) }
            .subscribeOn(IoScheduler())

    fun putInt(key: String, value: Int) {
        sharedPreferences.edit()
            .putInt(key, value)
            .apply()
    }

    fun getString(key: String, defaultValue: String = "") =
        Single.fromCallable { sharedPreferences.getString(key, defaultValue) }
            .subscribeOn(IoScheduler())

    fun putString(key: String, value: String) {
        easyPut<String> { this.putString(key, value) }
    }

    fun getBoolean(key: String, value: Boolean) =
        Single.fromCallable { sharedPreferences.getBoolean(key, value) }
            .subscribeOn(IoScheduler())

    fun putBoolean(key: String, value: Boolean) {
        easyPut<String> { this.putBoolean(key, value) }
    }
}