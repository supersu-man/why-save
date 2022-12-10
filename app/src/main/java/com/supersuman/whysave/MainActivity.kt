package com.supersuman.whysave

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.supersuman.apkupdater.ApkUpdater
import com.supersuman.whysave.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        checkForUpdates()
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = PagerAdapter(this)
        binding.viewPager.offscreenPageLimit = 2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = "Keypad"
                else -> tab.text = "Call Logs"
            }
        }.attach()
    }

    private fun checkForUpdates() = CoroutineScope(Dispatchers.IO).launch {
        val url = "https://github.com/supersu-man/why-save/releases/latest"
        val updater = ApkUpdater(this@MainActivity, url)
        updater.threeNumbers = true
        if (updater.isInternetConnection() && updater.isNewUpdateAvailable() == true) {
            Snackbar.make(binding.root, "New Update Found", Snackbar.LENGTH_INDEFINITE).setAction("Download") {
                CoroutineScope(Dispatchers.IO).launch { updater.requestDownload() }
            }.show()
        }
    }

}

class PagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int { return 2 }
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PhoneNumberFragment()
            else -> CallLogsFragment()
        }
    }
}