package kartollika.recipesbook.data.repository

import io.reactivex.Single
import io.reactivex.internal.schedulers.IoScheduler
import kartollika.recipesbook.data.local.RecipesDao
import kartollika.recipesbook.data.local.entities.RecipeEntity
import kartollika.recipesbook.data.models.mapToRecipeModel
import kartollika.recipesbook.data.remote.data.DataApi
import kartollika.recipesbook.data.remote.data.response.RecipeResponse
import java.util.*
import javax.inject.Inject

class RecipesRepository
@Inject constructor(
    private val recipesDao: RecipesDao,
    private val dataApi: DataApi
) {

    @Suppress("SENSELESS_COMPARISON")
    fun isRecipeInDb(id: Int): Single<Boolean> =
        recipesDao.isRecipeInTable(id)
            .subscribeOn(IoScheduler())
            .map { it > 0 }

    fun getRecipeById(id: Int) =
        isRecipeInDb(id)
            .flatMap { isInDb ->
                if (isInDb) {
                    recipesDao.getRecipeById(id)
                        .flatMap {
                            if (it.cachedUntil.before(Date(System.currentTimeMillis()))) {
                                dataApi.getRecipeInformation(id)
                            } else {
                                Single.just(it)
                            }
                        }
                } else {
                    dataApi.getRecipeInformation(id)
                }
            }
            .map {
                when (it) {
                    is RecipeEntity -> it.mapToRecipeModel()
                    is RecipeResponse -> {
                        recipesDao.insertRecipe(RecipeEntity(it.id, it.title, it.image))
                            .subscribeOn(IoScheduler())
                            .subscribe()
                        it.mapToRecipeModel()
                    }
                    else -> throw IllegalArgumentException()
                }
            }
}

