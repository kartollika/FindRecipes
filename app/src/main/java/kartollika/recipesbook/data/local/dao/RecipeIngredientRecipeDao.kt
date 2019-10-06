package kartollika.recipesbook.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Completable
import io.reactivex.Single
import kartollika.recipesbook.data.local.entities.RecipeAndIngredients
import kartollika.recipesbook.data.local.entities.RecipeIngredientEntity

@Dao
interface RecipeIngredientRecipeDao {

    @Transaction
    @Query("SELECT id from recipes_table WHERE id = :recipeId")
    fun getRecipeWithIngredients(recipeId: Int): Single<List<RecipeAndIngredients>>

    @Insert
    fun insertIngredientForRecipe(ingredientEntity: RecipeIngredientEntity): Completable
}