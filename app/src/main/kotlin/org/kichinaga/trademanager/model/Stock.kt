package org.kichinaga.trademanager.model

import io.realm.RealmObject
import java.util.*

/**
 * Created by kichinaga on 2017/12/05.
 */

open class Stock(open var price: Int = 0,
                 open var num: Int = 0,
                 open var action: String = "",
                 open var created_at: Date = Date()): RealmObject()