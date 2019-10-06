package kartollika.recipesbook.common.utils

import android.content.Context
import android.util.TypedValue

fun dp(dp: Int, context: Context): Int {
    return dpFloat(dp.toFloat(), context).toInt()
}

private fun dpFloat(dp: Float, context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    )
}