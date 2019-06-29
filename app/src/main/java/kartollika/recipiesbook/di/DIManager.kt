package kartollika.recipiesbook.di

import android.app.Application

class DIManager(private var application: Application) {

    var applicationComponent: ApplicationComponent? = null
        get() {
            if (field == null) {
                field = DaggerApplicationComponent.factory()
                    .create(application)
            }
            return field
        }
}