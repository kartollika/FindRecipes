package kartollika.recipiesbook

import android.app.Application
import kartollika.recipiesbook.di.DIManager

class App : Application() {

    companion object {
        lateinit var applicationComponent: DIManager
    }

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DIManager(this)
    }

}