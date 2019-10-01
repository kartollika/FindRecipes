package kartollika.recipesbook.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import kartollika.recipesbook.data.local.entities.FavoriteRecipeEntity
import kartollika.recipesbook.data.local.entities.RecipeEntity

@Dao
interface FavoriteDao {

    @Query("SELECT * from favorite_recipes WHERE recipeId = :id ")
    fun isRecipeFavorite(id: Int): Observable<List<FavoriteRecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(favoriteRecipeEntity: FavoriteRecipeEntity): Completable

    @Query("DELETE from favorite_recipes WHERE recipeId = :id")
    fun removeFavorite(id: Int): Completable

    @Query("SELECT id, image, title, cachedUntil from recipes_table, favorite_recipes WHERE id = recipeId LIMIT :number OFFSET :offset")
    fun getAllFavoriteRecipes(number: Int, offset: Int): Single<List<RecipeEntity>>
}