package kartollika.recipesbook.features

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kartollika.recipesbook.R
import kartollika.recipesbook.common.base.BaseFragment
import kotlinx.android.synthetic.main.hub_layout.*

class HubFragment : BaseFragment() {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var navController: NavController

    override fun getContentView(): Int = R.layout.hub_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        navController = Navigation.findNavController(requireActivity(), R.id.bottom_navigation_container)
        bottomNavigation = bottomNavigationView.apply {
            setupWithNavController(navController)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}