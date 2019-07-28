package kartollika.recipesbook.features.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipesbook.common.reactive.Event
import kartollika.recipesbook.data.local.entities.mapToIngredientSearchModel
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

    private var disposable: Disposable? = null
    private val clearCacheLiveData = MutableLiveData<Event<ClearCacheState>>(Event(ClearCacheState.Uninitialized))
    private val intoleranceIngredientsLiveData = MutableLiveData<List<IngredientSearch>>()

    init {
        disposable = recipesFilterRepository.getIntoleranceIngredients()
            .map { it.map { it.mapToIngredientSearchModel() } }
            .subscribeBy(onNext = { intoleranceIngredientsLiveData.postValue(it) }, onError = { it.printStackTrace() })
    }

    fun getIntoleranceIngredients(): LiveData<List<IngredientSearch>> = intoleranceIngredientsLiveData

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