package org.kichinaga.trademanager.api

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.kichinaga.trademanager.BuildConfig
import org.kichinaga.trademanager.api.adapter.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by kichinaga on 2017/10/12.
 */
class ApiCaller<T: BaseConvertAdapter>(adapter: T) {

    companion object {
        fun getDefaultApiClient(): TradeManager =
                Retrofit.Builder().client(OkHttpClient())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(MoshiConverterFactory.create())
                        .baseUrl("http://${BuildConfig.OBJECT_SERVER_IP}:3000/api/v1/")
                        .build().create(TradeManager::class.java)
    }
    // private val BASE_URL: String = "http://${BuildConfig.OBJECT_SERVER_IP}:3000/api/v1/"
    private val BASE_URL: String = "http://10.0.2.2:3000/api/v1/"

    val client: TradeManager = getApiClient(adapter)

    // APIを呼ぶためのクライアントを生成
    private fun <T> getApiClient(adapter: T): TradeManager {
        var builder: Retrofit? = null
        when(adapter){
            is AuthAdapter,
            is StockListAdapter,
            is CompanyAdapter,
            is CompanySearchAdapter,
            is CreateStockListAdapter,
            is StockAdapter
            -> builder =  getRetrofitBuilder(adapter)
        }

        return builder?.create(TradeManager::class.java)!!
    }

    private fun <T> getRetrofitBuilder(adapter: T): Retrofit =
            Retrofit.Builder()
                    .client(OkHttpClient())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            MoshiConverterFactory.create(
                                    Moshi.Builder()
                                            .add(adapter)
                                            .add(KotlinJsonAdapterFactory())
                                            .build()
                            )
                    )
                    .baseUrl(BASE_URL)
                    .build()

}