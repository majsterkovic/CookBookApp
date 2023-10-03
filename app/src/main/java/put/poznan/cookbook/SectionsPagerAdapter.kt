package put.poznan.cookbook

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionsPagerAdapter(fm: FragmentManager, private var context: Context) :
    FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return TopFragment()
            1 -> return TabFragment(Dish.TYPE_DISH)
            2 -> return TabFragment(Dish.TYPE_DESERT)
        }
        return TopFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return context.getString(R.string.home_tab)
            1 -> return context.getString(R.string.kat1_tab)
            2 -> return context.getString(R.string.kat2_tab)

        }
        return null
    }


}