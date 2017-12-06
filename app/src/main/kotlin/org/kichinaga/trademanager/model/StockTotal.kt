package org.kichinaga.trademanager.model

import io.realm.RealmObject
import java.util.*

/**
 * Created by kichinaga on 2017/12/05.
 */

open class StockTotal(open var price: Int = 0,
                      open var amount: Int = 0,
                      open var updated_at: Date = Date()): RealmObject()