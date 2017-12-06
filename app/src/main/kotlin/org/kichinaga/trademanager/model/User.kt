package org.kichinaga.trademanager.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by kichinaga on 2017/12/05.
 */

open class User(@PrimaryKey open var id: Int = 0,
                open var name: String = "",
                open var email: String = "",
                open var stock_lists: RealmList<StockList> = RealmList()): RealmObject()