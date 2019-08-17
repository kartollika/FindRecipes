package kartollika.recipesbook.data.repository

import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipesbook.data.models.RecipePreview
import kartollika.recipesbook.data.remote.api.search.SearchApi
import kartollika.recipesbook.data.remote.api.search.request.SearchRecipesComplexRequest
import kartollika.recipesbook.data.remote.api.search.response.SearchRecipeComplexResponse
import kartollika.recipesbook.data.remote.api.search.response.mapToRecipeModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRecipesRepository
@Inject constructor(
    private val searchApi: SearchApi,
    private val filterRecipesRepository: FilterRecipesRepository,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {

    private var disposable: Disposable? = null
    private var usePredefinedIntolerance: Boolean = false

    init {
        disposable = RxSharedPreferences.create(sharedPreferencesRepository.getSharedPreferences())
            .getBoolean(FilterRecipesRepository.SERACH_RECIPES_COMPLEX_USE_PREDEFINED_INTOLERANCE_KEY)
            .asObservable()
            .subscribeOn(IoScheduler())
            .subscribeBy(onNext = { usePredefinedIntolerance = it }, onError = { it.printStackTrace() })
    }

    fun searchRecipesComplex(offset: Int = 0, number: Int = 10): Single<List<RecipePreview>> =
        Single.zip(
            listOf(
                getIncludedActiveIngredientsFlatFormat(),
                getExcludedActiveIngredientsFlatFormat(),
                filterRecipesRepository.getQueryRecipe(),
                filterRecipesRepository.getRankingSingle(),
                getIntoleranceActiveIngredientsFlatFormat()
            )
        ) { t -> createSearchRecipesComplexRequest(offset, number, t) }
            .subscribeOn(IoScheduler())
            .flatMap { t: SearchRecipesComplexRequest -> searchRecipesComplex(t) }
            .map { t -> t.results }
            .map { t -> t.map { it.mapToRecipeModel() } }


    private fun createSearchRecipesComplexRequest(
        offset: Int,
        number: Int,
        t: Array<out Any>
    ): SearchRecipesComplexRequest =
        t.let {
            return SearchRecipesComplexRequest(
                offset = offset,
                number = number,
                includedIngredients = t[0] as String,
                excludedIngredients = t[1] as String,
                query = t[2] as String,
                ranking = t[3] as Int,
                intoleranceIngredients = t[4] as String
            )
        }

    private fun getIncludedActiveIngredientsFlatFormat(): Single<String> =
        Single.create { emitter ->
            filterRecipesRepository.getIncludedIngredients()
                .map { t -> t.filter { it.isActive } }
                .map { t -> t.map { it.name } }
                .map { t -> t.joinToString(separator = ",") }
                .subscribeBy(onNext = { result -> emitter.onSuccess(result) })
        }

    private fun getExcludedActiveIngredientsFlatFormat(): Single<String> =
        Single.create { emitter ->
            filterRecipesRepository.getExcludedIngredients()
                .map { list -> list.filter { it.isActive } }
                .map { it -> it.map { it.name } }
                .map { t -> t.joinToString(separator = ",") }
                .subscribeBy(
                    onNext = { result -> emitter.onSuccess(result) },
                    onError = { throwable -> emitter.onError(throwable) })
        }

    private fun getIntoleranceActiveIngredientsFlatFormat(): Single<String> =
        Single.create { emitter ->
            filterRecipesRepository.getIntoleranceIngredients()
                .map { list ->
                    list.filter {
                        if (usePredefinedIntolerance) {
                            it.isPredefinedActive
                        } else {
                            it.isActive
                        }
                    }
                }
                .map { it -> it.map { it.name } }
                .map { t -> t.joinToString(separator = ",") }
                .subscribeBy(
                    onNext = { result -> emitter.onSuccess(result) },
                    onError = { throwable -> emitter.onError(throwable) })
        }


    private fun searchRecipesComplex(searchRecipesComplexRequest: SearchRecipesComplexRequest): Single<SearchRecipeComplexResponse> {
        searchRecipesComplexRequest.let {
            return searchApi.searchRecipesComplex(
                limitLicense = it.limitLicence,
                offset = it.offset,
                number = it.number,
                includedIngredients = it.includedIngredients,
                excludedIngredients = it.excludedIngredients,
                intolerances = it.intoleranceIngredients,
                ranking = it.ranking,
                query = it.query,
                addRecipeInformation = it.addRecipeInformation
            )
        }
    }
}