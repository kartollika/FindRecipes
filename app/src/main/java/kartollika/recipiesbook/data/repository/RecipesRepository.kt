package kartollika.recipiesbook.data.repository

import io.reactivex.Single
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipiesbook.data.models.Recipe
import kartollika.recipiesbook.data.remote.search.SearchApi
import kartollika.recipiesbook.data.remote.search.request.SearchRecipesComplexRequest
import kartollika.recipiesbook.data.remote.search.response.SearchRecipeComplexResponse
import kartollika.recipiesbook.data.remote.search.response.mapToRecipeModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesRepository
@Inject constructor(
    private val searchApi: SearchApi,
    private val filterRepository: RecipesFilterRepository
) {
    fun searchRecipesComplex(offset: Int = 0, number: Int = 10): Single<List<Recipe>> =
        Single.zip(
            listOf(
                getIncludedIngredientsRequestFormat(),
                getExcludedIngredientsRequestFormat()
            )
        ) { t -> createSearchRecipesComplexRequest(offset, number, t) }
            .subscribeOn(IoScheduler())
            .flatMap { t: SearchRecipesComplexRequest -> searchRecipesComplex(t) }
            .map { t -> t.results }
//            .map { t -> t.flatMap { searchRecipeComplexResponse -> searchRecipeComplexResponse.results } }
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
                excludedIngredients = t[1] as String
            )
        }

    private fun getIncludedIngredientsRequestFormat(): Single<String> =
        Single.create { emitter ->
            filterRepository.getIncludedIngredients()
                .map { it -> it.map { it.name } }
                .map { t -> t.joinToString(separator = ",") }
                .subscribeBy(onNext = { result -> emitter.onSuccess(result) })
        }

    private fun getExcludedIngredientsRequestFormat(): Single<String> =
        Single.create { emitter ->
            filterRepository.getIncludedIngredients()
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
                ranking = it.ranking.ordinal
            )
        }
    }
}