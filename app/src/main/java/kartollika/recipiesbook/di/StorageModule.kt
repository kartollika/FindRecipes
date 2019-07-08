package kartollika.recipiesbook.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
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
        RecipesDatabase.getInstance(context)

    @Provides
    @JvmStatic
    fun provideIngredientsDao(recipesDatabase: RecipesDatabase) =
        recipesDatabase.getIngredientsDao()
}