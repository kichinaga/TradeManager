package org.kichinaga.trademanager.api.adapter

import com.squareup.moshi.*
import io.realm.RealmList
import org.kichinaga.trademanager.model.Stock

/**
 * Created by kichinaga on 2017/12/18.
 */
class StockAdapter: JsonAdapter<RealmList<Stock>>(), BaseConvertAdapter {
    @FromJson
    override fun fromJson(reader: JsonReader?): RealmList<Stock>? {
        reader ?: throw JsonDataException("json parse error")
        val list = RealmList<Stock>()

        reader.beginArray()
        while (reader.hasNext()) list.add(readStocks(reader))
        reader.endArray()

        return list
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: RealmList<Stock>?) {
        writer.value(value.toString())
    }
}