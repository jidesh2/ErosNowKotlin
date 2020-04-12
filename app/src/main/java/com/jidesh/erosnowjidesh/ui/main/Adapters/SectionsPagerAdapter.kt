package com.jidesh.erosnowjidesh.ui.main.Adapters

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jidesh.erosnowjidesh.R
import com.jidesh.erosnowjidesh.ui.main.Fragments.Favourite_fragment
import com.jidesh.erosnowjidesh.ui.main.Fragments.PlaceholderFragment

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
       var a: Fragment = PlaceholderFragment()
        when (position) {
            0 -> {

              a  = PlaceholderFragment()

            }
            1 -> {
               a = Favourite_fragment()
            }
        }

        return a
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int { // Show 2 total pages.
        return 2
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_text_1, R.string.tab_text_2)
    }

}