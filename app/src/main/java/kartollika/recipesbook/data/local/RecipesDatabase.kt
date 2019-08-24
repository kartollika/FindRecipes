package kartollika.recipesbook.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import io.reactivex.internal.schedulers.IoScheduler
import kartollika.recipesbook.common.utils.TimeConverters
import kartollika.recipesbook.data.local.dao.*
import kartollika.recipesbook.data.local.entities.*
import kartollika.recipesbook.data.models.IngredientChosenTypeConverters
import kartollika.recipesbook.data.models.IngredientSearch

@Database(
    entities = [IngredientEntity::class,
        RecipeEntity::class,
        RecipeIngredientEntity::class,
        RecipeIngredientRecipeJoinEntity::class,
        FavoriteRecipeEntity::class],
    version = 1
)
@TypeConverters(IngredientChosenTypeConverters::class, TimeConverters::class)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun getIngredientsDao(): IngredientsDao
    abstract fun getRecipesDao(): RecipesDao
    abstract fun getRecipeIngredientsRecipeDao(): RecipeIngredientRecipeDao
    abstract fun getRecipeIngredientDao(): RecipeIngredientDao
    abstract fun getFavoriteDao(): FavoriteDao

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
    for (intolerance in IngredientSearch.getIntolerances())
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