package org.kichinaga.trademanager.api.service

import android.content.Context
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.realm.RealmList
import org.kichinaga.trademanager.api.ApiCaller
import org.kichinaga.trademanager.api.adapter.StockListAdapter
import org.kichinaga.trademanager.extensions.getAccessToken
import org.kichinaga.trademanager.model.StockList

/**
 * Created by kichinaga on 2017/12/08.
 */
class StockListService {
    private val stockLists = BehaviorSubject.create<RealmList<StockList>>()
    private val caller = ApiCaller(StockListAdapter())

    fun get(context: Context): Observable<RealmList<StockList>> {
        val list = stockLists.value

        if (list != null && !list.isEmpty()){
            return stockLists
        }

        return fetch(context)
    }

    fun fetch(context: Context): Observable<RealmList<StockList>> {
        caller.client
                .stockLists(getAccessToken(context))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    stockLists.onNext(it)
                },{
                    it.printStackTrace()
                })

        return stockLists
    }
}