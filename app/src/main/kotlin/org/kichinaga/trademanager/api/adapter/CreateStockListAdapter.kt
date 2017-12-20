package org.kichinaga.trademanager.api.adapter

import com.squareup.moshi.*
import org.kichinaga.trademanager.model.StockList

/**
 * Created by kichinaga on 2017/12/14.
 */
class CreateStockListAdapter: JsonAdapter<StockList>(), BaseConvertAdapter {
    @FromJson
    override fun fromJson(reader: JsonReader?): StockList? {
        reader ?: throw JsonDataException("json parse error")
        return readStockLists(reader)
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: StockList?) {
        writer.value(value.toString())
    }
}