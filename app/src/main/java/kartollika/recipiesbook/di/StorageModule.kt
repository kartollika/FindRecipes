package kartollika.recipiesbook.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import dagger.Module
import dagger.Provides
import kartollika.recipiesbook.data.local.RecipesDatabase

@Module
object StorageModule {

    @Provides
    @JvmStatic
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @JvmStatic
    fun provideRecipesDatabase(context: Context) =
        Room.databaseBuilder(
            context,
            RecipesDatabase::class.java,
            "DATABASE"
        ).build()

    @Provides
    @JvmStatic
    fun provideIngredientsDao(recipesDatabase: RecipesDatabase) =
        recipesDatabase.getIngredientsDao()
}