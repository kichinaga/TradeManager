package org.kichinaga.trademanager

import android.app.Application
import io.realm.Realm

/**
 * Created by kichinaga on 2017/12/05.
 */
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}