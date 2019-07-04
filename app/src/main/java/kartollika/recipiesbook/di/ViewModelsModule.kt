package kartollika.recipiesbook.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kartollika.recipiesbook.common.viewmodel.ViewModelFactory
import kartollika.recipiesbook.viewmodels.SearchRecipesViewModel

@Module
interface ViewModelsModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SearchRecipesViewModel::class)
    fun bindSearchRecipesViewModel(viewModel: SearchRecipesViewModel): ViewModel
}