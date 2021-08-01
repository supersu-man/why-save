package com.supersuman.whysave

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> return "Type Number"
            else -> return "Call Logs"
        }
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> PhoneNumberFragment()
            else -> CallLogsFragment()
        }
    }
}