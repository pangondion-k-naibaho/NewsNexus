package com.newsnexus.client.view.activity.Home

interface CategoriesHomeCommunicator {
    fun startLoading()

    fun stopLoading()

    fun onPopUpStarted()

    fun onPopupStopped()
}