package org.kichinaga.trademanager.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by kichinaga on 2017/10/12.
 */

open class Industry(@PrimaryKey open var id: Int = 0,
                    open var name: String = ""): RealmObject()