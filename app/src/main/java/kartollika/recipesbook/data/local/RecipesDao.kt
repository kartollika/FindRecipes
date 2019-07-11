package kartollika.recipesbook.data.local

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import kartollika.recipesbook.data.local.entities.RecipeEntity

@Dao
interface RecipesDao {

    @Query("SELECT * from recipes_table WHERE id = :id")
    fun getRecipeById(id: Int): Single<RecipeEntity?>

    @Query("SELECT COUNT(*) from recipes_table WHERE id=:id")
    fun isRecipeInTable(id: Int): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipeEntity: RecipeEntity): Completable

    @Delete
    fun deleteRecipe(recipeEntity: RecipeEntity): Completable

    @Query("DELETE from recipes_table WHERE id = :id")
    fun deleteRecipeById(id: Int): Completable
}