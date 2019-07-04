package kartollika.recipiesbook.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import kartollika.recipiesbook.data.models.Ingredient

@Database(entities = [Ingredient::class], version = 1)
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