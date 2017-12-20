package org.kichinaga.trademanager.api.adapter

import com.squareup.moshi.*
import org.kichinaga.trademanager.model.Company

/**
 * Created by kichinaga on 2017/12/06.
 */

class CompanyAdapter : JsonAdapter<Company>(), BaseConvertAdapter {
    @FromJson
    override fun fromJson(reader: JsonReader?): Company? {
        reader ?: throw JsonDataException("json parse error")

        return readCompany(reader)
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Company?) {
        writer.value(value.toString())
    }
}