package org.kichinaga.trademanager.extensions

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by kichinaga on 2017/12/05.
 *
 * SharedPreference Extensions
 */


enum class PrefKeys {
    ACCESS_TOKEN,
    ERROR_TOKEN,
    USER_ID
}

fun isLoggedIn(context: Context): Boolean = (getAccessToken(context.applicationContext) != PrefKeys.ERROR_TOKEN.name) && (getUserId(context) > 0)

fun getAccessToken(context: Context): String =
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext).getString(PrefKeys.ACCESS_TOKEN.name, PrefKeys.ERROR_TOKEN.name)

fun setAccessToken(context: Context, access_token: String) =
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext).edit().putString(PrefKeys.ACCESS_TOKEN.name, access_token).apply()

fun getUserId(context: Context): Int =
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext).getInt(PrefKeys.USER_ID.name, -1)
fun setUserId(context: Context, user_id: Int) =
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext).edit().putInt(PrefKeys.USER_ID.name, user_id).apply()