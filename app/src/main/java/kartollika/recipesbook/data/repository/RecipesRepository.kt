package kartollika.recipesbook.data.repository

import io.reactivex.Single
import io.reactivex.internal.schedulers.IoScheduler
import kartollika.recipesbook.data.local.RecipeIngredientRecipeDao
import kartollika.recipesbook.data.local.RecipesDao
import kartollika.recipesbook.data.local.entities.RecipeIngredientRecipeJoinEntity
import kartollika.recipesbook.data.local.entities.mapToIngredientDetail
import kartollika.recipesbook.data.local.entities.mapToRecipeEntity
import kartollika.recipesbook.data.models.mapToRecipeModel
import kartollika.recipesbook.data.remote.data.DataApi
import kartollika.recipesbook.data.remote.data.response.mapToIngredientDetail
import javax.inject.Inject

class RecipesRepository
@Inject constructor(
    private val recipesDao: RecipesDao,
    private val recipeIngredientRecipeDao: RecipeIngredientRecipeDao,
    private val dataApi: DataApi
) {

    private fun getCachedRecipe(recipeId: Int) =
        Single.defer {
            recipesDao.getRecipeById(recipeId)
                .map { it.mapToRecipeModel() }
        }


    private fun getCachedIngredients(recipeId: Int) =
        Single.defer {
            recipeIngredientRecipeDao.getIngredientsOfRecipe(recipeId)
                .map {
                    if (it.isEmpty()) {
                        throw IllegalArgumentException()
                    } else {
                        it.map { it.mapToIngredientDetail() }
                    }
                }
        }

    fun getRecipeMainInformation(recipeId: Int) =
        getCachedRecipe(recipeId)
            .subscribeOn(IoScheduler())
            .onErrorResumeNext(
                dataApi.getRecipeInformation(recipeId)
                    .map { it.mapToRecipeModel() }
                    .doOnSuccess { recipesDao.insertRecipe(it.mapToRecipeEntity()) }
            )

    fun getRecipeIngredientsList(recipeId: Int) =
        getCachedIngredients(recipeId)
            .subscribeOn(IoScheduler())
            .onErrorResumeNext(
                dataApi.getRecipeInformation(recipeId)
                    .map { it.extendedIngredients.map { it.mapToIngredientDetail() } }
                    .doOnSuccess {
                        it.forEach {
                            recipeIngredientRecipeDao.insert(
                                RecipeIngredientRecipeJoinEntity(recipeId, it.id)
                            )
                        }
                    }
            )
}

