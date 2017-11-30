package org.kichinaga.trademanager

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.content_list.*
import org.kichinaga.trademanager.api.CompaniesService
import org.kichinaga.trademanager.model.Company

/**
 * Created by kichinaga on 2017/10/12.
 */
class CompaniesActivity : AppCompatActivity() {
    private val service = CompaniesService()
    private val list = mutableListOf<Company>()
    private val name_list = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_list)

        service.get().subscribe({
            list.addAll(it)
            it.forEach { name_list.add(it.name) }
            setCompanies()
        })
    }

    private fun setCompanies(){
        container_list.adapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_list_item_1, name_list)
    }
}