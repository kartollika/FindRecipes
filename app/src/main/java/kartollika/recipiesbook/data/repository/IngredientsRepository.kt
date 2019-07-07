package kartollika.recipiesbook.data.repository

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import kartollika.recipiesbook.data.entities.Ingredient
import kartollika.recipiesbook.data.local.IngredientsDao
import kartollika.recipiesbook.data.models.IngredientChosenType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IngredientsRepository
@Inject constructor(
    private val ingredientsDao: IngredientsDao
) {

    fun getAllIngredients(): Observable<List<Ingredient>> =
        ingredientsDao
            .getAllIngredients()

    fun getIncludedIngredients(): Observable<List<Ingredient>> =
        getAllIngredients()
            .map {
                it.filter { ingredient -> ingredient.chosenType == IngredientChosenType.Included }
            }
            .subscribeOn(IoScheduler())

    fun getExcludedIngredients(): Observable<List<Ingredient>> =
        getAllIngredients()
            .map {
                it.filter { ingredient -> ingredient.chosenType == IngredientChosenType.Excluded }
            }
            .subscribeOn(IoScheduler())

    fun getIntoleranceIngredients(): Observable<List<Ingredient>> =
        getAllIngredients()
            .map {
                it.filter { ingredient -> ingredient.chosenType == IngredientChosenType.Intolerance }
            }
            .subscribeOn(IoScheduler())


    fun addIngredient(ingredient: Ingredient): Disposable =
        ingredientsDao
            .insertIngredient(ingredient)
            .subscribeOn(IoScheduler())
            .subscribe()

    fun deleteIngredient(ingredient: String) {
        ingredientsDao.deleteIngredientByName(ingredient)
            .subscribeOn(IoScheduler())
            .subscribe()
    }

    fun switchActivateIngredient(ingredient: String, state: Boolean): Disposable =
        ingredientsDao.getIngredientByName(ingredient)
            .map { t ->
                t.apply {
                    isActive = state
                }
            }
            .subscribeOn(IoScheduler())
            .subscribe { t -> updateIngredient(t) }


    private fun updateIngredient(ingredient: Ingredient) {
        ingredientsDao.updateIngredient(ingredient)
            .subscribeOn(IoScheduler())
            .subscribe()
    }
}