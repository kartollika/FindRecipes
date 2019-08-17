package kartollika.recipesbook.common.utils.lists.chip

import android.view.View

abstract class BaseChipHolder<T>(private val view: View) {
    fun getView(): View = view
}