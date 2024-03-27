package com.newsnexus.client.view.activity.Detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.newsnexus.client.R
import com.newsnexus.client.databinding.ActivityDetailBinding
import com.newssphere.client.model.data_class.Article
import java.text.SimpleDateFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {
    private val TAG = DetailActivity::class.java.simpleName
    private lateinit var binding: ActivityDetailBinding
    private var retrievedArticle: Article?= null
    private var retrievedCategory: String = ""

    companion object{
        private const val EXTRA_ARTICLE = "EXTRA_ARTICLE"
        private const val EXTRA_CATEGORY = "EXTRA_CATEGORY"
        fun newIntent(context: Context, deliveredCategory: String, deliveredArticle: Article): Intent = Intent(context, DetailActivity::class.java)
            .putExtra(EXTRA_CATEGORY, deliveredCategory)
            .putExtra(EXTRA_ARTICLE, deliveredArticle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrievedArticle = intent.extras!!.get(EXTRA_ARTICLE) as Article
        retrievedCategory = intent.extras!!.getString(EXTRA_CATEGORY) as String

        val handler = Handler(Looper.getMainLooper())

        setForLoading(true)
        handler.postDelayed({
            setUpView()
            setForLoading(false)
        },4000)

    }

    private fun setUpView(){
        binding.apply {
            if(!retrievedArticle!!.urlToImage.isNullOrEmpty()){
                Glide.with(this@DetailActivity)
                    .load(retrievedArticle!!.urlToImage)
                    .into(ivNewsBackground)
            }else{
                Glide.with(this@DetailActivity)
                    .load(ContextCompat.getDrawable(this@DetailActivity, R.drawable.newsnexus_logo))
                    .into(ivNewsBackground)
            }


            ivBack.setOnClickListener{
                finish()
            }

            tvNewsTitle.text = retrievedArticle!!.title
            tvNewsDescription.apply {
                if(retrievedArticle!!.description.isNullOrEmpty()){
                    text = getString(R.string.tvDetail_NoDescription)
                }else{
                    text = retrievedArticle!!.description
                }
            }
            tvNewsContent.text = retrievedArticle!!.content
            tvNewsPublishedBy.apply {
                if(retrievedArticle!!.source == null){
                    text = String.format(getString(R.string.tvDummy_NewsPublishedBy, retrievedArticle!!.author))
                }else{
                    text = String.format(getString(R.string.tvDummy_NewsPublishedBy, retrievedArticle!!.source!!.name))
                }
            }

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(retrievedArticle!!.publishedAt!!)
            val formattedDate = outputFormat.format(date!!)

            tvNewsPublishedAt.text = formattedDate

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
}