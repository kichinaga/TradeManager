package org.kichinaga.trademanager.api.adapter

import android.annotation.SuppressLint
import com.squareup.moshi.JsonReader
import io.realm.RealmList
import org.kichinaga.trademanager.model.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kichinaga on 2017/12/06.
 */
interface BaseConvertAdapter {

    fun readAuth(reader: JsonReader): Auth {
        var token = ""
        var user = User()

        reader.beginObject()
        while (reader.hasNext()){
            when(reader.nextName()){
                "token" -> token = reader.nextString()
                "current_user" -> user = readUser(reader)
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return Auth(token, user)
    }

    fun readUser(reader: JsonReader): User {
        var id = 0
        var name = ""
        var email = ""

        reader.beginObject()
        while (reader.hasNext()){
            when(reader.nextName()){
                "id" -> id = reader.nextInt()
                "name" -> name = reader.nextString()
                "email" -> email = reader.nextString()
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return User(id, name, email)
    }

    fun readStockLists(reader: JsonReader): StockList {
        var id = 0
        var activated = false
        var stock_code = 0
        var company = Company()
        var stock_total = StockTotal()

        reader.beginObject()
        while (reader.hasNext()){
            when(reader.nextName()){
                "id" -> id = reader.nextInt()
                "activated" -> activated = reader.nextBoolean()
                "stock_code" -> stock_code = reader.nextInt()
                "company" -> company = readCompany(reader)
                "stock_total" -> stock_total = readStockTotal(reader)
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return StockList(id, activated, stock_code, company, stock_total)
    }

    fun readCompany(reader: JsonReader): Company {
        var id = 0
        var stock_code = 0
        var name = ""
        var market = 0
        var industry = 0
        var stock_detail = StockDetail()

        reader.beginObject()
        while (reader.hasNext()){
            when(reader.nextName()){
                "id" -> id = reader.nextInt()
                "stock_code" -> stock_code = reader.nextInt()
                "name" -> name = reader.nextString()
                "market_id" -> market = reader.nextInt()
                "industry_id" -> industry = reader.nextInt()
                "stock_detail" -> stock_detail = readStockDetail(reader)
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return Company(id, stock_code, name, market, industry, stock_detail)
    }

    fun readSearchCompany(reader: JsonReader): Company {
        var id = 0
        var stock_code = 0
        var name = ""
        var market = 0
        var industry = 0

        reader.beginObject()
        while (reader.hasNext()){
            when(reader.nextName()){
                "id" -> id = reader.nextInt()
                "stock_code" -> stock_code = reader.nextInt()
                "name" -> name = reader.nextString()
                "market_id" -> market = reader.nextInt()
                "industry_id" -> industry = reader.nextInt()
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return Company(id, stock_code, name, market, industry, StockDetail())
    }

    fun readStocks(reader: JsonReader): Stock {
        var price = 0
        var num = 0
        var action = ""
        var created_at = Date()
        val pattern = "yyyy-MM-dd"

        reader.beginObject()
        while (reader.hasNext()){
            when(reader.nextName()){
                "price" -> price = reader.nextInt()
                "num" -> num = reader.nextInt()
                "action" -> action = reader.nextString()
                "created_at" -> created_at = SimpleDateFormat(pattern, Locale.JAPAN).parse(reader.nextString())
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return Stock(price,num, action, created_at)
    }

    @SuppressLint("SimpleDateFormat")
    fun readStockTotal(reader: JsonReader): StockTotal {
        var id = 0
        var price = 0
        var amount = 0
        var updated_at = Date()
        // val pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        val pattern = "yyyy-MM-dd"

        reader.beginObject()
        while (reader.hasNext()){
            when(reader.nextName()){
                "id" -> id = reader.nextInt()
                "price" -> price = reader.nextInt()
                "amount" -> amount = reader.nextInt()
                "updated_at" -> updated_at = SimpleDateFormat(pattern, Locale.JAPAN).parse(reader.nextString())
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return StockTotal(id, price, amount, updated_at)
    }

    fun readStockDetail(reader: JsonReader): StockDetail{
        var id = 0
        var price = ""
        var change = ""
        var prev = ""
        var open = ""
        var high = ""
        var low = ""
        var vol = ""
        var trade = ""

        reader.beginObject()
        while (reader.hasNext()){
            when(reader.nextName()){
                "id" -> id = reader.nextInt()
                "price" -> price = reader.nextString()
                "change" -> change = reader.nextString()
                "prev_close_price" -> prev = reader.nextString()
                "open_price" -> open = reader.nextString()
                "high_price" -> high = reader.nextString()
                "low_price" -> low = reader.nextString()
                "volume" -> vol = reader.nextString()
                "total_trade" -> trade = reader.nextString()
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return StockDetail(id, price, change, prev, open, high, low, vol, trade)
    }
}