package org.kichinaga.trademanager.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by kichinaga on 2017/10/12.
 */

open class Company(open var id: Int = 0,
                   @PrimaryKey open var stock_code: Int = 0,
                   open var name: String = "",
                   open var market_id: Int = 0,
                   open var industry_id: Int = 0,
                   open var stock_detail: StockDetail? = StockDetail()): RealmObject()