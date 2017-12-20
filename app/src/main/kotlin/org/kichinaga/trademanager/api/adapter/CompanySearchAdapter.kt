package org.kichinaga.trademanager.api.adapter

import com.squareup.moshi.*
import org.kichinaga.trademanager.model.Company

/**
 * Created by kichinaga on 2017/12/13.
 */
class CompanySearchAdapter: JsonAdapter<List<Company>>(), BaseConvertAdapter {
    @FromJson
    override fun fromJson(reader: JsonReader?): List<Company>? {
        reader ?: throw JsonDataException("json parse error")
        val list = mutableListOf<Company>()

        reader.beginArray()
        while (reader.hasNext()) list.add(readSearchCompany(reader))
        reader.endArray()

        return list.toList()
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: List<Company>?) {
        writer.value(value.toString())
    }
}