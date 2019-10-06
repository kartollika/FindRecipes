package kartollika.recipesbook.features

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import kartollika.recipesbook.R
import kartollika.recipesbook.common.base.BaseFragment
import kartollika.recipesbook.features.search_recipes.SearchRecipesFragment
import kartollika.recipesbook.features.settings.SettingsFragment
import kotlinx.android.synthetic.main.hub_layout.*

class HubFragment : BaseFragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var bottomNavigation: BottomNavigationView

    private val fragmentsList = hashMapOf<String, BaseFragment>()

    override fun getLayoutRes(): Int = R.layout.hub_layout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomNavigation()
        openHomeTab()
    }

    private fun openHomeTab() {
        bottomNavigation.selectedItemId = R.id.recipiesSearchFragment
    }

    private fun setupBottomNavigation() {
        bottomNavigation = bottomNavigationView.apply { setOnNavigationItemSelectedListener(this@HubFragment) }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val fragmentTransaction = childFragmentManager.beginTransaction()

        val fragment: BaseFragment = when (menuItem.itemId) {
            R.id.recipiesSearchFragment -> {
                if (fragmentsList["Search Recipes"] == null) {
                    fragmentsList["Search Recipes"] = SearchRecipesFragment.newInstance()
                }
                fragmentsList["Search Recipes"]!!
            }

//            R.id.mealTimeFragment -> {
//                childFragmentManager.findFragmentByTag("Meal time") ?: SearchRecipesFragment.newInstance()
//            }

            R.id.otherMenuItemFragment -> {
                if (fragmentsList["Settings"] == null) {
                    fragmentsList["Settings"] = SettingsFragment.newInstance()
                }
                fragmentsList["Settings"]!!
            }
            else -> throw IllegalArgumentException("Wrong bottom sheet chosen id")
        }

        fragmentTransaction
            .replace(R.id.hub_container, fragment)
            .commit()
        return true
    }
}