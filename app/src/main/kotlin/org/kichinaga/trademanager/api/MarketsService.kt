package org.kichinaga.trademanager.api

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.kichinaga.trademanager.model.Market

/**
 * Created by kichinaga on 2017/10/12.
 */
class MarketsService {
    private val markets: BehaviorSubject<MutableList<Market>> = BehaviorSubject.create()
    private val caller: ApiCaller = ApiCaller()

    fun get(): Observable<MutableList<Market>> {
        val list = markets.value

        if (list != null && !list.isEmpty()){
            return markets
        }

        return fetch()
    }

    private fun fetch(): Observable<MutableList<Market>> {
        caller.client
                .markets()
                .subscribeOn(Schedulers.newThread())
                .subscribe({
                    markets.onNext(it.toMutableList())
                },{
                    it.printStackTrace()
                })

        return markets
    }
}