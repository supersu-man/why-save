package com.supersuman.whysave

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.supersuman.apkupdater.ApkUpdater
import com.supersuman.whysave.databinding.ActivityMainBinding
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.viewPager.adapter = PagerAdapter(supportFragmentManager)

        thread {
            val url = "https://github.com/supersu-man/why-save/releases/latest"
            val updater = ApkUpdater(this@MainActivity, url)
            updater.threeNumbers = true
            checkForUpdates(updater)
        }
    }

    private fun checkForUpdates(updater: ApkUpdater) {
        if (updater.isInternetConnection() && updater.isNewUpdateAvailable() == true) {
            Snackbar.make(binding.root, "New Update Found", Snackbar.LENGTH_INDEFINITE).setAction("Download") {
                thread { updater.requestDownload() }
            }.show()
        }
    }

}