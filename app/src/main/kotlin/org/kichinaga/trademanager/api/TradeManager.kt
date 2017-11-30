package org.kichinaga.trademanager.api

import io.reactivex.Observable
import org.kichinaga.trademanager.model.Company
import org.kichinaga.trademanager.model.Industry
import org.kichinaga.trademanager.model.Market
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by kichinaga on 2017/10/12.
 */
interface TradeManager {

    @GET("companies")
    fun companies(): Observable<List<Company>>

    @GET("companies/{stock_code}")
    fun company(@Path("stock_code") stock_code: Int): Observable<Company>

    @GET("markets")
    fun markets(): Observable<List<Market>>

    @GET("industries")
    fun industries(): Observable<List<Industry>>
}