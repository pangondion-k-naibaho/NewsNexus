package com.newssphere.client.view.activity.Home.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newsnexus.client.R
import com.newsnexus.client.databinding.FragmentNewsBinding
import com.newsnexus.client.model.Constants
import com.newsnexus.client.view.activity.Detail.DetailActivity
import com.newsnexus.client.view.activity.Home.CategoriesHomeCommunicator
import com.newsnexus.client.view.activity.Home.HomeCategoriesCommunicator
import com.newsnexus.client.view.adapter.ItemNewsAdapter
import com.newsnexus.client.viewmodel.HomeViewModel
import com.newssphere.client.model.data_class.Article
import com.newssphere.client.view.advanced_ui.PopUpNotificationListener
import com.newssphere.client.view.advanced_ui.showPopupNotification

class TechnologyFragment : Fragment(), HomeCategoriesCommunicator {
    private val TAG = TechnologyFragment::class.java.simpleName
    private lateinit var binding : FragmentNewsBinding
    private var deliveredCategory: String?= null
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var categoriesHomeCommunicator: CategoriesHomeCommunicator
    private var currentPage: Int? = null
    private var itemNewsAdapter: ItemNewsAdapter? = null
    private var retrievedToken: String? = ""

    companion object{
        private const val DELIVERED_CATEGORY = "DELIVERED_CATEGORY"

        fun newInstance(deliveredCategory: String?= null): TechnologyFragment{
            val fragment = TechnologyFragment()
            val bundle = Bundle()
            bundle.putString(DELIVERED_CATEGORY, deliveredCategory)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.bind(
            inflater.inflate(
                R.layout.fragment_news,
                container,
                false
            )
        )

        deliveredCategory = arguments?.getString(DELIVERED_CATEGORY).toString()
        Log.d(TAG, "delivered Category: $deliveredCategory")

        categoriesHomeCommunicator = activity as CategoriesHomeCommunicator

        setUpView()

        return binding.root
    }

    private fun setUpView(){
        currentPage = 1

        val appPreferences = this@TechnologyFragment.requireActivity().getSharedPreferences(Constants.PREFERENCES.APP_PREFERENCES, Context.MODE_PRIVATE)
        retrievedToken = appPreferences.getString(Constants.PREFERENCES.TOKEN_KEY, null)

        homeViewModel.getNewsCollection(apiKey = retrievedToken!!, category = deliveredCategory, page = currentPage!!)

        homeViewModel.isLoading.observe(this@TechnologyFragment.requireActivity(), {
            if(it) categoriesHomeCommunicator.startLoading() else categoriesHomeCommunicator.stopLoading()
        })

        homeViewModel.isFail.observe(this@TechnologyFragment.requireActivity(), {
            if(it){
                categoriesHomeCommunicator.onPopUpStarted()
                this@TechnologyFragment.requireActivity().showPopupNotification(
                    textTitle = getString(R.string.tvPopupTitle_FailRetrieve),
                    textDesc = getString(R.string.tvPopupDesc_FailRetrieve),
                    backgroundImage = R.drawable.ic_clear_socialist,
                    listener = object: PopUpNotificationListener {
                        override fun onClickListener() {
                            this@TechnologyFragment.requireActivity().closeOptionsMenu()
                            categoriesHomeCommunicator.onPopupStopped()
                        }
                    }
                )
            }
        })

        homeViewModel.newsCollection.observe(this@TechnologyFragment.requireActivity(), {collectionNews->
            binding.apply {
                rvItemNews.apply {
                    itemNewsAdapter = ItemNewsAdapter(
                        collectionNews.articles!!.toMutableList(),
                        object: ItemNewsAdapter.ItemListener{
                            override fun onItemClicked(item: Article) {
                                this@TechnologyFragment.requireActivity().startActivity(
                                    DetailActivity.newIntent(
                                        this@TechnologyFragment.requireActivity(),
                                        deliveredCategory!!,
                                        item
                                    )
                                )
                                this@TechnologyFragment.requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                            }
                        }
                    )

                    val myLayoutManager = LinearLayoutManager(this@TechnologyFragment.requireActivity())
                    adapter = itemNewsAdapter
                    layoutManager = myLayoutManager

                    addOnScrollListener(object: RecyclerView.OnScrollListener(){
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)

                            val visibleItemCount = myLayoutManager.childCount
                            val totalItemCount = myLayoutManager.itemCount
                            val firstVisibleItemPosition = myLayoutManager.findFirstVisibleItemPosition()

                            if((visibleItemCount+firstVisibleItemPosition) >= totalItemCount
                                && firstVisibleItemPosition >= 0
                                && totalItemCount >= collectionNews.articles!!.size
                            ){
                                currentPage = currentPage?.plus(1)
                                homeViewModel.getNewsCollectionMore(apiKey = retrievedToken!!, category = deliveredCategory, page = currentPage!!)
                                homeViewModel.newsCollection2.observe(this@TechnologyFragment.requireActivity(), {collectionNews2->
                                    if(collectionNews2.articles != null){
                                        itemNewsAdapter!!.addItem(collectionNews2.articles!!)
                                    }
                                })
                            }
                        }
                    })
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        currentPage = null
    }

    override fun searchOnSelectedCategories(inputString: String) {
        Log.d(TAG, "searchOnSelectedCategories: $inputString")
        currentPage = 1

        homeViewModel.getNewsCollectionMore(apiKey = retrievedToken!!, category = deliveredCategory, searchInput = inputString, page = currentPage!!)

        homeViewModel.newsCollection2.observe(this@TechnologyFragment, {collectionNews ->
            Log.d(TAG, "result : ${collectionNews.articles}")
            if(!collectionNews.articles.isNullOrEmpty()){
                itemNewsAdapter!!.updateItem(collectionNews.articles!!)
            }else{
                Toast.makeText(this@TechnologyFragment.requireActivity(), "Shows no result", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun clearSearching() {
        currentPage = 1
        homeViewModel.getNewsCollectionMore(apiKey = retrievedToken!!, category = deliveredCategory, searchInput = null, page = currentPage!!)

        homeViewModel.newsCollection2.observe(this@TechnologyFragment.requireActivity(), {collectionNews ->
            if(!collectionNews.articles.isNullOrEmpty()){
                itemNewsAdapter!!.updateItem(collectionNews.articles!!)
            }else{
                Toast.makeText(this@TechnologyFragment.requireActivity(), "Couldn't fetch data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}