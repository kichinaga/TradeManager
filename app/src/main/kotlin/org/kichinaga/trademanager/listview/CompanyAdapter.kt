package org.kichinaga.trademanager.listview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import kotlinx.android.synthetic.main.listview_company.view.*
import org.kichinaga.trademanager.R
import org.kichinaga.trademanager.api.ApiCaller
import org.kichinaga.trademanager.api.adapter.CreateStockListAdapter
import org.kichinaga.trademanager.extensions.getAccessToken
import org.kichinaga.trademanager.extensions.showToast
import org.kichinaga.trademanager.model.*

/**
 * Created by kichinaga on 2017/12/11.
 */
class CompanyAdapter(val context: Context, val  list: MutableList<Company>): RecyclerView.Adapter<CompanyAdapter.ViewHolder>() {
    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder =
            ViewHolder(inflater.inflate(R.layout.listview_company, parent, false))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val realm = Realm.getDefaultInstance()
        val market = realm.where(Market::class.java).equalTo("id", item.market_id).findFirst()!!
        val industry = realm.where(Industry::class.java).equalTo("id", item.industry_id).findFirst()!!

        holder.apply {
            companyName.text = "${item.name} ( ${item.stock_code} )"
            marketName.text = market.name
            industryName.text = industry.name
            companyLayout.setOnClickListener {
                if (realm.where(StockList::class.java).equalTo("stock_code", item.stock_code).findFirst() != null) {
                    context.showToast("すでに登録済です。")
                } else {
                    val caller = ApiCaller(CreateStockListAdapter())
                    caller.client.addStockLists(getAccessToken(context), item.id)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ stocklist ->
                                realm.executeTransaction {
                                    it.copyToRealmOrUpdate(stocklist)
                                }
                            },{
                                it.printStackTrace()
                                Handler(context.mainLooper).post { context.showToast("登録に失敗しました") }
                            },{
                                Handler(context.mainLooper).post { context.showToast("登録しました。") }
                            })
                }
            }
        }

        realm.close()
    }

    override fun getItemCount(): Int = list.size

    fun addItem(items: List<Company>) {
        items.forEach { list.add(it) }
        notifyDataSetChanged()
    }

    fun setList(lists: List<Company>){
        list.apply {
            clear()
            addAll(lists)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(itemview: View): RecyclerView.ViewHolder(itemview) {
        val companyLayout : ConstraintLayout = itemview.company_layout
        val companyName:TextView = itemview.company_name
        val marketName: TextView = itemview.market_name
        val industryName: TextView = itemview.industry_name
    }

}