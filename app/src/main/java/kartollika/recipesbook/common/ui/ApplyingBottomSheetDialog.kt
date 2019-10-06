package kartollika.recipesbook.common.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kartollika.recipesbook.R

abstract class ApplyingBottomSheetDialog : BottomSheetDialogFragment() {

    interface OnCloseDialogListener {
        fun onApply()
        fun onCanceled()
    }

    private lateinit var titleView: TextView
    private lateinit var applyView: View

    protected val callbacks: MutableList<BottomSheetBehavior.BottomSheetCallback> = mutableListOf()
    protected var onCloseDialogListener: OnCloseDialogListener? = null
    private var title: CharSequence = ""
    private var applyAction: (() -> Unit)? = null

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<out View>

    abstract fun getLayoutRes(): Int

    fun setCloseDialogListener(listener: OnCloseDialogListener) {
        onCloseDialogListener = listener
    }

    fun setTitle(title: CharSequence) {
        this.title = title
    }

    fun setTitle(@StringRes titleRes: Int) {
        title = context?.getString(titleRes) ?: ""
    }

    fun setOnApplyListener(applyAction: () -> Unit) {
        this.applyAction = applyAction
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.modal_bottom_sheet_dialog, container, false)

        view.findViewById<ViewGroup>(R.id.modal_sheet_content_holder)
            .addView(inflater.inflate(getLayoutRes(), null))

        titleView = view.findViewById(R.id.modal_sheet_title)
        applyView = view.findViewById(R.id.modal_sheet_apply)

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener {
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!).apply {

                isFitToContents = true
                skipCollapsed = true
                state = BottomSheetBehavior.STATE_EXPANDED
                setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(p0: View, p1: Float) {
                        for (callback in callbacks) {
                            callback.onSlide(p0, p1)
                        }
                    }

                    override fun onStateChanged(p0: View, p1: Int) {
                        for (callback in callbacks) {
                            callback.onStateChanged(p0, p1)
                        }
                    }
                })
            }

            callbacks.add(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                        onCloseDialogListener?.onCanceled()
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }
            })
        }

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleView.text = title
        applyView.setOnClickListener { onCloseDialogListener?.onApply() }
    }

    fun addCallback(callback: BottomSheetBehavior.BottomSheetCallback) {
        callbacks.add(callback)
    }
}