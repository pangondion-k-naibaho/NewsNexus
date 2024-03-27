package com.newsnexus.client.view.activity.Home

interface HomeCategoriesCommunicator {
    fun searchOnSelectedCategories(inputString: String)

    fun clearSearching()
}