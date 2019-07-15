package kartollika.recipesbook.data.repository

import io.reactivex.Single
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipesbook.data.models.RecipePreview
import kartollika.recipesbook.data.remote.search.SearchApi
import kartollika.recipesbook.data.remote.search.request.SearchRecipesComplexRequest
import kartollika.recipesbook.data.remote.search.response.SearchRecipeComplexResponse
import kartollika.recipesbook.data.remote.search.response.mapToRecipeModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRecipesRepository
@Inject constructor(
    private val searchApi: SearchApi,
    private val filterRepository: RecipesFilterRepository
) {
    fun searchRecipesComplex(offset: Int = 0, number: Int = 10): Single<List<RecipePreview>> =
        Single.zip(
            listOf(
                getIncludedActiveIngredientsFlatFormat(),
                getExcludedActiveIngredientsFlatFormat(),
                filterRepository.getQueryRecipe(),
                filterRepository.getRankingSingle(),
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
            filterRepository.getIncludedIngredients()
                .map { t -> t.filter { it.isActive } }
                .map { t -> t.map { it.name } }
                .map { t -> t.joinToString(separator = ",") }
                .subscribeBy(onNext = { result -> emitter.onSuccess(result) })
        }

    private fun getExcludedActiveIngredientsFlatFormat(): Single<String> =
        Single.create { emitter ->
            filterRepository.getExcludedIngredients()
                .map { list -> list.filter { it.isActive } }
                .map { it -> it.map { it.name } }
                .map { t -> t.joinToString(separator = ",") }
                .subscribeBy(
                    onNext = { result -> emitter.onSuccess(result) },
                    onError = { throwable -> emitter.onError(throwable) })
        }

    private fun getIntoleranceActiveIngredientsFlatFormat(): Single<String> =
        Single.create { emitter ->
            filterRepository.getIntoleranceIngredients()
                .map { list -> list.filter { it.isActive } }
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
                query = it.query
            )
        }
    }
}