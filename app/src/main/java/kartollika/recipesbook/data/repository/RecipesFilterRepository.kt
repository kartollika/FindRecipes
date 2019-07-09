package kartollika.recipesbook.data.repository

import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipesbook.data.local.entities.IngredientEntity
import kartollika.recipesbook.data.models.FoodItem
import kartollika.recipesbook.data.models.Ingredient
import kartollika.recipesbook.data.models.IngredientChosenType
import kartollika.recipesbook.data.models.Ranking
import kartollika.recipesbook.data.remote.extract.ExtractApi
import kartollika.recipesbook.data.remote.extract.response.mapToFoodItem
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

    fun switchActivateIngredient(ingredient: Ingredient, state: Boolean) {
        ingredientsRepository.switchActivateIngredient(ingredient, state)
    }

    fun deleteIngredient(ingredient: Ingredient) {
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

    fun getRankingObservable(): Observable<Int> =
        RxSharedPreferences.create(sharedPreferences).getInteger(
            SEARCH_RECIPES_COMPLEX_RANKING,
            2
        ).asObservable()
            .subscribeOn(IoScheduler())

    fun getRankingSingle(): Single<Int> =
        Single.fromCallable { sharedPreferences.getInt(SEARCH_RECIPES_COMPLEX_RANKING, 2) }
            .subscribeOn(IoScheduler())


    fun saveRankingFilter(ranking: Ranking) {
        sharedPreferences.edit()
            .putInt(SEARCH_RECIPES_COMPLEX_RANKING, ranking.ranking)
            .apply()
    }

    private fun parseFoodInText(text: String): Observable<FoodItem> =
        extractApi.detectFoodInText(text)
            .subscribeOn(IoScheduler())
            .map { it.annotations }
            .flatMapIterable { it }
            .map { it.mapToFoodItem() }
}
