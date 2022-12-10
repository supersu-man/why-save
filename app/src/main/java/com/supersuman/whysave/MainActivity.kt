package com.supersuman.whysave

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.supersuman.githubapkupdater.Updater
import com.supersuman.whysave.databinding.ActivityMainBinding
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var rootLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.viewPager.adapter = PagerAdapter(supportFragmentManager)

        thread {
            val updater = Updater(this, "https://github.com/supersu-man/why-save/releases/latest")
            checkForUpdates(updater)
        }
    }




    private fun checkForUpdates(updater: Updater){
        if (updater.isInternetConnection()){
            updater.init()
            updater.isNewUpdateAvailable {
                Snackbar.make(rootLayout,"New Update Found",Snackbar.LENGTH_INDEFINITE).setAction("Download"){
                    if (updater.hasPermissionsGranted()){
                        updater.requestDownload()
                    } else{
                        updater.requestMyPermissions {
                            updater.requestDownload()
                        }
                    }
                }.show()
            }
        }else{
            Snackbar.make(rootLayout,"Unable To Check For Updates",Snackbar.LENGTH_SHORT).show()
        }
    }
}