package org.kichinaga.trademanager

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_signup.*
import org.kichinaga.trademanager.api.ApiCaller
import org.kichinaga.trademanager.extensions.showToast

/**
 * Created by kichinaga on 2017/12/06.
 */
class SignUpActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        showProgress(false)

        signup_password_confirmation.setOnEditorActionListener { v, actionId, event ->
            if(actionId == R.id.sign_up || actionId == EditorInfo.IME_NULL){
                attemptSignUp()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        signup_button.setOnClickListener { attemptSignUp() }
    }

    /**
     * 新規登録検証
     */
    private fun attemptSignUp(){
        if (checkSignUpData()){
            showProgress(true)
            val caller = ApiCaller.getDefaultApiClient()
            caller.signup(signup_name.text.toString(), signup_email.text.toString(), signup_password.text.toString(), signup_password_confirmation.text.toString())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.flag){
                            applicationContext.showToast(R.string.success_signup)
                            finish()
                        }
                    },{
                        showProgress(false)
                        Snackbar.make(signup_form, R.string.error_signup, Snackbar.LENGTH_LONG).show()
                        it.printStackTrace()
                    })
        } else {
            signup_name.error = getString(R.string.error_form_field)
        }
    }

    /**
     * 新規登録に必要なデータの整合性を検査
     */
    private fun checkSignUpData(): Boolean =
            !TextUtils.isEmpty(signup_name.text) &&
                    !TextUtils.isEmpty(signup_email.text) &&
                    !TextUtils.isEmpty(signup_password.text) && signup_password.text.length > 7 &&
                    TextUtils.equals(signup_password.text, signup_password_confirmation.text)


    /**
     * プログレスバーの表示、非表示を管理
     */
    private fun showProgress(show: Boolean = false) {
        signup_progress.visibility = if (show) View.VISIBLE else View.GONE
    }
}