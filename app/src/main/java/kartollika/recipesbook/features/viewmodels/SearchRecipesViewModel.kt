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
import kartollika.recipesbook.data.models.RecipePreview
import kartollika.recipesbook.data.repository.RecipesFilterRepository
import kartollika.recipesbook.data.repository.SearchRecipesRepository
import javax.inject.Inject

class SearchRecipesViewModel
@Inject constructor(
    private val repositorySearch: SearchRecipesRepository,
    private val filterRepository: RecipesFilterRepository
) : ViewModel() {

    private var recipesList = mutableListOf<RecipePreview>()
    private val recipes: MutableLiveData<List<RecipePreview>> = MutableLiveData()
    private val recipesRefreshingEvent = MutableLiveData<Event<Boolean>>()
    private val ranking = MutableLiveData<Ranking>()
    private var compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.addAll(
            loadCurrentRanking()
        )
    }

    fun getRecipes(): LiveData<List<RecipePreview>> = recipes

    fun getRefreshingEvent(): LiveData<Event<Boolean>> = recipesRefreshingEvent

    fun getRanking(): LiveData<Ranking> = ranking

    private fun loadCurrentRanking(): Disposable =
        filterRepository.getRankingObservable()
            .subscribe { rank: Int ->
                ranking.postValue(
                    Ranking.fromRankingValue(rank)
                )
            }


    fun performComplexSearch(offset: Int = 0) {
        recipesRefreshingEvent.postValue(Event(true))
        repositorySearch.searchRecipesComplex(offset)
            .subscribeOn(IoScheduler())
            .doOnSuccess { t -> Log.d("Obs", t.size.toString()) }
            .subscribeBy(onSuccess = { list ->
                run {
                    recipesList = if (offset > 0) {
                        recipesList.apply { addAll(list) }
                    } else {
                        list.toMutableList()
                    }
                    recipes.postValue(recipesList)
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