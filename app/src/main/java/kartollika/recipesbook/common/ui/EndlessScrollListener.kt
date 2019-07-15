package kartollika.recipesbook.common.ui

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessScrollListener : RecyclerView.OnScrollListener() {

    var visibleThreshold = 5
    private var loading: Boolean = false
    private var previousTotal = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val nowVisibleCount = recyclerView.childCount
        val totalCount = recyclerView.layoutManager?.itemCount!!
        val firstVisibleItem =
            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        // Checks dynamically on scroll, if more data was loaded
        if (loading) {
            if (totalCount > previousTotal) {
                loading = false
                previousTotal = totalCount
            }
        }

        if (!loading && (totalCount - nowVisibleCount) <= (firstVisibleItem + visibleThreshold)) {
            onLoadMoreItems()
            loading = true
        }
    }

    abstract fun onLoadMoreItems()
}