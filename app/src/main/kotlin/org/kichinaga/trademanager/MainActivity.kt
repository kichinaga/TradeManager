package org.kichinaga.trademanager

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import org.kichinaga.trademanager.extensions.PrefKeys
import org.kichinaga.trademanager.extensions.setAccessToken
import org.kichinaga.trademanager.extensions.setUserId
import org.kichinaga.trademanager.fragment.StockListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        navigation_header.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.company_search -> {
                    startActivity(Intent(this, CompaniesActivity::class.java))
                }
                R.id.logout -> {
                    setAccessToken(this, PrefKeys.ERROR_TOKEN.name)
                    setUserId(this, -1)
                    val realm = Realm.getDefaultInstance()
                    realm.deleteAll()
                    finishAndRemoveTask()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                R.id.settings -> {
                    //todo 設定画面
                }
            }
            return@setNavigationItemSelectedListener false
        }

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.stock -> {
                    replaceFragment(StockListFragment())
                    true
                }
                R.id.fx -> {
                    true
                }
                R.id.bitcoin -> {
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        replaceFragment(StockListFragment())
    }

    

    @SuppressLint("CommitTransaction")
    private fun replaceFragment(item: Fragment){
        when(item){
            is StockListFragment -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.container, item)
                    commit()
                }
            }
        }
    }
}
