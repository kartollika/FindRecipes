package kartollika.recipiesbook.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import kartollika.recipiesbook.data.models.Ingredient

@Dao
interface IngredientsDao {

    @Query("SELECT * from ingredients_table")
    fun getAllIngredients(): Observable<List<Ingredient>>

    @Insert
    fun insertIngredient(ingredient: Ingredient): Completable

    @Delete
    fun deleteIngredient(ingredient: Ingredient): Completable
}