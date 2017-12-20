package org.kichinaga.trademanager.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import io.realm.Realm
import io.realm.Sort
import kotlinx.android.synthetic.main.fragment_listview.*
import org.kichinaga.trademanager.R
import org.kichinaga.trademanager.api.service.StockListService
import org.kichinaga.trademanager.extensions.getShapingList
import org.kichinaga.trademanager.listview.StockListAdapter
import org.kichinaga.trademanager.model.StockList

/**
 * Created by kichinaga on 2017/12/07.
 */
class StockListFragment: Fragment() {
    private val realm: Realm = Realm.getDefaultInstance()
    private val service = StockListService()
    private var adapter: StockListAdapter? = null
    private val listLimit = 20

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_listview, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress(true)
        service.get(activity)
                .subscribe { realmlist ->
                    realm.executeTransaction { it.copyToRealmOrUpdate(realmlist) }
                    val list = getShapingList(realmlist)
                    adapter = StockListAdapter(activity, list)
                    Handler(activity.mainLooper).post {
                        lists_view.adapter = adapter
                        showProgress(false)
                    }
                }

        lists_view.setOnScrollListener(object : AbsListView.OnScrollListener{
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {}
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                val list = realm.where(StockList::class.java).findAllSorted("stock_code", Sort.ASCENDING)
                if (totalItemCount > 0 && totalItemCount == (firstVisibleItem + visibleItemCount) && list.size > totalItemCount + listLimit){
                    Handler(activity.mainLooper).post { adapter?.addItem(getShapingList(list, totalItemCount, totalItemCount + listLimit)) }
                }
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun showProgress(show: Boolean = false) {
        list_progress.visibility = if (show) View.VISIBLE else View.GONE
    }
}