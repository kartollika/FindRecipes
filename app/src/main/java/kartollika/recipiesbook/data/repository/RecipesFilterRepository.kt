package kartollika.recipiesbook.data.repository

import android.content.SharedPreferences
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipiesbook.data.local.entities.IngredientEntity
import kartollika.recipiesbook.data.models.FoodItem
import kartollika.recipiesbook.data.models.IngredientChosenType
import kartollika.recipiesbook.data.models.Ranking
import kartollika.recipiesbook.data.remote.extract.ExtractApi
import kartollika.recipiesbook.data.remote.extract.response.mapToFoodItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesFilterRepository
@Inject constructor(
    private val sharedPreferences: SharedPreferences,
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
//            .filter { it.tag == "ingredient" }
            .subscribeBy(onNext = { foodItem ->
                ingredientsRepository.addIngredient(
                    IngredientEntity(
                        foodItem.name,
                        type,
                        true
                    )
                )
            })
    }

    fun switchActivateIngredient(ingredient: String, state: Boolean) {
        ingredientsRepository.switchActivateIngredient(ingredient, state)
    }

    fun deleteIngredient(ingredient: String) {
        ingredientsRepository.deleteIngredient(ingredient)
    }

    fun getQueryRecipe() =
        Single.fromCallable { sharedPreferences.getString(SEARCH_RECIPES_COMPLEX_QUERY, "") }
            .subscribeOn(IoScheduler())

    fun saveQueryRecipe(query: String) {
        sharedPreferences.edit()
            .putString(SEARCH_RECIPES_COMPLEX_QUERY, query)
            .apply()
    }

    fun getRanking(): Single<Int> =
        Single.fromCallable { sharedPreferences.getInt(SEARCH_RECIPES_COMPLEX_RANKING, 2) }
            .subscribeOn(IoScheduler())


    fun saveRankingFilter(ranking: Ranking) {
        sharedPreferences.edit()
            .putInt(SEARCH_RECIPES_COMPLEX_RANKING, ranking.ordinal)
            .apply()
    }

    private fun parseFoodInText(text: String): Observable<FoodItem> =
        extractApi.detectFoodInText(text)
            .subscribeOn(IoScheduler())
            .map { it.annotations }
            .flatMapIterable { it }
            .map { it.mapToFoodItem() }
}
