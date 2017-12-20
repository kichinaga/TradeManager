package org.kichinaga.trademanager.listview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.listview_stocklist.view.*
import org.kichinaga.trademanager.R
import org.kichinaga.trademanager.StockActivity
import org.kichinaga.trademanager.model.StockList

/**
 * Created by kichinaga on 2017/12/07.
 */
class StockListAdapter(val context: Context,allList: List<StockList>): BaseAdapter() {

    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val list = allList.toMutableList()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val contentView: View = convertView ?: inflater.inflate(R.layout.listview_stocklist, parent, false)

        val result = list[position]

        contentView.company_name.text = result.company!!.name
        contentView.having_stocks_data.text = result.stock_total!!.amount.toString()
        contentView.stocks_profit_data.text = result.stock_total!!.price.toString()
        contentView.stocklist_layout.setOnClickListener {
            val intent = Intent(context, StockActivity::class.java)
            intent.putExtra("stock_code", list[position].stock_code)

            context.startActivity(intent)
        }

        return contentView
    }

    override fun getItem(position: Int): StockList = list[position]

    override fun getItemId(position: Int): Long = list[position].id.toLong()

    override fun getCount(): Int = list.size

    fun addItem(items: List<StockList>) {
        items.forEach { list.add(it) }
        notifyDataSetChanged()
    }
}