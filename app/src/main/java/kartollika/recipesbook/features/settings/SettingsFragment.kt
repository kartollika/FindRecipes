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
import kartollika.recipesbook.common.utils.injectViewModel
import kartollika.recipesbook.features.MainActivity
import kartollika.recipesbook.features.search_recipes.SearchRecipesFragment
import kartollika.recipesbook.features.viewmodels.SettingsViewModel
import kotlinx.android.synthetic.main.settings_fragment_layout.*


class SettingsFragment : BaseFragment() {

    private val viewModel: SettingsViewModel by injectViewModel { App.diManager.applicationComponent!!.settingsViewModel }

    override fun getLayoutRes(): Int = R.layout.settings_fragment_layout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initObservers()

        initAboutAppInfo()
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
                        Toast.makeText(requireContext(), "Intolerance was saved", Toast.LENGTH_SHORT).show()
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
                ClearCacheState.Uninitialized -> {
                    settingsClearCacheProgress.visibility = View.GONE
                }
                ClearCacheState.Running -> {
                    settingsClearCacheProgress.visibility = View.VISIBLE
                }
                ClearCacheState.Finished -> {
                    settingsClearCacheProgress.visibility = View.GONE
                    Snackbar.make(
                        activity!!.findViewById<View>(R.id.test_bottom_sheet_container),
                        it.peekContent().message,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                ClearCacheState.Error -> {
                    settingsClearCacheProgress.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        it.peekContent().message, Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}
