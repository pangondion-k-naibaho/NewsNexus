package com.newsnexus.client.view.activity.Home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.newsnexus.client.R
import com.newsnexus.client.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private val TAG = HomeActivity::class.java
    private lateinit var binding: ActivityHomeBinding

    companion object{
        fun newIntent(context: Context):Intent = Intent(context, HomeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}