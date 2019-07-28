package kartollika.recipesbook.features.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipesbook.common.reactive.Event
import kartollika.recipesbook.data.models.IngredientChosenType
import kartollika.recipesbook.data.models.IngredientSearch
import kartollika.recipesbook.data.repository.RecipesFilterRepository
import kartollika.recipesbook.features.settings.ClearCacheState
import javax.inject.Inject

class SettingsViewModel
@Inject constructor(
    private val context: Context,
    private val recipesFilterRepository: RecipesFilterRepository
) : ViewModel() {

    private val clearCacheLiveData = MutableLiveData<Event<ClearCacheState>>(Event(ClearCacheState.Uninitialized))

    fun getClearCacheDataObservable(): LiveData<Event<ClearCacheState>> = clearCacheLiveData

    fun clearImagesCache() {
        Single.fromCallable { context.cacheDir.deleteRecursively() }
            .subscribeOn(IoScheduler())
            .doOnSubscribe { clearCacheLiveData.postValue(Event(ClearCacheState.Running)) }
            .doOnSuccess { if (it) clearCacheLiveData.postValue(Event(ClearCacheState.Finished)) }
            .doOnError { clearCacheLiveData.postValue(Event(ClearCacheState.Error)) }
            .subscribeBy(onError = { it.printStackTrace() })
    }

    fun switchActiveIngredient(ingredient: String, type: IngredientChosenType, state: Boolean) {
        recipesFilterRepository.switchPredefinedIngredientState(
            IngredientSearch(
                ingredient,
                type
            ), state
        )
    }
}