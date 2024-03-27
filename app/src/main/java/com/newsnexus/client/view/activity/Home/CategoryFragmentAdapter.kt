package com.newsnexus.client.view.activity.Home

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.newsnexus.client.model.Constants.COUNTRY_CONSTANTS.CATEGORY_CONSTANTS.Companion.BUSINESS
import com.newsnexus.client.model.Constants.COUNTRY_CONSTANTS.CATEGORY_CONSTANTS.Companion.ENTERTAINMENT
import com.newsnexus.client.model.Constants.COUNTRY_CONSTANTS.CATEGORY_CONSTANTS.Companion.GENERAL
import com.newsnexus.client.model.Constants.COUNTRY_CONSTANTS.CATEGORY_CONSTANTS.Companion.HEALTH
import com.newsnexus.client.model.Constants.COUNTRY_CONSTANTS.CATEGORY_CONSTANTS.Companion.SCIENCE
import com.newsnexus.client.model.Constants.COUNTRY_CONSTANTS.CATEGORY_CONSTANTS.Companion.SPORTS
import com.newsnexus.client.model.Constants.COUNTRY_CONSTANTS.CATEGORY_CONSTANTS.Companion.TECHNOLOGY
import com.newsnexus.client.view.activity.Home.Fragments.BusinessFragment
import com.newssphere.client.view.activity.Home.Fragment.EntertainmentFragment
import com.newssphere.client.view.activity.Home.Fragment.GeneralFragment
import com.newssphere.client.view.activity.Home.Fragment.HealthFragment
import com.newssphere.client.view.activity.Home.Fragment.ScienceFragment
import com.newssphere.client.view.activity.Home.Fragment.SportsFragment
import com.newssphere.client.view.activity.Home.Fragment.TechnologyFragment

class CategoryFragmentAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 7
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment?= null
        when(position){
            0 -> fragment = GeneralFragment.newInstance(GENERAL)
            1 -> fragment = BusinessFragment.newInstance(BUSINESS)
            2 -> fragment = EntertainmentFragment.newInstance(ENTERTAINMENT)
            3 -> fragment = HealthFragment.newInstance(HEALTH)
            4 -> fragment = ScienceFragment.newInstance(SCIENCE)
            5 -> fragment = SportsFragment.newInstance(SPORTS)
            6 -> fragment = TechnologyFragment.newInstance(TECHNOLOGY)
        }
        return fragment as Fragment
    }

}