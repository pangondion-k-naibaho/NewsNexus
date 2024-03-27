package com.newsnexus.client.view.activity.About

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.newsnexus.client.R
import com.newsnexus.client.databinding.ActivityAboutBinding
import com.newsnexus.client.model.Constants
import com.newsnexus.client.model.Constants.PREFERENCES.Companion.APP_PREFERENCES

class AboutActivity : AppCompatActivity() {
    private val TAG = AboutActivity::class.java.simpleName
    private lateinit var binding: ActivityAboutBinding
    private lateinit var appPreferences: SharedPreferences

    companion object{
        fun newIntent(context: Context): Intent = Intent(context, AboutActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarAbout)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_socialist)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        appPreferences = this@AboutActivity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val retrievedUsername = appPreferences.getString(Constants.PREFERENCES.USERNAME_KEY, null)

        binding.apply {
            tvUsername.text = retrievedUsername
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}