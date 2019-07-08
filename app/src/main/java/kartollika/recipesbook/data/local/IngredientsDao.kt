package kartollika.recipesbook.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import kartollika.recipesbook.data.local.entities.IngredientEntity

@Dao
interface IngredientsDao {

    @Query("SELECT * from ingredients_table")
    fun getAllIngredients(): Observable<List<IngredientEntity>>

    @Query("SELECT * from ingredients_table WHERE chosenType LIKE :type")
    fun getAllIngredientsOfType(type: String): Observable<List<IngredientEntity>>

    @Insert
    fun insertIngredient(ingredient: IngredientEntity): Completable

    @Update
    fun updateIngredient(ingredient: IngredientEntity): Completable

    @Query("DELETE from ingredients_table WHERE name = :name")
    fun deleteIngredientByName(name: String): Completable

    @Query("SELECT * from ingredients_table WHERE name = :name")
    fun getIngredientByName(name: String): Single<IngredientEntity>
}