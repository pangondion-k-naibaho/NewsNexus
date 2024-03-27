package com.newsnexus.client.view.activity.CritiqueSugesstions

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsnexus.client.R
import com.newsnexus.client.databinding.ActivityCritiqueSuggestionBinding
import com.newsnexus.client.model.Constants
import com.newsnexus.client.view.adapter.ItemCnSAdapter
import com.newsnexus.client.viewmodel.CritiqueSuggestionViewModel

class CritiqueSuggestionActivity : AppCompatActivity() {
    private val TAG = CritiqueSuggestionActivity::class.java.simpleName
    private lateinit var binding: ActivityCritiqueSuggestionBinding
    private lateinit var appPreferences: SharedPreferences
    private var retrievedUsername: String? = ""
    private lateinit var csViewModel: CritiqueSuggestionViewModel

    companion object{
        fun newIntent(context: Context): Intent = Intent(context, CritiqueSuggestionActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCritiqueSuggestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCnS)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_socialist)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        appPreferences = this@CritiqueSuggestionActivity.getSharedPreferences(Constants.PREFERENCES.APP_PREFERENCES, Context.MODE_PRIVATE)
        retrievedUsername = appPreferences.getString(Constants.PREFERENCES.USERNAME_KEY, null)

        csViewModel = ViewModelProvider(this@CritiqueSuggestionActivity).get(CritiqueSuggestionViewModel::class.java)

        initView()
    }

    private fun initView(){
        csViewModel.getListCnS()

        csViewModel.isLoading.observe(this@CritiqueSuggestionActivity, {
            if(it) setForLoading(true) else setForLoading(false)
        })

        csViewModel.isFail.observe(this@CritiqueSuggestionActivity, {
            if(it) Log.e(TAG, "isFail: $it") else Log.d(TAG, "isFail: $it")
        })

        csViewModel.listCnS.observe(this@CritiqueSuggestionActivity, { listCnSResponse->
            if(!listCnSResponse.isNullOrEmpty()){
                val rvAdapter = ItemCnSAdapter(listCnSResponse.toMutableList())
                val rvLayoutManager = LinearLayoutManager(this@CritiqueSuggestionActivity)

                binding.rvCnS.apply {
                    adapter = rvAdapter
                    layoutManager = rvLayoutManager
                }

                binding.tvEmptyData.visibility = View.GONE
            }else{
                binding.rvCnS.visibility = View.GONE
                binding.tvEmptyData.visibility = View.VISIBLE
            }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.addcns_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_addCnS ->{
                Toast.makeText(this@CritiqueSuggestionActivity, "Implemented Soon", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRestart() {
        super.onRestart()
    }
}