package org.kichinaga.trademanager.listview

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager



/**
 * Created by kichinaga on 2017/12/11.
 */
abstract class EndlessScrollListener(private val visibleThreshold: Int = 20, private val linearLayoutManager: LinearLayoutManager): RecyclerView.OnScrollListener() {
    private var previousTotal = 0
    private var loading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = recyclerView.childCount
        val totalItemCount = linearLayoutManager.itemCount
        val firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }

        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {

            onLoadMore(totalItemCount)

            loading = true
        }
    }

    abstract fun onLoadMore(totalItemCount: Int)
}