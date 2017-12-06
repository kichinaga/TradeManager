package org.kichinaga.trademanager.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by kichinaga on 2017/12/05.
 */

open class StockList(@PrimaryKey open var id: Int = 0,
                     open var activated: Boolean = false,
                     open var company: Company? = Company(),
                     open var stocks: RealmList<Stock> = RealmList(),
                     open var stock_total: StockTotal? = StockTotal()): RealmObject()