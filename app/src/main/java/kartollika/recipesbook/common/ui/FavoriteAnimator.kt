package kartollika.recipesbook.common.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.util.Property
import android.view.View

object FavoriteAnimator {

    fun animateFavoriteView(favoriteView: View, isFavorite: Boolean) {
        val animatorSet = AnimatorSet()

        if (isFavorite) {
            animatorSet.playTogether(
                animateFavorite(favoriteView, View.SCALE_X),
                animateFavorite(favoriteView, View.SCALE_Y)
            )
        } else {
            animatorSet.playTogether(
                animateUnfavorite(favoriteView, View.SCALE_X),
                animateUnfavorite(favoriteView, View.SCALE_Y)
            )
        }
        animatorSet.start()
    }

    private fun animateFavorite(view: View, property: Property<View, Float>): AnimatorSet =
        AnimatorSet().apply {
            playSequentially(
                ObjectAnimator.ofFloat(view, property, 1f).setDuration(0),
                ObjectAnimator.ofFloat(view, property, 1.3f).setDuration(75),
                ObjectAnimator.ofFloat(view, property, 0.95f).setDuration(140),
                ObjectAnimator.ofFloat(view, property, 1.05f).setDuration(165),
                ObjectAnimator.ofFloat(view, property, 1f).setDuration(100)
            )
        }

    private fun animateUnfavorite(view: View, property: Property<View, Float>): AnimatorSet =
        AnimatorSet().apply {
            playSequentially(
                ObjectAnimator.ofFloat(view, property, 1f).setDuration(0),
                ObjectAnimator.ofFloat(view, property, 0.85f).setDuration(165),
                ObjectAnimator.ofFloat(view, property, 1f).setDuration(100)
            )
        }
}