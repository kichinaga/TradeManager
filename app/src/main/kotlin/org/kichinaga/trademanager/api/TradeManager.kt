package org.kichinaga.trademanager.api

import io.reactivex.Observable
import io.reactivex.Single
import io.realm.RealmList
import org.kichinaga.trademanager.model.*
import retrofit2.http.*

/**
 * Created by kichinaga on 2017/10/12.
 */
interface TradeManager {
    /**
     * ログイン
     */
    @POST("auth/login")
    fun login(@Query("email") email: String,
              @Query("password") password: String
    ): Single<Auth>

    /**
     * 新規登録
     */
    @POST("auth/signup")
    fun signup(@Query("name") name: String,
               @Query("email") email: String,
               @Query("password") password: String,
               @Query("password_confirmation") password_confirmation: String
    ): Single<CreateResponse>

    /**
     * アカウントに紐づけられた企業のリストを入手
     */
    @GET("stock_lists")
    fun stockLists(@Query("token") access_token: String): Observable<RealmList<StockList>>
    /**
     * StockListを追加する
     */
    @POST("stock_lists/create")
    fun addStockLists(@Query("token") access_token: String,
                      @Query("id") company_id: Int): Observable<StockList>

    /**
     * 企業の株の購入履歴を入手
     */
    @GET("stocks")
    fun stocks(@Query("token") access_token: String,
               @Query("stock_code") stock_code: Int): Observable<RealmList<Stock>>
    /**
     * 会社の株を購入する
     */
    @POST("stocks/create")
    fun createStocks(@Query("token") access_token: String,
                     @Query("num") amount: Int,
                     @Query("price") price: Int,
                     @Query("action") action: String,
                     @Query("stock_list_id") id: Int): Single<CreateResponse>

    /**
     * 企業の詳細情報を入手する
     */
    @GET("companies/{stock_code}")
    fun company(@Path("stock_code") stock_code: Int): Observable<Company>

    @GET("companies/search")
    fun search(@Query("stock_code") stock_code: String = "",
               @Query("company_name") company_name: String = "",
               @Query("market_id") market_id: String = "",
               @Query("industry_id") industry_id: String = ""): Observable<List<Company>>
}