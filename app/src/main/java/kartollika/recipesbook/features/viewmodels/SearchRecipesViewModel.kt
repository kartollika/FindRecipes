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
import kartollika.recipesbook.features.search_recipes.LoadingState
import javax.inject.Inject

class SearchRecipesViewModel
@Inject constructor(
    private val repositorySearch: SearchRecipesRepository,
    private val filterRepository: RecipesFilterRepository
) : ViewModel() {

    private val recipes: MutableLiveData<List<RecipePreview>> = MutableLiveData()
    private val recipesRefreshingEvent = MutableLiveData<Event<LoadingState>>()
    private val ranking = MutableLiveData<Ranking>()
    private val errorLiveData = MutableLiveData<Event<String>>()

    private var compositeDisposable = CompositeDisposable()
    private var recipesList = mutableListOf<RecipePreview>()

    init {
        compositeDisposable.addAll(
            loadCurrentRanking()
        )
    }

    fun getRecipes(): LiveData<List<RecipePreview>> = recipes

    fun getRefreshingEvent(): LiveData<Event<LoadingState>> = recipesRefreshingEvent

    fun getRanking(): LiveData<Ranking> = ranking

    fun getErrorObservable(): LiveData<Event<String>> = errorLiveData

    private fun loadCurrentRanking(): Disposable =
        filterRepository.getRankingObservable()
            .subscribe { rank: Int ->
                ranking.postValue(
                    Ranking.fromRankingValue(rank)
                )
            }


    fun performComplexSearch(offset: Int = 0) {
        repositorySearch.searchRecipesComplex(offset)
            .subscribeOn(IoScheduler())
            .doOnSubscribe { switchLoadingState(LoadingState.Loading) }
            .doOnSuccess { t -> Log.d("Obs", t.size.toString()) }
            .subscribeBy(onSuccess = { list ->
                run {
                    recipesList = if (offset > 0) {
                        recipesList.apply { addAll(list) }
                    } else {
                        list.toMutableList()
                    }
                    recipes.postValue(recipesList)
                    switchLoadingState(LoadingState.Finished)
                }
            },
                onError = {
                    errorLiveData.postValue(Event("Failed to load recipes"))
                })
    }

    private fun switchLoadingState(state: LoadingState) {
        recipesRefreshingEvent.postValue(Event(state))
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