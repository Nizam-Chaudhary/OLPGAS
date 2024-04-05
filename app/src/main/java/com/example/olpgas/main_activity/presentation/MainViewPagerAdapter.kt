package com.example.olpgas.main_activity.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.olpgas.BookingsFragment
import com.example.olpgas.browse_rooms.presentation.HomeFragment
import com.example.olpgas.manage_room.presentation.owned_rooms.ManageFragment
import com.example.olpgas.more_details.presentation.MoreFragment
import java.lang.IllegalArgumentException

class MainViewPagerAdapter(
    private val fragmentManager: FragmentManager,
    private val lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HomeFragment()
            1 -> BookingsFragment()
            2 -> ManageFragment()
            3 -> MoreFragment()
            else -> throw(IllegalArgumentException("Invalid Position"))
        }
    }
}