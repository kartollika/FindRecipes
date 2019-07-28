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
class RecipesFilterRepository
@Inject constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    private val ingredientsRepository: IngredientsRepository,
    private val extractApi: ExtractApi

) {

    private val SEARCH_RECIPES_COMPLEX_QUERY = "SEARCH_RECIPES_COMPLEX_QUERY"
    private val SEARCH_RECIPES_COMPLEX_RANKING = "SEARCH_RECIPES_COMPLEX_RANKING"

    fun getIncludedIngredients() = ingredientsRepository.getIncludedIngredients()

    fun getExcludedIngredients() = ingredientsRepository.getExcludedIngredients()

    fun getIntoleranceIngredients() = ingredientsRepository.getIntoleranceIngredients()

    fun addNewIngredients(ingredientRawText: String, type: IngredientChosenType) {
        parseFoodInText(ingredientRawText)
            .subscribeBy(onNext = { foodItem ->
                ingredientsRepository.addIngredient(
                    IngredientEntity(
                        foodItem.name,
                        type,
                        isActive = true
                    )
                )
            })
    }

    fun switchNotPredefinedIngredientsState(ingredient: IngredientSearch, state: Boolean) {
        ingredientsRepository.switchNotPredefinedIngredientState(ingredient, state)
    }

    fun switchPredefinedIngredientState(ingredient: IngredientSearch, state: Boolean) {
        ingredientsRepository.switchPredefinedIngredientState(ingredient, state)
    }

    fun deleteIngredient(ingredient: IngredientSearch) {
        ingredientsRepository.deleteIngredient(ingredient)
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

    private fun parseFoodInText(text: String): Observable<FoodItem> =
        extractApi.detectFoodInText(text)
            .subscribeOn(IoScheduler())
            .map { it.annotations }
            .flatMapIterable { it }
            .map { it.mapToFoodItem() }
}
