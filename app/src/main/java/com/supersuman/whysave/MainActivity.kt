package com.supersuman.whysave

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.supersuman.githubapkupdater.Updater
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var rootLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        initViews()

        thread {
            val updater = Updater(this, "https://github.com/supersu-man/WhySave/releases/latest")
            checkForUpdates(updater)
        }
    }



    private fun initViews() {
        rootLayout = findViewById(R.id.rootLayout)
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)
        viewPager.adapter = PagerAdapter(supportFragmentManager)
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