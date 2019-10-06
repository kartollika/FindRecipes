package kartollika.recipesbook.common.utils

import android.view.View
import android.view.ViewTreeObserver

fun onRenderFinished(view: View, action: Runnable) {
    view.viewTreeObserver.addOnGlobalLayoutListener(
        object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)

                action.run()
            }
        }
    )
}

fun rotateView(view: View, degree: Float, withAnimation: Boolean, duration: Long) {
    if (withAnimation) {
        view.animate()
            .rotation(degree)
            .duration = duration
    } else {
        view.rotation = degree
    }
}
