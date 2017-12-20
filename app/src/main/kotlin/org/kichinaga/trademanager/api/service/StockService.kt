package org.kichinaga.trademanager.api.service

import android.content.Context
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.realm.RealmList
import org.kichinaga.trademanager.api.ApiCaller
import org.kichinaga.trademanager.api.adapter.StockAdapter
import org.kichinaga.trademanager.extensions.getAccessToken
import org.kichinaga.trademanager.model.Stock

/**
 * Created by kichinaga on 2017/12/18.
 */
class StockService {
    private val stocks = BehaviorSubject.create<RealmList<Stock>>()
    private val caller = ApiCaller(StockAdapter())

    fun get(context: Context, stock_code: Int): Observable<RealmList<Stock>> {
        val list = stocks.value

        if (list != null && !list.isEmpty()){
            return stocks
        }

        return fetch(context,stock_code)
    }

    fun fetch(context: Context, stock_code: Int): Observable<RealmList<Stock>> {
        caller.client
                .stocks(getAccessToken(context), stock_code)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    stocks.onNext(it)
                },{
                    it.printStackTrace()
                })

        return stocks
    }
}