package org.kichinaga.trademanager

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_login.*
import org.kichinaga.trademanager.api.ApiCaller
import org.kichinaga.trademanager.extensions.isLoggedIn
import org.kichinaga.trademanager.extensions.setAccessToken
import org.kichinaga.trademanager.extensions.setUserId

/**
 * Created by kichinaga on 2017/12/05.
 */
class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        showProgress(false)

        /* パスワード入力時のキーボードからの、Enter入力からでもログインできるように */
        login_password.setOnEditorActionListener({ _, actionId, _ ->
            if(actionId == R.id.log_in || actionId == EditorInfo.IME_NULL){
                attemptLogin()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        })

        login_button.setOnClickListener { attemptLogin() }
        register_text.setOnClickListener { /* startActivity(Intent(this, RegisterActivity::class.java)) */ }

        //ログインデータが残っていれば、ログインをスキップする
        if (savedInstanceState == null && isLoggedIn(applicationContext)) loginComplete()
    }

    /**
     * ログイン検証部
     */
    private fun attemptLogin(){
        if (checkLoginData()){
            //todo 非同期でサーバにリクエストを送る
            showProgress(true)
            val caller = ApiCaller()

            caller.client.login(login_email.text.toString(), login_password.text.toString())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        //todo トークンを手に入れ、保存し、ユーザーデータをrealmに格納
                        val realm = Realm.getDefaultInstance()
                        val auth = it
                        setAccessToken(applicationContext, auth.token)
                        setUserId(applicationContext, auth.current_user?.id!!)

                        realm.executeTransaction {
                            realm.copyToRealmOrUpdate(auth.current_user)
                        }

                        realm.close()
                    },{
                        //todo エラー処理
                        showProgress(false)
                        Snackbar.make(login_form, R.string.error_login, Snackbar.LENGTH_LONG).show()
                        it.printStackTrace()
                    },{
                        loginComplete()
                    })
        } else {
            //todo エラー文を設定
            login_email.error = getString(R.string.error_login_field)
        }
    }

    /**
     * ログインを行なった後の処理
     */
    private fun loginComplete(){
        showProgress(false)
        Toast.makeText(this, R.string.success_login, Toast.LENGTH_SHORT).show()
        finishAndRemoveTask()
        startActivity(Intent(this, MainActivity::class.java))
    }

    /**
     * email、passwordの内容を検証する処理
     */
    private fun checkLoginData(): Boolean =
            !TextUtils.isEmpty(login_email.text) && !TextUtils.isEmpty(login_password.text) && login_password.text.length > 7

    /**
     * プログレスバーの表示、非表示を管理
     */
    private fun showProgress(show: Boolean) {
        login_progress.visibility = if (show) View.VISIBLE else View.GONE
    }
}