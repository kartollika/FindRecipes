package kartollika.recipesbook.features

import android.os.Bundle
import android.view.View
import kartollika.recipesbook.R
import kartollika.recipesbook.common.base.BaseFragment

class OtherFragment : BaseFragment() {

    override fun getContentView(): Int = R.layout.other_fragment_layout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {

    }

}