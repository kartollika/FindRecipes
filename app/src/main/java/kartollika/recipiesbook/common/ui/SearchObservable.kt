package kartollika.recipiesbook.common.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import java.util.concurrent.TimeUnit

fun EditText.createSearchDelayedObservable(interval: Long): Observable<String> =
    Observable.create { emitter: ObservableEmitter<String> ->
        run {
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    emitter.onNext(s.toString())
                }
            })
        }
    }
        .debounce(interval, TimeUnit.MILLISECONDS)