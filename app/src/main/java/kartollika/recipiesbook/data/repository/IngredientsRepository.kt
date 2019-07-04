package kartollika.recipiesbook.data.repository

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import kartollika.recipiesbook.data.local.IngredientsDao
import kartollika.recipiesbook.data.models.Ingredient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IngredientsRepository
@Inject constructor(
    private val ingredientsDao: IngredientsDao
) {

    fun getIncludedIngredients(): Observable<List<Ingredient>> =
        ingredientsDao
            .getAllIngredients()
            .subscribeOn(IoScheduler())

    fun addIncludedIngredient(name: String): Disposable =
        ingredientsDao
            .insertIngredient(Ingredient(name))
            .subscribeOn(IoScheduler())
            .subscribe()

}