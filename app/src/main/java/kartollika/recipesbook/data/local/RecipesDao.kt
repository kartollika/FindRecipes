package kartollika.recipesbook.data.local

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import kartollika.recipesbook.data.local.entities.HOUR
import kartollika.recipesbook.data.local.entities.RecipeEntity
import java.util.*

@Dao
interface RecipesDao {

    @Query("SELECT * from recipes_table WHERE id = :id")
    fun getRecipeById(id: Int): Single<RecipeEntity?>

    @Query("SELECT COUNT(*) from recipes_table WHERE id=:id AND cachedUntil BETWEEN :now AND :nextHour")
    fun isRecipeInTable(
        id: Int,
        now: Date = Date(System.currentTimeMillis()),
        nextHour: Date = Date(System.currentTimeMillis() + HOUR)
    ): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipeEntity: RecipeEntity): Completable

    @Delete
    fun deleteRecipe(recipeEntity: RecipeEntity): Completable

    @Query("DELETE from recipes_table WHERE id = :id")
    fun deleteRecipeById(id: Int): Completable
}