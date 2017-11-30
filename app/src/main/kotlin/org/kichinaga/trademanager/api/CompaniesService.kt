package org.kichinaga.trademanager.api

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.kichinaga.trademanager.model.Company

/**
 * Created by kichinaga on 2017/10/12.
 */
class CompaniesService {
    private val companies: BehaviorSubject<List<Company>> = BehaviorSubject.create()
    private val caller: ApiCaller = ApiCaller()

    fun get(): Observable<List<Company>> {
        val list = companies.value

        if (list != null && !list.isEmpty()){
            return companies
        }

        return fetch()
    }

    private fun fetch(): Observable<List<Company>>{
        caller.client
                .companies()
                .subscribeOn(Schedulers.newThread())
                .subscribe({
                    companies.onNext(it)
                },{
                    it.printStackTrace()
                })

        return companies
    }
}