package com.sarada.quickwhatsappsender

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import com.sarada.quickwhatsappsender.adapters.MainViewPagerAdapter


class MainActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    private lateinit var mainTabLayout: TabLayout
    private lateinit var mainViewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewPager = findViewById(R.id.main_view_pager)
        mainTabLayout = findViewById(R.id.main_nav_tabs)

        mainViewPager.adapter = createPagerAdapter()

        TabLayoutMediator(mainTabLayout, mainViewPager,
            TabConfigurationStrategy { tab, position ->
                tab.text = if ( position == 0 ) {
                    getString(R.string.title_dial)
                } else {
                    getString(R.string.title_call_log)
                }

                val tetView = tab.view[1] as AppCompatTextView
                tetView.textSize = 36.0f
                mainViewPager.setCurrentItem(tab.position, true)
            }).attach()
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.exit_message), Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    private fun createPagerAdapter() = MainViewPagerAdapter(this)


}
