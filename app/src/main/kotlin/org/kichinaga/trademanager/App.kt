package org.kichinaga.trademanager

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import org.kichinaga.trademanager.model.*

/**
 * Created by kichinaga on 2017/12/05.
 */
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        val config = RealmConfiguration.Builder()
                .name("data.realm")
                .initialData {
                    it.let {
                        this.let { source ->
                            it.createAllFromJson(Company::class.java, source.resources.assets.open("companies.json"))
                            it.createAllFromJson(Market::class.java, source.resources.assets.open("markets.json"))
                            it.createAllFromJson(Industry::class.java, source.resources.assets.open("industries.json"))
                        }
                    }
                }
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
    }
}