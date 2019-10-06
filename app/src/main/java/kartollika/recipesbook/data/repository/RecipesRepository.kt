package kartollika.recipesbook.data.repository

import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.rxkotlin.subscribeBy
import kartollika.recipesbook.data.local.dao.RecipeIngredientRecipeDao
import kartollika.recipesbook.data.local.dao.RecipesDao
import kartollika.recipesbook.data.local.entities.RecipeEntity
import kartollika.recipesbook.data.local.entities.mapToIngredientDetail
import kartollika.recipesbook.data.local.entities.mapToRecipeEntity
import kartollika.recipesbook.data.models.IngredientDetail
import kartollika.recipesbook.data.models.Recipe
import kartollika.recipesbook.data.models.mapToRecipeIngredientEntity
import kartollika.recipesbook.data.models.mapToRecipeModel
import kartollika.recipesbook.data.remote.api.data.DataApi
import kartollika.recipesbook.data.remote.api.data.response.mapToEquipment
import javax.inject.Inject

class RecipesRepository
@Inject constructor(
    private val recipesDao: RecipesDao,
    private val recipeIngredientRecipeDao: RecipeIngredientRecipeDao,
    private val dataApi: DataApi
) {

    fun getRecipeMainInformation(recipeId: Int) =
        Single.zip<Recipe, List<IngredientDetail>, Recipe>(
            getCachedRecipe(recipeId),
            getCachedIngredients(recipeId),
            BiFunction { t1, t2 -> t1.copy(requiredIngredients = t2) })
            .subscribeOn(IoScheduler())
            .onErrorResumeNext(
                dataApi.getRecipeInformation(recipeId)
                    .map { it.mapToRecipeModel() }
                    .doOnSuccess {
                        insertRecipeEntity(it.mapToRecipeEntity())
                    }
                    .doOnSuccess {
                        it.requiredIngredients.forEach { ingredient ->
                            insertIngredientForRecipeRelation(recipeId, ingredient)
                        }
                    }
            )

    fun getRecipeRequiredEquipment(recipeId: Int) =
        Single.defer { dataApi.getRecipeRequiredEquipment(recipeId) }
            .subscribeOn(IoScheduler())
            .map { it.equipment }
            .map { it.map { it.mapToEquipment() } }

    private fun getCachedRecipe(recipeId: Int) =
        Single.defer {
            recipesDao.getRecipeById(recipeId)
                .map { it.mapToRecipeModel() }
        }


    private fun getCachedIngredients(recipeId: Int) =
        Single.defer {
            recipeIngredientRecipeDao.getRecipeWithIngredients(recipeId)
                .map { it.flatMap { it.ingredients } }
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
        ingredient: IngredientDetail
    ) {
        recipeIngredientRecipeDao.insertIngredientForRecipe(
            ingredient.mapToRecipeIngredientEntity().copy(
                recipeId = recipeId
            )
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

