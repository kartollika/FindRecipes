package kartollika.recipiesbook.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kartollika.recipiesbook.data.entities.Ingredient
import kartollika.recipiesbook.data.models.IngredientChosenType

@Database(entities = [Ingredient::class], version = 1)
@TypeConverters(IngredientChosenType.Converters::class)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun getIngredientsDao(): IngredientsDao

    /* companion object {
         *//* fun getInstance(context: Context): RecipesDatabase {
             if (instance == null) {
                 instance =
                     Room.databaseBuilder(context, RecipesDatabase::class.java, "RECIPES_DATABASE")
                         .build()
             }
             return instance as RecipesDatabase
         }*//*
    }*/
}