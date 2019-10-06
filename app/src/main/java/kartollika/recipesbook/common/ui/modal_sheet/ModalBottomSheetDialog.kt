package kartollika.recipesbook.common.ui.modal_sheet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import kartollika.recipesbook.R
import kotlinx.android.synthetic.main.modal_bottom_sheet_dialog.view.*

class ModalBottomSheetDialog(context: Context?, theme: Int) : AppCompatDialog(context, theme) {

    private lateinit var titleView: TextView
    private lateinit var containerView: View
    private lateinit var recyclerView: RecyclerView



    override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {
        super.setContentView(wrapContentView(view, params))
    }

    fun setToolbarTitle(title: CharSequence) {
        titleView.text = title
    }

    private fun wrapContentView(view: View, params: ViewGroup.LayoutParams?): View {
        val root = LayoutInflater.from(context).inflate(R.layout.modal_bottom_sheet_dialog, null) as CoordinatorLayout
        titleView = root.modal_sheet_title

        val designBottomSheet = root.findViewById<View>(R.id.design_bottom_sheet)
//            val layoutParams = this.layoutParams as CoordinatorLayout.LayoutParams
//            layoutParams.behavior = behavior
//        }

//        val bottomSheetBehavior = BottomSheetBehavior.from(designBottomSheet)
//        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        return root
    }
}