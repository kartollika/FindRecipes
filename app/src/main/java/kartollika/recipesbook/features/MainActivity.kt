package kartollika.recipesbook.features

import android.os.Bundle
import android.os.PersistableBundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kartollika.recipesbook.R
import kartollika.recipesbook.common.base.BaseActivity

class MainActivity : BaseActivity() {

    private lateinit var navController: NavController

    override fun getLayoutRes(): Int = R.layout.main_activity_layout

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        navController = Navigation.findNavController(this, R.id.navHostFragment)
    }
}