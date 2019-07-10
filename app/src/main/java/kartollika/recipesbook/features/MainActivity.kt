package kartollika.recipesbook.features

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.fragment.app.Fragment
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

    fun replaceFragment(fragment: Fragment, sharedElements: Map<View, String>) {
        val transaction = supportFragmentManager.beginTransaction()
        for ((sharedView, transitionName) in sharedElements) {
            transaction.addSharedElement(sharedView, transitionName)
        }
        transaction
            .replace(R.id.navHostFragment, fragment)
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .commit()

    }
}