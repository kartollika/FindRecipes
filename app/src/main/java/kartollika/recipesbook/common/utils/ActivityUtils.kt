package kartollika.recipesbook.common.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbarShort(message: String) {
    createSnackbar(this, message, Snackbar.LENGTH_SHORT).show()
}

fun View.showSnackbarLong(message: String) {
    createSnackbar(this, message, Snackbar.LENGTH_LONG).show()
}

fun View.showSnackbarIndefinite(message: String) {
    createSnackbar(this, message, Snackbar.LENGTH_INDEFINITE).show()
}

fun createSnackbar(view: View, message: String, length: Int): Snackbar =
    Snackbar.make(view, message, length)

fun Context.createShortToast(message: String) {
    createToast(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.createLongToast(message: String) {
    createToast(this, message, Toast.LENGTH_LONG).show()
}

fun createToast(context: Context, message: String, length: Int): Toast =
    Toast.makeText(context, message, length)
