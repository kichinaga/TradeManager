package org.kichinaga.trademanager.model

import io.realm.RealmObject

/**
 * Created by kichinaga on 2017/10/12.
 */

open class Company(open var id: Int = 0,
                   open var stock_code: Int = 0,
                   open var name: String = "",
                   open var market: Market? = Market(),
                   open var industry: Industry? = Industry()): RealmObject()