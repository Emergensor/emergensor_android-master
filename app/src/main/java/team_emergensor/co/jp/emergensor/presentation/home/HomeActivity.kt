package team_emergensor.co.jp.emergensor.presentation.home

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.databinding.ActivityHomeBinding
import team_emergensor.co.jp.emergensor.presentation.BaseActivity


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
            toolBar.findViewById<android.support.v7.widget.Toolbar>(R.id.toolBar)
                    .apply {
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
            fragmentContainer
                    .apply {
                        val fragment = MapFragment()
                        // Insert the fragment by replacing any existing fragment
                        val fragmentManager = fragmentManager
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainer, fragment)
                                .commit()
                    }
            navigationView.setNavigationItemSelectedListener(navigationViewListener)
        }
    }

    private val navigationViewListener = NavigationView.OnNavigationItemSelectedListener {
        val fragmentManager = fragmentManager
        when (it.itemId) {
            R.id.home -> {
                val fragment = MapFragment()
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .commit()
            }
            R.id.friends -> {
                val fragment = MembersFragment()
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .commit()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return@OnNavigationItemSelectedListener true
    }
}
