package org.kichinaga.trademanager

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import io.realm.Realm
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_trade_detail.*
import org.kichinaga.trademanager.model.Stock
import org.kichinaga.trademanager.model.StockList
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kichinaga on 2017/12/20.
 */
class TradeDetailActivity : AppCompatActivity() {
    private val realm = Realm.getDefaultInstance()
    private var stockCode = 0
    private val labelList = mutableListOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade_detail)
        stockCode = intent.getIntExtra("stock_code", 0)


        makeLineChart()
        setStockListTotal()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun makeLineChart(){
        fun makeChartDataSet():LineDataSet {
            fun makeChartData(stocks: List<Stock>): MutableList<Int> {
                //日付の重複を無くして日付のみをリストに保存
                val list = mutableListOf(0)
                val dates = mutableListOf<Date>()
                val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN)
                stocks.map{ sdf.parse(sdf.format(it.created_at)) }.distinct().forEach{
                    dates.add(it)
                    labelList.add(sdf.format(it))
                }

                var value = 0
                dates.forEach { date ->
                    stocks.forEach {
                        if (sdf.parse(sdf.format(it.created_at)).compareTo(date) == 0) {
                            if ("buy" == it.action) value -= it.price * it.num
                            else if ("sell" == it.action) value += it.price * it.num
                        }
                    }
                    list.add(value)
                }

                return list
            }

            val dataList = mutableListOf<Entry>()
            makeChartData(
                    realm.where(Stock::class.java)
                            .equalTo("stock_code", stockCode)
                            .findAllSorted("created_at", Sort.ASCENDING)
                            .toList()
            ).forEachIndexed { index, value -> dataList.add(Entry(index.toFloat(), value.toFloat())) }

            val dataSet = LineDataSet(dataList, "trade")

            dataSet.color = Color.BLUE
            dataSet.setDrawValues(true)
            dataSet.valueTextSize = 12f
            dataSet.isHighlightEnabled = true
            return dataSet
        }

        stock_line_chart.data = LineData(makeChartDataSet())

        stock_line_chart.apply {
            // chart全体の設定
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            isDoubleTapToZoomEnabled = false
            setBackgroundColor(Color.LTGRAY)
            isAutoScaleMinMaxEnabled = true
            description = null

            // Y軸の間隔
            axisLeft.isGranularityEnabled = true

            // chartのY軸右の設定
            axisRight.isEnabled = false

            // chartのX軸
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawLabels(true)
            xAxis.granularity = 1f
            xAxis.valueFormatter = IAxisValueFormatter { value, _ ->
                val index = value.toInt()
                return@IAxisValueFormatter if(index > 0 && labelList.size > index -1) labelList[index] else ""
            }

            invalidate()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setStockListTotal(){
        val stocklist = realm.where(StockList::class.java).equalTo("stock_code", stockCode).findFirst()!!
        val total = stocklist.stock_total!!

        trade_price.text = "利益 : ${total.price} 円"
        trade_amount.text = "保有数 : ${total.amount} 株"
        trade_updated_at.text = "最終更新日 : ${SimpleDateFormat("yyyy年MM月dd日", Locale.JAPAN).format(total.updated_at)}"

    }
}