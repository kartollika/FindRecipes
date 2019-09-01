package kartollika.recipesbook.common.utils

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.internal.schedulers.IoScheduler

fun <T> wrapSingleAndRun(
    callable: () -> T,
    scheduler: Scheduler = IoScheduler()
) {
    Single.fromCallable { callable }
        .subscribeOn(scheduler)
        .subscribe()
}