package com.newsnexus.client.viewmodel

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.newsnexus.client.model.Constants.KEY.Companion.NEWSAPI_KEY
import com.newsnexus.client.model.Constants.STATUS.Companion.STATUS_ERROR
import com.newsnexus.client.model.Constants.STATUS.Companion.STATUS_SUCCESS
import com.newsnexus.client.model.dataclass.dummy.User
import com.newsnexus.client.model.dataclass.request.LoginRequest
import com.newsnexus.client.model.dataclass.response.login.LoginResponse
import com.newsnexus.client.model.local.LoginDao
import com.newsnexus.client.model.local.LoginDatabase

class LoginViewModel(application: Application): AndroidViewModel(application) {
    private val TAG = LoginViewModel::class.java.simpleName

    private var loginDao: LoginDao?= null
    private var loginDB : LoginDatabase?= LoginDatabase.getDatabase(application)

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isFail = MutableLiveData<Boolean>()
    val isFail: LiveData<Boolean> = _isFail

    private var _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    init {
        loginDao = loginDB?.loginDao()
    }

    fun registerListUser(listUser: List<User>){
        _isLoading.value = true
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            loginDao?.addListUser(listUser)
            Log.d(TAG, "addListUser")

            }, 4000)

        _isLoading.value = false

//        if(handler.hasMessages(MSG_COMPLETION)) {
//            Log.d(TAG, "isStop")
//            _isLoading.value = false
//        }
    }

    fun getUserLogin(loginRequest: LoginRequest): User? = loginDao?.retrieveUserLogin(loginRequest.email, loginRequest.password)

    fun loginUser(loginRequest: LoginRequest){
        _isLoading.value = true
        val retrievedUser = getUserLogin(loginRequest)
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            if(retrievedUser != null){
                _isLoading.value = false
                val response = LoginResponse(status = STATUS_SUCCESS,token = NEWSAPI_KEY, userName = retrievedUser.userName)
                _loginResponse.value = response
                _isFail.value = false
            }else{
                _isLoading.value = false
                _isFail.value = true
                val response = LoginResponse(status = STATUS_ERROR, null, null)
                _loginResponse.value = response
            }
        },4000)

    }

}