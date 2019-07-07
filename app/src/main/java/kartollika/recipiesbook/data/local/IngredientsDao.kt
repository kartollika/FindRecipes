package kartollika.recipiesbook.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import kartollika.recipiesbook.data.entities.Ingredient

@Dao
interface IngredientsDao {

    @Query("SELECT * from ingredients_table")
    fun getAllIngredients(): Observable<List<Ingredient>>

    @Insert
    fun insertIngredient(ingredient: Ingredient): Completable

    @Update
    fun updateIngredient(ingredient: Ingredient): Completable

    @Query("DELETE from ingredients_table WHERE name = :name")
    fun deleteIngredientByName(name: String): Completable

    @Query("SELECT * from ingredients_table WHERE name = :name")
    fun getIngredientByName(name: String): Single<Ingredient>
}