package kartollika.recipesbook.data.repository

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import kartollika.recipesbook.data.local.IngredientsDao
import kartollika.recipesbook.data.local.entities.IngredientEntity
import kartollika.recipesbook.data.models.IngredientChosenType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IngredientsRepository
@Inject constructor(
    private val ingredientsDao: IngredientsDao
) {

    fun getIncludedIngredients(): Observable<List<IngredientEntity>> =
        ingredientsDao.getAllIngredientsOfType(IngredientChosenType.Included.name)
            .subscribeOn(IoScheduler())

    fun getExcludedIngredients(): Observable<List<IngredientEntity>> =
        ingredientsDao.getAllIngredientsOfType(IngredientChosenType.Excluded.name)
            .subscribeOn(IoScheduler())

    fun getIntoleranceIngredients(): Observable<List<IngredientEntity>> =
        ingredientsDao.getAllIngredientsOfType(IngredientChosenType.Intolerance.name)
            .subscribeOn(IoScheduler())


    fun addIngredient(ingredient: IngredientEntity): Disposable =
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


    private fun updateIngredient(ingredient: IngredientEntity) {
        ingredientsDao.updateIngredient(ingredient)
            .subscribeOn(IoScheduler())
            .subscribe()
    }
}