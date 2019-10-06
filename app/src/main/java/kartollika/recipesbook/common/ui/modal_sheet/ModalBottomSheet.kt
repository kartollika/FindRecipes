package kartollika.recipesbook.common.ui.modal_sheet

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalBottomSheet : BottomSheetDialogFragment(), Dismissable {

    private var title: CharSequence? = null
    private var customView: View? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = ModalBottomSheetDialog(context, theme).apply {
            if (customView != null) {
                setContentView(
                    customView!!,
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
            }
        }

        val bottomSheet =
            (dialog as BottomSheetDialog).findViewById(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
        return dialog
    }

    fun hide() {
        dialog?.dismiss()
    }

    @JvmOverloads
    fun show(tag: String? = null, fragmentManager: FragmentManager) {
        show(fragmentManager, tag)
    }

    override fun dismiss() {
        super.dismiss()
    }

    class Builder(private val context: Context) {

        private var title: CharSequence? = null
        private var view: View? = null

        fun setTitle(@StringRes titleRes: Int) = apply {
            title = context.getString(titleRes)
        }

        fun setTitle(title: CharSequence) = apply {
            this@Builder.title = title
        }

        fun setCustomView(view: View) = apply {
            this@Builder.view = view
        }

        fun create() = ModalBottomSheet().apply {
            this.title = this@Builder.title
            this.customView = this@Builder.view
        }
    }
}