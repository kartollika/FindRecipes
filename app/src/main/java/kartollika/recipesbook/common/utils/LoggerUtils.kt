package kartollika.recipesbook.common.utils

import android.util.Log

object Log {

    private val TAG = this.javaClass.simpleName

    private enum class LoggerLevel {
        e, d, w
    }

    public fun Throwable.e() = log(LoggerLevel.e, this)
    public fun Throwable.d() = log(LoggerLevel.d, this)
    public fun Throwable.w() = log(LoggerLevel.w, this)

    private fun log(loggerLevel: LoggerLevel, thr: Throwable) {
        when (loggerLevel) {
            LoggerLevel.e -> Log.e(TAG, thr.message, thr)
            LoggerLevel.d -> Log.d(TAG, thr.message, thr)
            LoggerLevel.w -> Log.w(TAG, thr.message, thr)
        }
    }
}