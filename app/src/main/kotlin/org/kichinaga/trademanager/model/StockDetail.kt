package org.kichinaga.trademanager.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by kichinaga on 2017/12/11.
 */
open class StockDetail(@PrimaryKey open var id: Int = 0,
                       open var price: String = "",
                       open var change: String = "",
                       open var prev_close_price: String = "",
                       open var open_price: String = "",
                       open var high_price: String = "",
                       open var low_price: String = "",
                       open var volume: String = "",
                       open var total_trade: String = ""): RealmObject()