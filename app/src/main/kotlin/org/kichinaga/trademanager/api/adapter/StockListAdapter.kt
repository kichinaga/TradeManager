package org.kichinaga.trademanager.api.adapter

import com.squareup.moshi.*
import io.realm.RealmList
import org.kichinaga.trademanager.model.StockList

/**
 * Created by kichinaga on 2017/12/07.
 */
class StockListAdapter: JsonAdapter<RealmList<StockList>>(), BaseConvertAdapter {
    @FromJson
    override fun fromJson(reader: JsonReader?): RealmList<StockList>? {
        reader ?: throw JsonDataException("json parse error")

        val list = RealmList<StockList>()

        reader.beginObject()
        while (reader.hasNext()){
            when(reader.nextName()){
                "stock_lists" -> {
                    reader.beginArray()
                    while (reader.hasNext()) list.add(readStockLists(reader))
                    reader.endArray()
                }
            }
        }
        reader.endObject()

        return list
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: RealmList<StockList>?) {
        writer.value(value.toString())
    }
}