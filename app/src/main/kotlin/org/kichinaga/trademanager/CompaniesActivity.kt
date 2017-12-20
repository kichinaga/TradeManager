package org.kichinaga.trademanager

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import io.realm.Realm
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_company_list.*
import org.kichinaga.trademanager.api.service.CompaniesService
import org.kichinaga.trademanager.extensions.getShapingList
import org.kichinaga.trademanager.listview.CompanyAdapter
import org.kichinaga.trademanager.listview.EndlessScrollListener
import org.kichinaga.trademanager.model.Company
import org.kichinaga.trademanager.model.Industry
import org.kichinaga.trademanager.model.Market

/**
 * Created by kichinaga on 2017/10/12.
 */
class CompaniesActivity : AppCompatActivity() {
    /**
     * DB
     */
    private val realm = Realm.getDefaultInstance()
    /**
     * 企業データを取得するためのサービス
     */
    private val service = CompaniesService()
    /**
     * 表示するリスト
     */
    private val allList = realm.where(Company::class.java).findAllSorted("stock_code", Sort.ASCENDING).toMutableList()
    /**
     * 表示する件数
     */
    private val listLimit = 20
    /**
     * listviewのアダプタ
     */
    private lateinit var listAdapter: CompanyAdapter
    /**
     * 検索条件
     */
    var market_id = ""
    var industry_id = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_list)
        setSupportActionBar(toolbar_company)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        showProgress(false)

        // recyclerview
        listAdapter = CompanyAdapter(this, allList.slice(IntRange(0, listLimit - 1)).toMutableList())
        company_list.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            this.adapter = listAdapter
            addOnScrollListener(object : EndlessScrollListener(linearLayoutManager = company_list.layoutManager as LinearLayoutManager){
                override fun onLoadMore(totalItemCount: Int) {
                    if (allList.size > totalItemCount + listLimit){
                        Handler(mainLooper).post { listAdapter.addItem(getShapingList(allList, totalItemCount, totalItemCount + listLimit)) }
                    }
                }
            })
        }

        // spinner
        search_market.apply {
            adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, realm.where(Market::class.java).findAllSorted("id", Sort.ASCENDING).map { it.name })
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) { market_id = (position + 1).toString() }
                override fun onNothingSelected(parent: AdapterView<*>?) { market_id = "" }
            }
        }
        search_industry.apply {
            adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, realm.where(Industry::class.java).findAllSorted("id", Sort.ASCENDING).map { it.name })
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) { industry_id = (position + 1).toString() }
                override fun onNothingSelected(parent: AdapterView<*>?) { industry_id = "" }
            }
        }

        // EditText
        fab_company.setOnClickListener {
            val company_name = search_company.text.toString()
            val stock_code = search_stockcode.text.toString()
            search_company.text.clear()
            search_stockcode.text.clear()

            showProgress(true)

            service.fetch(stock_code, company_name, market_id, industry_id)
                    .subscribe {
                        allList.apply {
                            clear()
                            addAll(it)
                        }
                        val list = getShapingList(allList)
                        Handler(mainLooper).post {
                            listAdapter.setList(list.toList())
                            showProgress(false)
                        }
                    }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun showProgress(show: Boolean = false) {
        company_progress.visibility = if (show) View.VISIBLE else View.GONE
    }

}