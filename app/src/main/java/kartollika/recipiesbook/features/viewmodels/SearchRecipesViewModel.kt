package kartollika.recipiesbook.features.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipiesbook.data.models.Recipe
import kartollika.recipiesbook.data.repository.RecipesRepository
import javax.inject.Inject

class SearchRecipesViewModel
@Inject constructor(
    private val repository: RecipesRepository
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


}