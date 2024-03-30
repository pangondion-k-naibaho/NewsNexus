package com.newsnexus.client.view.activity.CritiqueSugesstions

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu.OnDismissListener
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.newsnexus.client.R
import com.newsnexus.client.databinding.ActivityCritiqueSuggestionBinding
import com.newsnexus.client.databinding.BottomsheetLayoutBinding
import com.newsnexus.client.model.Constants
import com.newsnexus.client.model.Extensions.Companion.dpToPx
import com.newsnexus.client.model.dataclass.dummy.CritiqueSuggestions
import com.newsnexus.client.view.adapter.ItemCnSAdapter
import com.newsnexus.client.view.advanced_ui.InputTextView
import com.newsnexus.client.viewmodel.CritiqueSuggestionViewModel
import com.newssphere.client.view.advanced_ui.PopUpNotificationListener
import com.newssphere.client.view.advanced_ui.showPopupNotification

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
        supportActionBar!!.setDisplayShowTitleEnabled(true)

        supportActionBar!!.setTitle(getString(R.string.tvTitle_Critiques_n_SUggestions))

        appPreferences = this@CritiqueSuggestionActivity.getSharedPreferences(Constants.PREFERENCES.APP_PREFERENCES, Context.MODE_PRIVATE)
        retrievedUsername = appPreferences.getString(Constants.PREFERENCES.USERNAME_KEY, null)

        csViewModel = ViewModelProvider(this@CritiqueSuggestionActivity).get(CritiqueSuggestionViewModel::class.java)

        initView()
    }

    private fun initView(){
        csViewModel.getListCnS_e1().observe(this@CritiqueSuggestionActivity, { listCnSResponse->
            Log.d(TAG, "listCnSResponse: $listCnSResponse")
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

        csViewModel.isLoading.observe(this@CritiqueSuggestionActivity, {
            if(it) setForLoading(true) else setForLoading(false)
        })

        csViewModel.isFail.observe(this@CritiqueSuggestionActivity, {
            if(it) Log.e(TAG, "isFail: $it") else Log.d(TAG, "isFail: $it")
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
                setUpBottomSheet()
                true
            }
            android.R.id.home ->{
                onBackPressed()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpBottomSheet(){
        val bottomSheetDialog = BottomSheetDialog(this@CritiqueSuggestionActivity)
        val mBottomSheetDialog = BottomsheetLayoutBinding.inflate(layoutInflater, null, false)

        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            600.dpToPx(this@CritiqueSuggestionActivity)
        )

        mBottomSheetDialog.root.layoutParams = layoutParams
        bottomSheetDialog.setContentView(mBottomSheetDialog.root)

        mBottomSheetDialog.itvCritique.apply {
            setInputType(InputTextView.INPUT_TYPE.MULTILINE)
            setTitle(getString(R.string.tvTitle_Critique))
            setTextHelper(getString(R.string.tvHint_Critique))
        }

        mBottomSheetDialog.itvSuggestion.apply {
            setInputType(InputTextView.INPUT_TYPE.MULTILINE)
            setTitle(getString(R.string.tvTitle_Suggestion))
            setTextHelper(getString(R.string.tvHint_Suggestion))
        }

        mBottomSheetDialog.btnLeft.apply {
            setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }

        mBottomSheetDialog.btnRight.apply {
            setOnClickListener {
                val retrievedUsername = appPreferences.getString(Constants.PREFERENCES.USERNAME_KEY, null)
                val retrievedCritique = mBottomSheetDialog.itvCritique.getText()
                val retrievedSuggestion = mBottomSheetDialog.itvSuggestion.getText()

                Log.d(TAG, "$retrievedUsername $retrievedCritique $retrievedSuggestion")

                bottomSheetDialog.dismiss()
                csViewModel.addCritiquenSuggestion(CritiqueSuggestions(0, retrievedUsername!!, retrievedCritique, retrievedSuggestion))

                csViewModel.isAddCnSSuccess.observe(this@CritiqueSuggestionActivity, {
                    setForPopUpDisplaying(true)
                    this@CritiqueSuggestionActivity.showPopupNotification(
                        textTitle = getString(R.string.tvPopupTitle_Success),
                        textDesc = getString(R.string.tvPopupDesc_SuccessAddCnS),
                        backgroundImage = R.drawable.ic_checklist_green,
                        listener = object: PopUpNotificationListener{
                            override fun onClickListener() {
                                this@CritiqueSuggestionActivity.closeOptionsMenu()
                                this@CritiqueSuggestionActivity.recreate()
                            }
                        }
                    )
                })
            }

        }

        bottomSheetDialog.show()
    }

    override fun onRestart() {
        super.onRestart()
    }
}