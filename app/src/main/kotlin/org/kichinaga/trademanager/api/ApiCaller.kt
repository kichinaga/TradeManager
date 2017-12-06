package org.kichinaga.trademanager.api

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.kichinaga.trademanager.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by kichinaga on 2017/10/12.
 */
class ApiCaller {
    private val BASE_URL: String = "http://${BuildConfig.OBJECT_SERVER_IP}:3000/api/v1/"

    val client: TradeManager = getApiClient()

    // APIを呼ぶためのクライアントを生成
    private fun getApiClient(): TradeManager {
        val builder: Retrofit = Retrofit.Builder()
                .client(OkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                        MoshiConverterFactory.create(
                                Moshi.Builder()
                                        .add(AuthAdapter.FACTORY)
                                        .add(KotlinJsonAdapterFactory())
                                        .build()
                        )
                )
                .baseUrl(BASE_URL)
                .build()

        return builder.create(TradeManager::class.java)
    }

}