package kartollika.recipesbook.common.base.lists.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)
}