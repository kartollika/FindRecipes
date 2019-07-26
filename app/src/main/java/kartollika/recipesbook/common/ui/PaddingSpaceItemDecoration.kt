package kartollika.recipesbook.common.ui

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kartollika.recipesbook.common.utils.dpToPx

class PaddingSpaceItemDecoration constructor(
    private val context: Context,
    private var vertical: Int = 0,
    private var horizontal: Int = 0
) :
    RecyclerView.ItemDecoration() {

    init {
        vertical = dpToPx(vertical.toFloat(), context)
        horizontal = dpToPx(horizontal.toFloat(), context)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.also {
            it.left = horizontal
            it.right = horizontal
            it.top = vertical
        }
    }
}