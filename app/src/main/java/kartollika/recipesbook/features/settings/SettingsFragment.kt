package kartollika.recipesbook.features.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kartollika.recipesbook.App
import kartollika.recipesbook.BuildConfig
import kartollika.recipesbook.R
import kartollika.recipesbook.common.base.BaseFragment
import kartollika.recipesbook.common.ui.ApplyingBottomSheetDialog
import kartollika.recipesbook.common.utils.createSnackbar
import kartollika.recipesbook.common.utils.injectViewModel
import kartollika.recipesbook.features.MainActivity
import kartollika.recipesbook.features.search_recipes.SearchRecipesFragment
import kartollika.recipesbook.features.viewmodels.SettingsViewModel
import kotlinx.android.synthetic.main.settings_fragment_layout.*


class SettingsFragment : BaseFragment() {

    private val viewModel: SettingsViewModel by injectViewModel { App.diManager.applicationComponent!!.settingsViewModel }

    private lateinit var bottomNavigation: View

    override fun getLayoutRes(): Int = R.layout.settings_fragment_layout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        initListeners()
        initObservers()
        initAboutAppInfo()
    }

    private fun initViews() {
        bottomNavigation = activity!!.findViewById(R.id.bottomNavigationView)
    }

    private fun initAboutAppInfo() {
        settingsVersionNameTextView.text = BuildConfig.VERSION_NAME
        settingsVersionCodeTextView.text = BuildConfig.VERSION_CODE.toString()
    }


    private fun initListeners() {
        settingsClearCacheAction.setOnClickListener {
            viewModel.clearImagesCache()
        }

        settingsSetupIntoleranceAction.setOnClickListener {
            (activity as MainActivity).navigateFullScreen(SearchRecipesFragment())
        }

        settingsSignInGoogleAction.setOnClickListener {

        }

        settingsSetupIntoleranceAction.setOnClickListener {
            IntoleranceDefineDialogFragment().apply {
                setTitle("Define your intolerance")
                setCloseDialogListener(object : ApplyingBottomSheetDialog.OnCloseDialogListener {
                    override fun onApply() {
                        dismiss()
                        Toast.makeText(
                            requireContext(),
                            "Intolerance was saved",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onCanceled() {
                    }
                })
            }.show(childFragmentManager, "")
        }
    }

    private fun initObservers() {
        viewModel.getClearCacheDataObservable().observe(viewLifecycleOwner, Observer {
            if (it.hasBeenHandled) {
                return@Observer
            }

            when (it.getContentIfNotHandled()) {
                true -> {
                    createSnackbar(view!!, "Cache cleared", Snackbar.LENGTH_SHORT)
                        .setAnchorView(bottomNavigation).show()
                }
                false -> {
                    createSnackbar(view!!, "Error occured on clearing cache", Snackbar.LENGTH_SHORT)
                        .setAnchorView(bottomNavigation).show()
                }
            }
        })
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}
