package kartollika.recipesbook

import android.app.Application
import kartollika.recipesbook.data.local.RecipesDatabase
import kartollika.recipesbook.di.DIManager

class App : Application() {

    companion object {
        lateinit var diManager: DIManager
    }

    override fun onCreate() {
        super.onCreate()
        diManager = DIManager(this)

        initializeDatabase()
    }

    private fun initializeDatabase() {
        RecipesDatabase.getInstance(this)
    }

}