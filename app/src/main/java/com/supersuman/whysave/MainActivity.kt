package com.supersuman.whysave

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import kotlin.math.atan2


class MainActivity : AppCompatActivity() {

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        println(event)
        return super.onTouchEvent(event)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout : TabLayout = findViewById(R.id.tabLayout)
        tabLayout.addTab(tabLayout.newTab().setText("Type Number"))
        tabLayout.addTab(tabLayout.newTab().setText("Call Logs"))
        val viewPager : ViewPager = findViewById(R.id.viewPager)
        val adapter = PagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab?.position!!

            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }

    private fun closeKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}