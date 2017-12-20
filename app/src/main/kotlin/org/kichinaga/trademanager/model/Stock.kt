package org.kichinaga.trademanager.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Created by kichinaga on 2017/12/05.
 */

open class Stock(@PrimaryKey open var id: Int = 0,
                 open var stock_code: Int = 0,
                 open var price: Int = 0,
                 open var num: Int = 0,
                 open var action: String = "",
                 open var created_at: Date = Date()): RealmObject()