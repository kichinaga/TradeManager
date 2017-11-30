package org.kichinaga.trademanager.api

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.kichinaga.trademanager.model.Industry

/**
 * Created by kichinaga on 2017/10/12.
 */
class IndustriesServise {
    private val industries: BehaviorSubject<List<Industry>> = BehaviorSubject.create()
    private val caller: ApiCaller = ApiCaller()

    fun get(): Observable<List<Industry>> {
        val list = industries.value

        if (list != null && !list.isEmpty()){
            return industries
        }

        return fetch()
    }

    private fun fetch(): Observable<List<Industry>> {
        caller.client
                .industries()
                .subscribeOn(Schedulers.newThread())
                .subscribe({
                    industries.onNext(it)
                },{
                    it.printStackTrace()
                })

        return industries
    }
}