package org.kichinaga.trademanager.model

/**
 * Created by kichinaga on 2017/10/12.
 */
data class Company(val id: Int,
                   val stock_code: Int,
                   val name: String,
                   val market: Market,
                   val industry: Industry)