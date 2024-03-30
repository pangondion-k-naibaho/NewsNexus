package com.newsnexus.client.viewmodel

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.newsnexus.client.model.dataclass.dummy.CritiqueSuggestions
import com.newsnexus.client.model.local.CnSDatabase
import com.newsnexus.client.model.local.CnsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CritiqueSuggestionViewModel(application: Application):AndroidViewModel(application) {
    private val TAG = LoginViewModel::class.java.simpleName

    private var cnsDao: CnsDao?= null
    private var cnsDB: CnSDatabase?= CnSDatabase.getDatabase(application)

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isFail = MutableLiveData<Boolean>()
    val isFail: LiveData<Boolean> = _isFail

    private var _isAddCnSSuccess = MutableLiveData<Boolean>()
    val isAddCnSSuccess: LiveData<Boolean> = _isAddCnSSuccess

    private var _listCnS = MutableLiveData<List<CritiqueSuggestions>>()
    val listCnS: LiveData<List<CritiqueSuggestions>> = _listCnS

    fun addCritiquenSuggestion(inputCnS: CritiqueSuggestions){
        _isLoading.value = true

        val work = CoroutineScope(Dispatchers.IO).launch {
            cnsDao?.addCnS(inputCnS)
        }

        if(work.isCompleted){
            _isLoading.value = false
            _isAddCnSSuccess.value = true
        }

    }

    fun getListCnS(){
        _isLoading.value = true
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            val retrievedListCnS = cnsDao?.getListCnS()!!.value
//            _listCnS.value = retrievedListCnS!!
            try {
                _listCnS.value = retrievedListCnS!!
            }catch (e: NullPointerException){
                _listCnS.value = listOf<CritiqueSuggestions>()
            }
            _isLoading.value = false
        }, 4000)
    }
}