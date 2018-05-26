package team_emergensor.co.jp.emergensor.ui.home

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.ActionBarDrawerToggle
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.databinding.ActivityHomeBinding
import team_emergensor.co.jp.emergensor.ui.BaseActivity


class HomeActivity : BaseActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.apply {
            toolBar.findViewById<android.support.v7.widget.Toolbar>(R.id.toolBar).apply {
                setSupportActionBar(this)
                val actionBarDrawerToggle = ActionBarDrawerToggle(
                        this@HomeActivity,
                        drawerLayout,
                        this,
                        0, 0
                )
                drawerLayout.addDrawerListener(actionBarDrawerToggle)
                actionBarDrawerToggle.syncState()
            }
            viewPager.apply {
                adapter = fragmentAdapter
                tabLayout.setupWithViewPager(this)
            }
        }
    }

    private val fragmentAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
        override fun getItem(position: Int): Fragment {
            val tab = HomeTab.values().first { it.ordinal == position }
            return when (tab) {
                HomeTab.MAP -> MapFragment()
                HomeTab.MEMBERS -> MembersFragment()
            }
        }

        override fun getCount(): Int {
            return HomeTab.values().size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return HomeTab.values().first { it.ordinal == position }.name
        }

    }

    private enum class HomeTab {
        MAP,
        MEMBERS
    }
}
