package org.kichinaga.trademanager.api

import android.net.Uri
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import java.io.IOException
import java.lang.reflect.Type

/**
 *
 * Created by kichinaga on 2017/10/12.
 * プリミティブ以外の型をPOJOに含めるためのカスタムアダプタ。
 *
 */
class UriAdapter : JsonAdapter<Uri>() {

    companion object {
        // Moshiのソースコードではアダプタ毎にファクトリを用意していたのでそれに習う。
        val FACTORY: Factory = object: Factory {
            override fun create(type: Type, annotations: Set<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
                if (type === Uri::class.java) {
                    return UriAdapter()
                }
                return null
            }
        }
    }

    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): Uri = Uri.parse(reader.nextString())

    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: Uri?) {
        writer.value(value.toString())
    }

    override fun toString(): String = "JsonAdapter(Uri)"
}