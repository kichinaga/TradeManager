package org.kichinaga.trademanager.listview

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.listview_stock.view.*
import org.kichinaga.trademanager.R
import org.kichinaga.trademanager.model.Stock

/**
 * Created by kichinaga on 2017/12/18.
 */
class StockAdapter(val context: Context, allList: List<Stock>): BaseAdapter() {
    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val list = allList.toMutableList()

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val contentView: View = convertView ?: inflater.inflate(R.layout.listview_stock, parent, false)

        val item = list[position]
        // 背景色の変更
        if ("buy" == item.action) contentView.stock_constrant.background = context.getDrawable(R.color.buy)
        else contentView.stock_constrant.background = context.getDrawable(R.color.sell)

        contentView.stock_action.text = item.action
        val action = if("buy" == item.action)  "購入" else "売却"

        contentView.stock_price.text = "${action}価格 : ${item.price} 円"
        contentView.stock_amount.text = "${action}株数 : ${item.num} 株"
        contentView.stock_created_at.text = "${action}日 : ${item.created_at}"

        return contentView
    }

    override fun getItem(position: Int): Stock = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = list.size

    fun setList(lists: List<Stock>){
        list.apply {
            clear()
            addAll(lists)
        }
        notifyDataSetChanged()
    }
}