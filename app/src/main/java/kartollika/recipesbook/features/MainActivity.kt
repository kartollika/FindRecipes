package kartollika.recipesbook.features

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kartollika.recipesbook.R
import kartollika.recipesbook.common.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun getLayoutRes(): Int = R.layout.main_activity_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateFullScreen(HubFragment(), false)
    }

    fun navigateFullScreen(
        fragment: Fragment,
        backstack: Boolean = true,
        sharedElements: Map<View, String> = emptyMap()
    ) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.root, fragment)
            if (backstack) {
                addToBackStack("")
            }
            for ((view, tag) in sharedElements) {
                addSharedElement(view, tag)
            }
        }.commitAllowingStateLoss()
    }

    fun navigateUpFullScreen() {
        supportFragmentManager.popBackStack()
    }

    fun navigateBottomSheet(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.test_bottom_sheet_container, fragment)
            .commit()
    }

    fun showDialogFragment(fragment: DialogFragment) {
        fragment.show(supportFragmentManager, "")
    }
}