package kartollika.recipesbook.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import kartollika.recipesbook.data.local.entities.RecipeIngredientEntity
import kartollika.recipesbook.data.local.entities.RecipeIngredientRecipeJoinEntity

@Dao
interface RecipeIngredientRecipeDao {

    @Query(
        "SELECT * from recipe_ingredient_table INNER JOIN recipe_ingredient_recipe_join_table " +
            "ON recipe_ingredient_table.id=recipe_ingredient_recipe_join_table.recipeIngredientId " +
            "WHERE recipe_ingredient_recipe_join_table.recipeId=:recipeId"
    )
    fun getIngredientsOfRecipe(recipeId: Int): Single<List<RecipeIngredientEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(recipeIngredientRecipeJoin: RecipeIngredientRecipeJoinEntity): Completable
}