package kartollika.recipesbook.common.utils

import android.app.Activity
import android.content.Context

fun Context.castToActivity(): Activity? = this as? Activity