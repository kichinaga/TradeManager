package org.kichinaga.trademanager.api.adapter

import com.squareup.moshi.*
import org.kichinaga.trademanager.model.Industry

/**
 * Created by kichinaga on 2017/12/06.
 */
class IndustryAdapter : JsonAdapter<Industry>(), BaseConvertAdapter {
    @FromJson
    override fun fromJson(reader: JsonReader?): Industry? {
        reader ?: throw JsonDataException("json parse error")
        return readIndustry(reader)
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Industry?) {
        writer.value(value.toString())
    }
}