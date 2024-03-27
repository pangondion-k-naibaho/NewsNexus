package com.newsnexus.client.view.activity.Login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.newsnexus.client.R
import com.newsnexus.client.databinding.ActivityLoginBinding
import com.newsnexus.client.model.Constants.DUMMY_DATA.Companion.getListDummyUser
import com.newsnexus.client.model.Constants.PREFERENCES.Companion.APP_PREFERENCES
import com.newsnexus.client.model.Constants.PREFERENCES.Companion.TOKEN_KEY
import com.newsnexus.client.model.Constants.PREFERENCES.Companion.USERNAME_KEY
import com.newsnexus.client.model.Constants.STATUS.Companion.STATUS_SUCCESS
import com.newsnexus.client.model.dataclass.request.LoginRequest
import com.newsnexus.client.model.dataclass.response.login.LoginResponse
import com.newsnexus.client.view.activity.Home.HomeActivity
import com.newsnexus.client.view.advanced_ui.InputTextView
import com.newsnexus.client.viewmodel.LoginViewModel
import com.newssphere.client.view.advanced_ui.PopUpNotificationListener
import com.newssphere.client.view.advanced_ui.showPopupNotification

class LoginActivity : AppCompatActivity() {
    private val TAG = LoginActivity::class.java.simpleName
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    companion object{
        fun newIntent(context: Context): Intent = Intent(context, LoginActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.hide()

        loginViewModel = ViewModelProvider(this@LoginActivity).get(LoginViewModel::class.java)
        registerDummyUser()
        initView()
    }

    private fun registerDummyUser(){
        loginViewModel.registerListUser(getListDummyUser())
    }

    private fun initView(){
        binding.itvEmail.apply {
            setInputType(InputTextView.INPUT_TYPE.EMAIL)
            Log.d(TAG, "inputType itvEmail: ${binding.itvEmail.getInputType()}")
            setTitle(getString(R.string.tvTitle_Email))
            setTextHelper(getString(R.string.tvHint_Email))
            setListener(null)
        }

        binding.itvPassword.apply {
            setInputType(InputTextView.INPUT_TYPE.PASSWORD)
            Log.d(TAG, "inputType itvPassword: ${binding.itvPassword.getInputType()}")
            setTitle(getString(R.string.tvTitle_Password))
            setListener(object: InputTextView.InputViewListener{
                override fun onClickReveal() {
                    Log.d(TAG, "onClickReveal")
                    revealPassword()
                }
            })
        }

        binding.btnLogin.setOnClickListener {
            val retrievedEmail = binding.itvEmail.getText()
            val retrievedPassword = binding.itvPassword.getText()

            Log.d(TAG, "email: $retrievedEmail, password: $retrievedPassword")

            loginViewModel.loginUser(LoginRequest(retrievedEmail, retrievedPassword))
        }

        loginViewModel.isLoading.observe(this@LoginActivity, {
            setForLoading(it)
        })

        loginViewModel.isFail.observe(this@LoginActivity, {
            if (it) Log.e(TAG, "isFail: $it") else Log.d(TAG, "isFail: $it")
        })

        loginViewModel.loginResponse.observe(this@LoginActivity, {userLoginResponse->
            setUpSuccessAction(userLoginResponse)
        })

    }

    private fun setForLoading(isLoading: Boolean){
        if(isLoading){
            binding.loadingLayout.visibility = View.VISIBLE
            binding.pbLoading.visibility = View.VISIBLE
        }else{
            binding.loadingLayout.visibility = View.GONE
            binding.pbLoading.visibility = View.GONE
        }
    }

    private fun setForPopUpDisplaying(isDisplaying: Boolean){
        if(isDisplaying){
            binding.loadingLayout.visibility = View.VISIBLE
            binding.pbLoading.visibility = View.GONE
        }else{
            binding.loadingLayout.visibility = View.GONE
            binding.pbLoading.visibility = View.GONE
        }
    }

    private fun setUpSuccessAction(loginResponse: LoginResponse){
        when(loginResponse.status){
            STATUS_SUCCESS ->{
                val appPreferences = this@LoginActivity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
                val editor = appPreferences.edit()
                editor.putString(TOKEN_KEY, loginResponse.token)
                editor.putString(USERNAME_KEY, loginResponse.userName)
                editor.apply()
                if(editor.commit()){
                    setForPopUpDisplaying(true)
                    this@LoginActivity.showPopupNotification(
                        textTitle = getString(R.string.tvPopupTitle_Success),
                        textDesc = getString(R.string.tvPopupDesc_LoginSuccess),
                        backgroundImage = R.drawable.ic_tick_circle,
                        object: PopUpNotificationListener{
                            override fun onClickListener() {
                                this@LoginActivity.closeOptionsMenu()
                                setForPopUpDisplaying(false)
                                startActivity(HomeActivity.newIntent(this@LoginActivity))
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                finish()
                            }

                        }
                    )
                }
            }
            else ->{
                Toast.makeText(this@LoginActivity, "Login Error", Toast.LENGTH_SHORT).show()
                binding.itvEmail.setOnError()
                binding.itvPassword.setOnError()
            }
        }
    }
}