package team_emergensor.co.jp.emergensor.presentation.home

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.design.widget.NavigationView
import team_emergensor.co.jp.emergensor.R

class HomeViewModel : ViewModel() {

    enum class State {
        HOME,
        MEMBERS,
        SETTINGS
    }

    private var state = State.HOME
        set(value) {
            if (field != value) {
                val isToolbarVisible = when (value) {
                    State.HOME -> true
                    State.MEMBERS -> false
                    State.SETTINGS -> false
                }
                mapToolbarVisible.postValue(isToolbarVisible)
                replaceFragmentPublisher.postValue(value)
            }
            drawerShouldClosePublisher.postValue(true)
            field = value
        }

    val mapToolbarVisible = MutableLiveData<Boolean>()

    val replaceFragmentPublisher = MutableLiveData<State>()
    val drawerShouldClosePublisher = MutableLiveData<Boolean>()

    init {
        mapToolbarVisible.postValue(true)
    }

    val navigationViewListener = NavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.map -> {
                state = State.HOME
            }
            R.id.settings -> {
                state = State.SETTINGS
            }
            R.id.members -> {
                state = State.MEMBERS
            }
        }
        return@OnNavigationItemSelectedListener true
    }

    fun backHome() {
        state = State.HOME
    }
}