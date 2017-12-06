package org.kichinaga.trademanager.model

import io.realm.RealmObject

/**
 * Created by kichinaga on 2017/10/12.
 */

open class Market(open var id: Int = 0,
                  open var name: String = ""): RealmObject()