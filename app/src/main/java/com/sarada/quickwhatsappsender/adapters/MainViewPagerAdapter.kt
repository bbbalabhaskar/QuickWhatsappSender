package com.sarada.quickwhatsappsender.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sarada.quickwhatsappsender.ui.calllogs.CallLogFragment
import com.sarada.quickwhatsappsender.ui.dialpad.DialPadFragment

class MainViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {


    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            DialPadFragment.newInstance()
        } else {
            CallLogFragment.newInstance()
        }
    }

}