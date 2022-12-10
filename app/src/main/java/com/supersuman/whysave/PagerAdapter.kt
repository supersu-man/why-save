package com.supersuman.whysave

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int { return 2 }

    override fun createFragment(position: Int): Fragment {
        val phoneNumberFragment = PhoneNumberFragment()
        val callLogsFragment = CallLogsFragment()
        return when (position) {
            0 -> phoneNumberFragment
            else -> callLogsFragment
        }
    }
}