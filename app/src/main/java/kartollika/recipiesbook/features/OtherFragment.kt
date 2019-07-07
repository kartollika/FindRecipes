package kartollika.recipiesbook.features

import android.os.Bundle
import android.view.View
import kartollika.recipiesbook.R
import kartollika.recipiesbook.common.base.BaseFragment

class OtherFragment : BaseFragment() {

    override fun getContentView(): Int = R.layout.other_fragment_layout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {

    }

}