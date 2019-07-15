package kartollika.recipesbook.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import io.reactivex.Completable
import kartollika.recipesbook.data.local.entities.RecipeIngredientEntity

@Dao
interface RecipeIngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredient(ingredient: RecipeIngredientEntity): Completable
}