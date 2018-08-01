package team_emergensor.co.jp.emergensor.presentation.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.WindowManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.data.repository.EmergencyCallRepository
import team_emergensor.co.jp.emergensor.data.repository.EmergensorUserRepository
import team_emergensor.co.jp.emergensor.databinding.ActivityHomeBinding
import team_emergensor.co.jp.emergensor.databinding.DrawerHeaderBinding
import team_emergensor.co.jp.emergensor.presentation.BaseActivity
import team_emergensor.co.jp.emergensor.presentation.analysys.AnalysysFragment
import team_emergensor.co.jp.emergensor.presentation.mapandfeed.MapFragment
import team_emergensor.co.jp.emergensor.presentation.members.MembersFragment
import team_emergensor.co.jp.emergensor.presentation.settings.SettingsFragment


class HomeActivity : BaseActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
    }

    private val emergensorUserRepository by lazy {
        EmergensorUserRepository(this)
    }
    private val emergencyCallRepository by lazy {
        EmergencyCallRepository(this)
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = homeViewModel
        binding.setLifecycleOwner(this)
        initFragment()
        initDrawer()
        initViewModel()
        initSubscribe()
    }

    private val navigationHeaderViewModel by lazy {
        ViewModelProviders.of(this).get(NavigationHeaderViewModel::class.java)
    }

    private val homeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    private fun initDrawer() {
        binding.mapToolBar?.findViewById<android.support.v7.widget.Toolbar>(R.id.mapToolBar)?.apply {
            title = ""
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

        binding.navigationView.run {
            setNavigationItemSelectedListener(homeViewModel.navigationViewListener)
            DataBindingUtil.bind<DrawerHeaderBinding>(getHeaderView(0))
            val headerBinding = DataBindingUtil.getBinding<DrawerHeaderBinding>(getHeaderView(0))
            headerBinding?.setLifecycleOwner(this@HomeActivity)
            headerBinding?.viewModel = navigationHeaderViewModel
        }
    }

    private fun initFragment() {
        setWindowFullScreen(true)
        binding.fragmentContainer.apply {
            val fragment = MapFragment()
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit()
        }
    }

    private fun initViewModel() {
        homeViewModel.replaceFragmentPublisher.observe(this, Observer<HomeViewModel.State> {
            val fragmentManager = supportFragmentManager
            var fragment: Fragment? = null
            var title = ""
            when (it) {
                HomeViewModel.State.HOME -> {
                    setWindowFullScreen(true)
                    binding.navigationView.menu.getItem(0).isChecked = true
                    fragment = MapFragment()
                }
                HomeViewModel.State.MEMBERS -> {
                    setWindowFullScreen(false)
                    binding.navigationView.menu.getItem(1).isChecked = true
                    fragment = MembersFragment()
                    title = "MEMBERS"
                }
                HomeViewModel.State.SETTINGS -> {
                    setWindowFullScreen(false)
                    binding.navigationView.menu.getItem(3).isChecked = true
                    fragment = SettingsFragment()
                    title = "SETTINGS"
                }
                HomeViewModel.State.ANALYSYS -> {
                    setWindowFullScreen(false)
                    binding.navigationView.menu.getItem(2).isChecked = true
                    fragment = AnalysysFragment()
                    title = "ACCELERATION ANALYSYS"
                }
            }
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit()
            supportActionBar?.title = title
        })

        homeViewModel.drawerShouldClosePublisher.observe(this, Observer<Boolean> {
            val shouldClose = it ?: return@Observer
            if (shouldClose) {
                binding.drawerLayout.closeDrawer(android.support.v4.view.GravityCompat.START)
            }
        })

        homeViewModel.mapToolbarVisible.observe(this, Observer {
            val isMap = it ?: return@Observer
            if (isMap) {
                binding.mapToolBar?.findViewById<Toolbar>(R.id.mapToolBar).apply {
                    title = ""
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
            } else {
                binding.normalToolBar?.findViewById<Toolbar>(R.id.normalToolBar).apply {
                    title = ""
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
            }
        })
    }

    private fun initSubscribe() {
        compositeDisposable.add(
                emergensorUserRepository.getMyInfoWithAsync()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { it, e ->
                            e?.let {
                                Log.e("get my info", e.message)
                                return@subscribe
                            }
                            navigationHeaderViewModel.facebookInfo = it
                        })
    }

    private fun setWindowFullScreen(boolean: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (boolean) {
                window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
