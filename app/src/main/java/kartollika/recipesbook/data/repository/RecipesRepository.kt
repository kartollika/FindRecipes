package kartollika.recipesbook.data.repository

import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipesbook.data.local.RecipeIngredientDao
import kartollika.recipesbook.data.local.RecipeIngredientRecipeDao
import kartollika.recipesbook.data.local.RecipesDao
import kartollika.recipesbook.data.local.entities.RecipeEntity
import kartollika.recipesbook.data.local.entities.RecipeIngredientRecipeJoinEntity
import kartollika.recipesbook.data.local.entities.mapToIngredientDetail
import kartollika.recipesbook.data.local.entities.mapToRecipeEntity
import kartollika.recipesbook.data.models.IngredientDetail
import kartollika.recipesbook.data.models.Recipe
import kartollika.recipesbook.data.models.mapToRecipeModel
import kartollika.recipesbook.data.remote.data.DataApi
import javax.inject.Inject

class RecipesRepository
@Inject constructor(
    private val recipesDao: RecipesDao,
    private val recipeIngredientRecipeDao: RecipeIngredientRecipeDao,
    private val recipeIngredientDao: RecipeIngredientDao,
    private val dataApi: DataApi
) {

    fun getRecipeMainInformation(recipeId: Int) =
        Single.zip<Recipe, List<IngredientDetail>, Recipe>(
            getCachedRecipe(recipeId),
            getCachedIngredients(recipeId),
            BiFunction { t1, t2 ->
                t1.apply {
                    requiredIngredients = t2
                }
            }
        )
            .subscribeOn(IoScheduler())
            .onErrorResumeNext(
                dataApi.getRecipeInformation(recipeId)
                    .map { it.mapToRecipeModel() }
                    .doOnSuccess {
                        insertRecipeEntity(it.mapToRecipeEntity())
                    }
                    .doOnSuccess {
                        it.requiredIngredients.forEach { ingredient ->
                            insertIngredientForRecipeRelation(recipeId, ingredient.id)
                        }
                    }
            )

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

    private fun insertIngredientForRecipeRelation(
        recipeId: Int,
        ingredientId: Int
    ) {
        recipeIngredientRecipeDao.insert(
            RecipeIngredientRecipeJoinEntity(recipeId, ingredientId)
        )
            .subscribeOn(IoScheduler())
            .subscribeBy(
                onError = { it.printStackTrace() }
            )
    }

    private fun insertRecipeEntity(recipe: RecipeEntity) {
        recipesDao.insertRecipe(recipe)
            .subscribeOn(IoScheduler())
            .subscribe()
    }
}

