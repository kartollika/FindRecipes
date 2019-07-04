package kartollika.recipiesbook.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import kartollika.recipiesbook.features.search_by_ingredients.SearchRecipesActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, StorageModule::class, ViewModelsModule::class])
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    fun inject(activity: SearchRecipesActivity)
}