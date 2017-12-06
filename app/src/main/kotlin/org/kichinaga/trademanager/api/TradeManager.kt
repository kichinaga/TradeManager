package org.kichinaga.trademanager.api

import io.reactivex.Observable
import org.kichinaga.trademanager.model.Auth
import org.kichinaga.trademanager.model.Company
import org.kichinaga.trademanager.model.Industry
import org.kichinaga.trademanager.model.Market
import retrofit2.http.*

/**
 * Created by kichinaga on 2017/10/12.
 */
interface TradeManager {

    /* hoge */
    @POST("auth/login")
    fun login(@Query("email") email: String, @Query("password") password: String): Observable<Auth>

    /* /api/v1/companies */
    @GET("companies")
    fun companies(): Observable<List<Company>>

    @GET("companies/{stock_code}")
    fun company(@Path("stock_code") stock_code: Int): Observable<Company>

    /* /api/v1/markets */
    @GET("markets")
    fun markets(): Observable<List<Market>>

    /* /api/v1/industries */
    @GET("industries")
    fun industries(): Observable<List<Industry>>
}