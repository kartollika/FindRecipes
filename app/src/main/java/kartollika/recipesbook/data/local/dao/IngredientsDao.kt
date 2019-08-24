package kartollika.recipesbook.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import kartollika.recipesbook.data.local.entities.IngredientEntity
import kartollika.recipesbook.data.models.IngredientChosenType

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

    @Query("DELETE from ingredients_table WHERE name = :name AND chosenType LIKE :type")
    fun deleteIngredient(name: String, type: IngredientChosenType): Completable

    @Query("SELECT * from ingredients_table WHERE name = :name AND chosenType LIKE :type")
    fun getIngredientByNameAndType(name: String, type: IngredientChosenType): Single<IngredientEntity>
}