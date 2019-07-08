package kartollika.recipesbook.features.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.rxkotlin.subscribeBy
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
    fun getRecipes(): LiveData<List<Recipe>> = recipes

    private var compositeDisposable = CompositeDisposable()

    fun performComplexSearch() {
        repository.searchRecipesComplex()
            .subscribeOn(IoScheduler())
            .doOnSuccess { t -> Log.d("Obs", t.size.toString()) }
            .subscribeBy(onSuccess = { list -> recipes.postValue(list) })
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