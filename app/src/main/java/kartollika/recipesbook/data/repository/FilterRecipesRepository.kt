package kartollika.recipesbook.data.repository

import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipesbook.data.local.entities.IngredientEntity
import kartollika.recipesbook.data.models.FoodItem
import kartollika.recipesbook.data.models.IngredientChosenType
import kartollika.recipesbook.data.models.IngredientSearch
import kartollika.recipesbook.data.models.Ranking
import kartollika.recipesbook.data.remote.extract.ExtractApi
import kartollika.recipesbook.data.remote.extract.response.mapToFoodItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterRecipesRepository
@Inject constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    private val filterIngredientsRepository: FilterIngredientsRepository,
    private val extractApi: ExtractApi

) {

    companion object {
        const val SEARCH_RECIPES_COMPLEX_QUERY = "SEARCH_RECIPES_COMPLEX_QUERY"
        const val SEARCH_RECIPES_COMPLEX_RANKING = "SEARCH_RECIPES_COMPLEX_RANKING"
        const val SERACH_RECIPES_COMPLEX_USE_PREDEFINED_INTOLERANCE_KEY =
            "SERACH_RECIPES_COMPLEX_USE_PREDEFINED_INTOLERANCE_KEY"
    }

    fun getIncludedIngredients() = filterIngredientsRepository.getIncludedIngredients()

    fun getExcludedIngredients() = filterIngredientsRepository.getExcludedIngredients()

    fun getIntoleranceIngredients() = filterIngredientsRepository.getIntoleranceIngredients()

    fun addNewIngredients(ingredientRawText: String, type: IngredientChosenType) {
        parseFoodInText(ingredientRawText)
            .subscribeBy(onNext = { foodItem ->
                filterIngredientsRepository.addIngredient(
                    IngredientEntity(
                        foodItem.name,
                        type,
                        isActive = true
                    )
                )
            })
    }

    fun switchNotPredefinedIngredientsState(ingredient: IngredientSearch, state: Boolean) {
        filterIngredientsRepository.switchNotPredefinedIngredientState(ingredient, state)
    }

    fun switchPredefinedIngredientState(ingredient: IngredientSearch, state: Boolean) {
        filterIngredientsRepository.switchPredefinedIngredientState(ingredient, state)
    }

    fun deleteIngredient(ingredient: IngredientSearch) {
        filterIngredientsRepository.deleteIngredient(ingredient)
    }

    fun getQueryRecipe() =
        sharedPreferencesRepository.getString(SEARCH_RECIPES_COMPLEX_QUERY)

    fun saveQueryRecipe(query: String) {
        sharedPreferencesRepository.putString(SEARCH_RECIPES_COMPLEX_QUERY, query)
    }

    fun getRankingObservable(): Observable<Int> =
        RxSharedPreferences.create(sharedPreferencesRepository.getSharedPreferences()).getInteger(
            SEARCH_RECIPES_COMPLEX_RANKING,
            2
        ).asObservable()
            .subscribeOn(IoScheduler())

    fun getRankingSingle(): Single<Int> =
        sharedPreferencesRepository.getInt(SEARCH_RECIPES_COMPLEX_RANKING, 2)


    fun saveRankingFilter(ranking: Ranking) {
        sharedPreferencesRepository.putInt(SEARCH_RECIPES_COMPLEX_RANKING, ranking.ranking)
    }

    fun saveUsePredefinedIntoleranceSetting(state: Boolean) {
        sharedPreferencesRepository.easyPut<Boolean> {
            putBoolean(
                SERACH_RECIPES_COMPLEX_USE_PREDEFINED_INTOLERANCE_KEY,
                state
            )
        }
    }

    fun getUsePredefinedIntoleranceObservable() =
        RxSharedPreferences.create(sharedPreferencesRepository.getSharedPreferences())
            .getBoolean(SERACH_RECIPES_COMPLEX_USE_PREDEFINED_INTOLERANCE_KEY)
            .asObservable()

    private fun parseFoodInText(text: String): Observable<FoodItem> =
        extractApi.detectFoodInText(text)
            .subscribeOn(IoScheduler())
            .map { it.annotations }
            .flatMapIterable { it }
            .map { it.mapToFoodItem() }
}
