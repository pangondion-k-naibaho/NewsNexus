package com.newsnexus.client.model.remote

import com.newsnexus.client.model.Constants.COUNTRY_CONSTANTS.Companion.ID
import com.newssphere.client.model.data_class.NewsCollection
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    fun getCollectionTopHeadlineNews(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String?= ID,
        @Query("category") category: String? = null,
        @Query("q") searchInput: String?= null,
        @Query("page") page: Int
    ): Call<NewsCollection>
}