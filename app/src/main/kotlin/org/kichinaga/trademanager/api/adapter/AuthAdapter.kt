package org.kichinaga.trademanager.api.adapter

import com.squareup.moshi.*
import org.kichinaga.trademanager.model.*

/**
 * Created by kichinaga on 2017/12/05.
 */
class AuthAdapter : JsonAdapter<Auth>(), BaseConvertAdapter {

    @FromJson
    override fun fromJson(reader: JsonReader?): Auth? {
        reader ?: throw JsonDataException("json parse error")
        return readAuth(reader)
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Auth?) {
        writer.value(value.toString())
    }
}