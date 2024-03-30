package com.newsnexus.client.view.activity.LoadScreen

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.newsnexus.client.R
import com.newsnexus.client.databinding.ActivityLoadScreenBinding
import com.newsnexus.client.model.Constants.PREFERENCES.Companion.APP_PREFERENCES
import com.newsnexus.client.model.Constants.PREFERENCES.Companion.TOKEN_KEY
import com.newsnexus.client.model.Constants.PREFERENCES.Companion.USERNAME_KEY
import com.newsnexus.client.view.activity.Home.HomeActivity
import com.newsnexus.client.view.activity.Login.LoginActivity

class LoadScreenActivity : Activity() {
    private lateinit var binding: ActivityLoadScreenBinding
    private val TAG = LoadScreenActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{false}
        actionBar?.hide()
        setUpView()
    }

    private fun setUpView(){
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
//            startActivity(LoginActivity.newIntent(this@LoadScreenActivity))
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//            finish()
            val appPreferences = this@LoadScreenActivity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            val retrievedToken = appPreferences.getString(TOKEN_KEY, null)
            val retrievedUsername = appPreferences.getString(USERNAME_KEY, null)

            if(retrievedToken.isNullOrEmpty() || retrievedUsername.isNullOrEmpty()){
                startActivity(LoginActivity.newIntent(this@LoadScreenActivity))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            }else{
                startActivity(HomeActivity.newIntent(this@LoadScreenActivity))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            }

        }, 4000)
    }
}