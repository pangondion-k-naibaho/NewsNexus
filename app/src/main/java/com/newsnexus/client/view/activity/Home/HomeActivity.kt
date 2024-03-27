package com.newsnexus.client.view.activity.Home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.newsnexus.client.R
import com.newsnexus.client.databinding.ActivityHomeBinding
import com.newsnexus.client.model.Constants
import com.newsnexus.client.model.Constants.PREFERENCES.Companion.APP_PREFERENCES
import com.newsnexus.client.model.Constants.PREFERENCES.Companion.TOKEN_KEY
import com.newsnexus.client.model.Constants.PREFERENCES.Companion.USERNAME_KEY
import com.newsnexus.client.view.activity.About.AboutActivity
import com.newsnexus.client.view.activity.CritiqueSugesstions.CritiqueSuggestionActivity
import com.newsnexus.client.view.activity.Login.LoginActivity
import com.newsnexus.client.viewmodel.HomeViewModel
import com.newssphere.client.view.advanced_ui.InputSearchView
import com.newssphere.client.view.advanced_ui.PopUpDialogListener
import com.newssphere.client.view.advanced_ui.PopUpNotificationListener
import com.newssphere.client.view.advanced_ui.showPopupDialog
import com.newssphere.client.view.advanced_ui.showPopupNotification

class HomeActivity : AppCompatActivity(), CategoriesHomeCommunicator {
    private val TAG = HomeActivity::class.java.simpleName
    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel by viewModels<HomeViewModel>()
    private var selectedFragment: Fragment?= null
    private var retrievedInput: String?= null
    private lateinit var appPreferences: SharedPreferences
    private var userToken: String? = null

    companion object{
        fun newIntent(context: Context):Intent = Intent(context, HomeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appPreferences = this@HomeActivity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        initView()
    }

    private fun initView(){
        binding.apply {
            val newsCategoryAdapter = CategoryFragmentAdapter(this@HomeActivity)
            vpCategoryNews.apply {
                adapter = newsCategoryAdapter
                registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        selectedFragment = supportFragmentManager.findFragmentByTag("f$position")
                    }
                })
            }

            val categoryTitleArray: Array<String> = Constants.COUNTRY_CONSTANTS.CATEGORY_CONSTANTS.Companion.CATEGORY_ENUM.values().map { it.name.toLowerCase().capitalize() }.toTypedArray()

            TabLayoutMediator(tlCategoryNews, vpCategoryNews, false, true){tab, position ->
                tab.text = categoryTitleArray[position]
            }.attach()

            btnMore.setOnClickListener(object: View.OnClickListener {
                override fun onClick(v: View?) {
                    val popUpMenu = PopupMenu(this@HomeActivity, v!!)
                    popUpMenu.menuInflater.inflate(R.menu.home_menu, popUpMenu.menu)

                    popUpMenu.setOnMenuItemClickListener(object: PopupMenu.OnMenuItemClickListener{
                        override fun onMenuItemClick(item: MenuItem?): Boolean {
                            when(item!!.itemId){
                                R.id.menuAbout ->{
                                    startActivity(
                                        AboutActivity.newIntent(this@HomeActivity)
                                    )
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                }
                                R.id.menuCnS ->{
                                    startActivity(
                                        CritiqueSuggestionActivity.newIntent(this@HomeActivity)
                                    )
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                }
                                R.id.menuLogout ->{
                                    setForPopUpDisplaying(true)
                                    this@HomeActivity.showPopupDialog(
                                        textTitle = getString(R.string.tvPopupTitle_Logout),
                                        textDesc = getString(R.string.tvPopupDesc_Logout),
                                        backgroundImage = R.drawable.ic_logout,
                                        btnTextRight = getString(R.string.btnTxt_Cancel),
                                        btnTextLeft = getString(R.string.btnTxt_Logout),
                                        listener = object: PopUpDialogListener{
                                            override fun onLeftButtonClickListener() {
                                                this@HomeActivity.closeOptionsMenu()
                                                setForPopUpDisplaying(false)
                                                homeViewModel.logOutUser()
                                                homeViewModel.isLoading.observe(this@HomeActivity, {
                                                    if(it) setForLoading(true) else setForLoading(false)
                                                })
                                                homeViewModel.isLogoutSuccess.observe(this@HomeActivity, {
                                                    if(it){
                                                        val editor = appPreferences.edit()
                                                        editor.remove(TOKEN_KEY)
                                                        editor.remove(USERNAME_KEY)
                                                        editor.apply()
                                                        if(editor.commit()){
                                                            setForPopUpDisplaying(true)
                                                            showPopupNotification(
                                                                textTitle = getString(R.string.tvPopupTitle_Success),
                                                                textDesc = getString(R.string.tvPopupDesc_LogoutSuccess),
                                                                backgroundImage = R.drawable.ic_checklist_green,
                                                                listener = object: PopUpNotificationListener{
                                                                    override fun onClickListener() {
                                                                        this@HomeActivity.closeOptionsMenu()
                                                                        startActivity(
                                                                            LoginActivity.newIntent(this@HomeActivity)
                                                                        )
                                                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                                                        finish()

                                                                    }
                                                                }
                                                            )
                                                        }
                                                    }else{
                                                        Toast.makeText(this@HomeActivity, "Failed to Log Out", Toast.LENGTH_SHORT)
                                                            .show()
                                                    }
                                                })
                                            }

                                            override fun onRightButtonClickListener() {
                                                this@HomeActivity.closeOptionsMenu()
                                                setForPopUpDisplaying(false)
                                            }

                                        }
                                    )
                                }
                            }
                            return true
                        }

                    })

                    popUpMenu.show()
                }
            })

            isvSearchNews.apply {
                setListener(object: InputSearchView.InputSearchListener{
                    override fun onClickSearch() {
                        retrievedInput = getText()
                        Log.d(TAG, "selectedFragment: ${selectedFragment!!::class.java.simpleName}")
                        val homeCategoriesCommunicator = selectedFragment as HomeCategoriesCommunicator

                        homeCategoriesCommunicator.searchOnSelectedCategories(retrievedInput!!)
                    }

                    override fun onClearSearch() {
                        clearText()
                        val homeCategoriesCommunicator = selectedFragment as HomeCategoriesCommunicator
                        homeCategoriesCommunicator.clearSearching()
                    }

                })
            }
        }
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

    override fun startLoading() {
        setForLoading(true)
    }

    override fun stopLoading() {
        setForLoading(false)
    }

    override fun onPopUpStarted() {
        setForPopUpDisplaying(true)
    }

    override fun onPopupStopped() {
        setForPopUpDisplaying(false)
    }
}