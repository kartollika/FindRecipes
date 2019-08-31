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
import kartollika.recipesbook.data.repository.FilterRecipesRepository
import javax.inject.Inject

class SettingsViewModel
@Inject constructor(
    private val context: Context,
    private val filterRecipesRepository: FilterRecipesRepository
) : ViewModel() {

    private var disposable: Disposable? = null
    private val clearCacheLiveData = MutableLiveData<Event<Boolean>>()
    private val intoleranceIngredientsLiveData = MutableLiveData<List<IngredientSearch>>()

    init {
        disposable = filterRecipesRepository.getIntoleranceIngredients()
            .map { it.map { it.mapToIngredientSearchModel() } }
            .subscribeBy(onNext = { intoleranceIngredientsLiveData.postValue(it) }, onError = { it.printStackTrace() })
    }

    fun getIntoleranceIngredients(): LiveData<List<IngredientSearch>> = intoleranceIngredientsLiveData

    fun getClearCacheDataObservable(): LiveData<Event<Boolean>> = clearCacheLiveData

    fun clearImagesCache() {
        Single.fromCallable { context.cacheDir.deleteRecursively() }
            .subscribeOn(IoScheduler())
            .doOnSuccess { clearCacheLiveData.postValue(Event(it)) }
            .doOnError { clearCacheLiveData.postValue(Event(false)) }
            .subscribeBy(onError = { it.printStackTrace() })
    }

    fun switchActiveIngredient(ingredient: String, type: IngredientChosenType, state: Boolean) {
        filterRecipesRepository.switchPredefinedIngredientState(
            IngredientSearch(
                ingredient,
                type
            ), state
        )
    }
}