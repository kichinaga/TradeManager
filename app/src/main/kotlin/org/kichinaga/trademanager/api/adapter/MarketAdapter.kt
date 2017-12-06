package org.kichinaga.trademanager.api.adapter

import com.squareup.moshi.*
import org.kichinaga.trademanager.model.Market

/**
 * Created by kichinaga on 2017/12/06.
 */
class MarketAdapter : JsonAdapter<Market>(), BaseConvertAdapter {
    @FromJson
    override fun fromJson(reader: JsonReader?): Market? {
        reader ?: throw JsonDataException("json parse error")
        return readMarket(reader)
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Market?) {
        writer.value(value.toString())
    }
}