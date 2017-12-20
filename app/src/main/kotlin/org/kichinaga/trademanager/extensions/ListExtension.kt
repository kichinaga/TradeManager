package org.kichinaga.trademanager.extensions

/**
 * Created by kichinaga on 2017/12/13.
 */

fun <T> getShapingList(list: List<T>, start: Int = 0, limit: Int = 20): List<T> {

    return list.run {
        if (size != 0) {
            if (size - start > limit) slice(IntRange(start, limit -1)).toMutableList()
            else slice(IntRange(start, size - 1)).toMutableList()
        }

        this
    }
}
