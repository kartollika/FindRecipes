package kartollika.recipesbook.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import kartollika.recipesbook.features.search_recipes.RecipesFiltersDialogFragment
import kartollika.recipesbook.features.search_recipes.SearchRecipesFragment
import kartollika.recipesbook.features.viewmodels.FilterRecipesViewModel
import kartollika.recipesbook.features.viewmodels.RecipeDetailViewModel
import kartollika.recipesbook.features.viewmodels.SearchRecipesViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, StorageModule::class])
interface ApplicationComponent {

    val searchRecipesViewModel: SearchRecipesViewModel
    val filterRecipesViewModel: FilterRecipesViewModel
    val recipeDetailViewModel: RecipeDetailViewModel

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    fun inject(fragment: SearchRecipesFragment)
    fun inject(fragment: RecipesFiltersDialogFragment)
}