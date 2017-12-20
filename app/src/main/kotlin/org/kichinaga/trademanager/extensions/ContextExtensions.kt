package org.kichinaga.trademanager.extensions

import android.content.Context
import android.os.Handler
import android.support.annotation.StringRes
import android.widget.Toast

/**
 * Created by kichinaga on 2017/12/06.
 */

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) =
        Handler(mainLooper).post { Toast.makeText(this, text, duration).show() }

fun Context.showToast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) =
        Handler(mainLooper).post { Toast.makeText(this, resId, duration).show() }