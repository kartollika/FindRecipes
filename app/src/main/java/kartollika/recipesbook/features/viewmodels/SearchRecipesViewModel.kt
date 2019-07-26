package kartollika.recipesbook.features.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipesbook.common.reactive.Event
import kartollika.recipesbook.common.ui.LoadingState
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

    private val recipes: MutableLiveData<List<RecipePreview>> = MutableLiveData()
    private val recipesRefreshingEvent = MutableLiveData<Event<LoadingState>>()
    private val ranking = MutableLiveData<Ranking>()
    private val errorLiveData = MutableLiveData<Event<String>>()

    private var recipesList = mutableListOf<RecipePreview>()
    private var compositeDisposable = CompositeDisposable()
    private var currentListOffset = 0


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


    fun performComplexSearch() {
        switchLoadingState(LoadingState.Loading)
        repositorySearch.searchRecipesComplex(currentListOffset)
            .subscribeOn(IoScheduler())
            .doOnEvent { _, _ -> switchLoadingState(LoadingState.Finished) }
            .subscribeBy(onSuccess = { list ->
                run {
                    recipesList = if (currentListOffset > 0) {
                        recipesList.apply { addAll(list) }
                    } else {
                        list.toMutableList()
                    }
                    currentListOffset = recipesList.size
                    recipes.postValue(recipesList)
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

    fun resetList() {
        currentListOffset = 0
    }
}