package kartollika.recipiesbook.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import kartollika.recipiesbook.features.search_recipes.RecipesFiltersDialogFragment
import kartollika.recipiesbook.features.search_recipes.SearchRecipesFragment
import kartollika.recipiesbook.features.viewmodels.FilterRecipesViewModel
import kartollika.recipiesbook.features.viewmodels.SearchRecipesViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, StorageModule::class])
interface ApplicationComponent {

    val searchRecipesViewModel: SearchRecipesViewModel
    val filterRecipesViewModel: FilterRecipesViewModel

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    fun inject(fragment: SearchRecipesFragment)
    fun inject(fragment: RecipesFiltersDialogFragment)
}