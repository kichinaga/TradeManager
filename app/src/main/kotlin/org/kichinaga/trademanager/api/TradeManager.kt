package org.kichinaga.trademanager.api

import io.reactivex.Observable
import io.reactivex.Single
import org.kichinaga.trademanager.model.*
import retrofit2.http.*

/**
 * Created by kichinaga on 2017/10/12.
 */
interface TradeManager {

    @POST("auth/login")
    fun login(@Query("email") email: String,
              @Query("password") password: String
    ): Observable<Auth>

    @POST("auth/signup")
    fun signup(@Query("name") name: String,
               @Query("email") email: String,
               @Query("password") password: String,
               @Query("password_confirmation") password_confirmation: String
    ): Single<SignUpResponse>

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