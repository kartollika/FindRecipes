package kartollika.recipesbook.data.repository

import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.internal.schedulers.IoScheduler
import kartollika.recipesbook.data.local.RecipeIngredientDao
import kartollika.recipesbook.data.local.RecipeIngredientRecipeDao
import kartollika.recipesbook.data.local.RecipesDao
import kartollika.recipesbook.data.local.entities.HOUR
import kartollika.recipesbook.data.local.entities.RecipeEntity
import kartollika.recipesbook.data.local.entities.RecipeIngredientRecipeJoinEntity
import kartollika.recipesbook.data.local.entities.mapToIngredientDetail
import kartollika.recipesbook.data.models.Recipe
import kartollika.recipesbook.data.models.mapToRecipeIngredientEntity
import kartollika.recipesbook.data.models.mapToRecipeModel
import kartollika.recipesbook.data.remote.data.DataApi
import java.util.*
import javax.inject.Inject

class RecipesRepository
@Inject constructor(
    private val recipesDao: RecipesDao,
    private val recipeIngredientRecipeDao: RecipeIngredientRecipeDao,
    private val recipeIngredientDao: RecipeIngredientDao,
    private val dataApi: DataApi
) {

    private fun isRecipeInDb(id: Int): Single<Boolean> =
        recipesDao.isRecipeInTable(id)
            .subscribeOn(IoScheduler())
            .map { it > 0 }

    fun getRecipe(recipeId: Int): Single<Recipe> {
        return if (isRecipeInDb(recipeId).blockingGet()) {
            // Recipe and its ingredients are in database
            Single.zip(
                recipesDao.getRecipeById(recipeId),
                recipeIngredientRecipeDao.getIngredientsOfRecipe(recipeId),
                BiFunction { t1, t2 -> t1.mapToRecipeModel().apply { requiredIngredients = t2.map { it.mapToIngredientDetail() } } }
            )
        } else {
            // Invalidate recipe due to non existing in db or expired cache date
            dataApi.getRecipeInformation(recipeId)
                .subscribeOn(IoScheduler())
                .map { it.mapToRecipeModel() }
                .doOnSuccess { t ->
                    recipesDao.insertRecipe(
                        RecipeEntity(
                            t.id,
                            t.title,
                            t.image,
                            Date(System.currentTimeMillis() + HOUR)
                        )
                    )
                        .subscribeOn(IoScheduler()).subscribe()

                    for (ingredient in t.requiredIngredients) {
                        t.requiredIngredients.forEach {
                            recipeIngredientDao.insertIngredient(it.mapToRecipeIngredientEntity())
                                .subscribeOn(
                                    IoScheduler()
                                ).subscribe()
                        }

                        recipeIngredientRecipeDao.insert(
                            RecipeIngredientRecipeJoinEntity(
                                recipeId,
                                ingredient.id
                            )
                        ).subscribeOn(IoScheduler()).subscribe()
                    }
                }
        }
    }
}

