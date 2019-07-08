package kartollika.recipesbook.features.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipesbook.common.reactive.Event
import kartollika.recipesbook.data.models.Ranking
import kartollika.recipesbook.data.models.Recipe
import kartollika.recipesbook.data.repository.RecipesFilterRepository
import kartollika.recipesbook.data.repository.RecipesRepository
import javax.inject.Inject

class SearchRecipesViewModel
@Inject constructor(
    private val repository: RecipesRepository,
    private val filterRepository: RecipesFilterRepository
) : ViewModel() {

    private val recipes: MutableLiveData<List<Recipe>> = MutableLiveData()
    private val recipesRefreshingEvent = MutableLiveData<Event<Boolean>>()
    private val ranking = MutableLiveData<Ranking>()
    private var compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.addAll(
            loadCurrentRanking()
        )
    }

    fun getRecipes(): LiveData<List<Recipe>> = recipes

    fun getRefreshingEvent(): LiveData<Event<Boolean>> = recipesRefreshingEvent

    fun getRanking(): LiveData<Ranking> = ranking

    private fun loadCurrentRanking(): Disposable =
        filterRepository.getRankingObservable()
            .subscribe { rank: Int ->
                ranking.postValue(
                    Ranking.fromRankingValue(rank)
                )
            }


    fun performComplexSearch() {
        recipesRefreshingEvent.postValue(Event(true))
        repository.searchRecipesComplex()
            .subscribeOn(IoScheduler())
            .doOnSuccess { t -> Log.d("Obs", t.size.toString()) }
            .subscribeBy(onSuccess = { list ->
                run {
                    recipes.postValue(list)
                    recipesRefreshingEvent.postValue(Event(false))
                }
            })
    }

    fun applyRankingFilter(ranking: Ranking) {
        filterRepository.saveRankingFilter(ranking)
        performComplexSearch()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}