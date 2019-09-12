package kartollika.recipesbook.features.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import kartollika.recipesbook.App
import kartollika.recipesbook.BuildConfig
import kartollika.recipesbook.R
import kartollika.recipesbook.common.base.BaseFragment
import kartollika.recipesbook.common.ui.ApplyingBottomSheetDialog
import kartollika.recipesbook.common.utils.injectViewModel
import kartollika.recipesbook.common.utils.showSnackbarShort
import kartollika.recipesbook.features.MainActivity
import kartollika.recipesbook.features.search_recipes.SearchRecipesFragment
import kartollika.recipesbook.features.viewmodels.SettingsViewModel
import kotlinx.android.synthetic.main.settings_fragment_layout.*


class SettingsFragment : BaseFragment() {

    private val viewModel: SettingsViewModel by injectViewModel { App.diManager.applicationComponent!!.settingsViewModel }

    private lateinit var bottomSheetContainer: View

    override fun getLayoutRes(): Int = R.layout.settings_fragment_layout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        initListeners()
        initObservers()
        initAboutAppInfo()
    }

    private fun initViews() {
        bottomSheetContainer = activity!!.findViewById(R.id.test_bottom_sheet_container)
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
                setCloseDialogListener(object : ApplyingBottomSheetDialog.OnCloseDialogListener {
                    override fun onApply() {
                        dismiss()
                        bottomSheetContainer.showSnackbarShort("Intolerance was saved")
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
                    bottomSheetContainer.showSnackbarShort("Cache cleared")
                }
                false -> {
                    bottomSheetContainer.showSnackbarShort("Error occured on clearing cache")
                }
            }
        })
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}
