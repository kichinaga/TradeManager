package org.kichinaga.trademanager

import android.animation.Animator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_stock.*
import org.kichinaga.trademanager.model.StockList
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_buystock.view.*
import org.kichinaga.trademanager.api.ApiCaller
import org.kichinaga.trademanager.api.service.StockService
import org.kichinaga.trademanager.extensions.getAccessToken
import org.kichinaga.trademanager.extensions.showToast
import org.kichinaga.trademanager.listview.StockAdapter


/**
 * Created by kichinaga on 2017/12/14.
 */
class StockActivity: AppCompatActivity() {
    private val realm = Realm.getDefaultInstance()
    private val service = StockService()
    private var stockCode: Int = 0
    private lateinit var stocklist: StockList
    private lateinit var adapter: StockAdapter
    private var state: ButtonState = ButtonState.CLOSE

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock)
        stockCode = intent.getIntExtra("stock_code", 0)
        stocklist = realm.where(StockList::class.java).equalTo("stock_code", stockCode).findFirst()!!
        setTableLayout()

        showProgress(true)
        service.get(this, stockCode)
                .subscribe { realmlist ->
                    realm.executeTransaction { it.copyToRealmOrUpdate(realmlist) }
                    adapter = StockAdapter(this, realmlist)
                    Handler(this.mainLooper).post {
                        stock_listview.adapter = adapter
                        showProgress(false)
                    }
                }

        // FABのメニューボタン
        fab_menu.setOnClickListener {
            if (state == ButtonState.CLOSE) fabOpen(convertDpToPx(56))
            else fabClose()
        }
        // FABの株購入ボタン
        fab_buy.setOnClickListener {
            showDialog(this.layoutInflater.inflate(R.layout.dialog_buystock, null), this)
            fabClose()
        }
        // FABの取引詳細画面への遷移ボタン
        fab_graph.setOnClickListener {
            val intent = Intent(this, TradeDetailActivity::class.java)
            intent.putExtra("stock_code", stockCode)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun setTableLayout(){
        val company = stocklist.company
        val detail = company?.stock_detail!!

        company_name.text = company.name
        price.text = detail.price
        change.text = detail.change
        prev_close.text = detail.prev_close_price
        open.text = detail.open_price
        high.text = detail.high_price
        low.text = detail.low_price
        volume.text = detail.volume
        total_trade.text = detail.total_trade
    }

    private fun showProgress(show: Boolean = false) {
        stock_progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showDialog(view: View, context: Context) {
        val company = stocklist.company!!
        val detail = company.stock_detail!!
        val text = detail.price.replace("""\(.+?\)""".toRegex(), "").replace(",", "").replace(" ", "")
        view.stock_buy_price.setText(text)

        AlertDialog.Builder(this).apply {
            title = "株を購入"
            setView(view)
            setPositiveButton("購入", { _, _ ->
                showProgress(true)
                val action = when(view.stock_group.checkedRadioButtonId){
                    R.id.stock_buy_action -> "buy"
                    R.id.stock_sell_action -> "sell"
                    else -> "error"
                }
                val price = view.stock_buy_price.text.toString().toInt()
                val amount = view.stock_buy_amount.text.toString().toInt()
                val id = stocklist.id

                // 購入処理
                ApiCaller.getDefaultApiClient().createStocks(getAccessToken(context), amount, price, action, id)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            // onSuccess
                            if(it.flag){
                                context.apply {
                                    Handler(mainLooper).post {
                                        showToast("株を購入しました。")
                                        showProgress(false)
                                    }
                                }
                            }

                            fetchHistory()
                        },{
                            // onError
                            it.printStackTrace()
                            context.apply { Handler(mainLooper).post {
                                showToast("購入できませんでした。\n ${it.message}")
                                showProgress(false)
                            } }
                        })
            })
            setNegativeButton("キャンセル", null)
            show()
        }
    }

    private fun convertDpToPx(dp: Int): Float = dp * this.resources.displayMetrics.density

    private fun fabOpen(iconWhile: Float){
        fab_buy_layout.visibility = View.VISIBLE
        ObjectAnimator.ofFloat(fab_buy_layout, "translationY", -iconWhile * 1.5f).apply {
            duration = 200
        }.start()

        fab_graph_layout.visibility = View.VISIBLE
        ObjectAnimator.ofFloat(fab_graph_layout, "translationY", -iconWhile * 3).apply {
            duration = 200
        }.start()

        ObjectAnimator.ofFloat(fab_menu, "rotation", 45f).apply {
            duration = 200
        }.start()

        state = ButtonState.OPEN
        fab_background.visibility = View.VISIBLE
    }

    private fun fabClose(){

        ObjectAnimator.ofFloat(fab_buy_layout, "translationY", 0f).apply {
            duration = 200
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) { fab_buy_layout.visibility = View.GONE }
            })
        }.start()


        ObjectAnimator.ofFloat(fab_graph_layout, "translationY", 0f).apply {
            duration = 200
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) { fab_graph_layout.visibility = View.GONE }
            })
        }.start()

        ObjectAnimator.ofFloat(fab_menu, "rotation", 0f).apply {
            duration = 200
        }.start()

        state = ButtonState.CLOSE
        fab_background.visibility = View.GONE
    }

    private fun fetchHistory(){
        showProgress(true)
        service.fetch(this, stockCode)
                .subscribe { realmlist ->
                    realm.executeTransaction { it.copyToRealmOrUpdate(realmlist) }
                    Handler(this.mainLooper).post {
                        adapter.setList(realmlist.toList())
                        showProgress(false)
                    }
                }
    }

    enum class ButtonState {
        OPEN, CLOSE
    }
}