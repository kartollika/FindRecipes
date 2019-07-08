package kartollika.recipiesbook.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import io.reactivex.internal.schedulers.IoScheduler
import kartollika.recipiesbook.data.local.entities.IngredientEntity
import kartollika.recipiesbook.data.models.Ingredient
import kartollika.recipiesbook.data.models.IngredientChosenType

@Database(entities = [IngredientEntity::class], version = 1)
@TypeConverters(IngredientChosenType.Converters::class)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun getIngredientsDao(): IngredientsDao

    companion object {

        private var instance: RecipesDatabase? = null

        fun getInstance(context: Context): RecipesDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        RecipesDatabase::class.java,
                        "RECIPES_DATABASE"
                    )
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                Log.d("DatabaseRecipes", "onCreate Room called")
                                Thread(Runnable { populateIntolerances(getInstance(context)) }).start()
                            }
                        })
                        .build()
            }
            return instance as RecipesDatabase
        }
    }
}

private fun populateIntolerances(db: RecipesDatabase) {
    for (intolerance in Ingredient.getIntolerances())
        db.getIngredientsDao().insertIngredient(
            IngredientEntity(
                intolerance.name,
                intolerance.chosenType,
                intolerance.isActive
            )
        )
            .subscribeOn(IoScheduler())
            .subscribe()
}