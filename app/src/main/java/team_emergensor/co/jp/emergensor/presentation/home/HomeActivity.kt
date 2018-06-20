package team_emergensor.co.jp.emergensor.presentation.home

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.WindowManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.data.repository.MyInfoRepository
import team_emergensor.co.jp.emergensor.databinding.ActivityHomeBinding
import team_emergensor.co.jp.emergensor.databinding.DrawerHeaderBinding
import team_emergensor.co.jp.emergensor.presentation.BaseActivity


class HomeActivity : BaseActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
    }

    private val myInfoRepository by lazy {
        MyInfoRepository(this)
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMap()
        initDrawer()
    }

    private val navigationHeaderViewModel by lazy {
        ViewModelProviders.of(this).get(NavigationHeaderViewModel::class.java)
    }

    private fun initDrawer() {
        compositeDisposable.add(
                myInfoRepository.getMyInfo()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { it, e ->
                            e?.let {
                                Log.e("getmyinfo", e.message)
                                return@subscribe
                            }
                            navigationHeaderViewModel.setMyInfo(it)
                        })

        binding.toolBar?.findViewById<android.support.v7.widget.Toolbar>(R.id.toolBar).apply {
            title = ""
            setSupportActionBar(this)
            setSupportActionBar(this)
            val actionBarDrawerToggle = ActionBarDrawerToggle(
                    this@HomeActivity,
                    binding.drawerLayout,
                    this,
                    0, 0
            )
            binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
            actionBarDrawerToggle.syncState()
        }

        binding.navigationView.apply {
            setNavigationItemSelectedListener(navigationViewListener)
            DataBindingUtil.bind<DrawerHeaderBinding>(getHeaderView(0))
            val headerBinding = DataBindingUtil.getBinding<DrawerHeaderBinding>(getHeaderView(0))
            headerBinding?.viewModel = navigationHeaderViewModel
        }
    }

    private fun initMap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        binding.fragmentContainer.apply {
            val fragment = MapFragment()
            val fragmentManager = fragmentManager
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit()
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
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return@OnNavigationItemSelectedListener true
    }
}
