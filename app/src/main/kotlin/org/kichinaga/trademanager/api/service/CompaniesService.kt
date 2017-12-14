package org.kichinaga.trademanager.api.service

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.kichinaga.trademanager.api.ApiCaller
import org.kichinaga.trademanager.api.adapter.CompanySearchAdapter
import org.kichinaga.trademanager.model.Company

/**
 * Created by kichinaga on 2017/10/12.
 */
class CompaniesService {
    private val companies = BehaviorSubject.create<List<Company>>()
    private val caller = ApiCaller(CompanySearchAdapter())

    fun get(code: String, name: String, market: String, industry: String): Observable<List<Company>> {
        val item = companies.value

        if (item != null){
            return companies
        }

        return fetch(code, name, market, industry)
    }

    fun fetch(code: String, name: String, market: String, industry: String): Observable<List<Company>>{
        caller.client
                .search(stock_code = code, company_name = name, market_id = market, industry_id = industry)
                .subscribeOn(Schedulers.newThread())
                .subscribe({
                    companies.onNext(it)
                },{
                    it.printStackTrace()
                })

        return companies
    }
}