package kartollika.recipesbook.features

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kartollika.recipesbook.R
import kartollika.recipesbook.common.base.BaseActivity
import kotlinx.android.synthetic.main.main_hub_activity.*

class MainHubActivity : BaseActivity() {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var navController: NavController

    override fun getLayoutRes(): Int = R.layout.main_hub_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        navController = Navigation.findNavController(this, R.id.container)

        bottomNavigation = bottomNavigationView.apply {
            setupWithNavController(navController)
        }
    }
}