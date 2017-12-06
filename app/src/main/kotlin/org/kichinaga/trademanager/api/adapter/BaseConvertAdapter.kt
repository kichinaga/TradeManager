package org.kichinaga.trademanager.api.adapter

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
        val stockLists: RealmList<StockList> = RealmList()

        reader.beginObject()
        while (reader.hasNext()){
            when(reader.nextName()){
                "id" -> id = reader.nextInt()
                "name" -> name = reader.nextString()
                "email" -> email = reader.nextString()
                "stock_lists" -> {
                    reader.beginArray()
                    while (reader.hasNext()){
                        stockLists.add(readStockLists(reader))
                    }
                    reader.endArray()
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return User(id, name, email, stockLists)
    }

    fun readStockLists(reader: JsonReader): StockList {
        var id = 0
        var activated = false
        var company = Company()
        val stocks: RealmList<Stock> = RealmList()
        var stock_total = StockTotal()

        reader.beginObject()
        while (reader.hasNext()){
            when(reader.nextName()){
                "id" -> id = reader.nextInt()
                "activated" -> activated = reader.nextBoolean()
                "company" -> company = readCompany(reader)
                "stocks" -> {
                    reader.beginArray()
                    while (reader.hasNext()){
                        stocks.add(readStocks(reader))
                    }
                    reader.endArray()
                }
                "stock_total" -> stock_total = readStockTotal(reader)
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return StockList(id, activated, company, stocks, stock_total)
    }

    fun readCompany(reader: JsonReader): Company {
        var id = 0
        var stock_code = 0
        var name = ""
        var market = Market()
        var industry = Industry()

        reader.beginObject()
        while (reader.hasNext()){
            when(reader.nextName()){
                "id" -> id = reader.nextInt()
                "stock_code" -> stock_code = reader.nextInt()
                "name" -> name = reader.nextString()
                "market" -> market = readMarket(reader)
                "industry" -> industry = readIndustry(reader)
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return Company(id, stock_code, name, market, industry)
    }

    fun readMarket(reader: JsonReader): Market {
        var id = 0
        var name = ""

        reader.beginObject()
        while (reader.hasNext()){
            when(reader.nextName()){
                "id" -> id = reader.nextInt()
                "name" -> name = reader.nextString()
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return Market(id, name)
    }

    fun readIndustry(reader: JsonReader): Industry {
        var id = 0
        var name = ""

        reader.beginObject()
        while (reader.hasNext()){
            when(reader.nextName()){
                "id" -> id = reader.nextInt()
                "name" -> name = reader.nextString()
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return Industry(id, name)
    }

    fun readStocks(reader: JsonReader): Stock {
        var price = 0
        var num = 0
        var action = ""
        var created_at = Date()

        reader.beginObject()
        while (reader.hasNext()){
            when(reader.nextName()){
                "price" -> price = reader.nextInt()
                "num" -> num = reader.nextInt()
                "action" -> action = reader.nextString()
                "created_at" -> created_at = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.JAPAN).parse(reader.nextString())
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return Stock(price,num, action, created_at)
    }

    fun readStockTotal(reader: JsonReader): StockTotal {
        var price = 0
        var amount = 0
        var updated_at = Date()

        reader.beginObject()
        while (reader.hasNext()){
            when(reader.nextName()){
                "price" -> price = reader.nextInt()
                "amount" -> amount = reader.nextInt()
                "updated_at" -> updated_at = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.JAPAN).parse(reader.nextString())
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return StockTotal(price, amount, updated_at)
    }
}