package team_emergensor.co.jp.emergensor.presentation.mapandfeed

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.location.Location
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.GeoPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.data.repository.DangerousAreaRepository
import team_emergensor.co.jp.emergensor.data.repository.EmergencyCallRepository
import team_emergensor.co.jp.emergensor.databinding.FragmentMapBinding
import team_emergensor.co.jp.emergensor.domain.entity.ActionType
import team_emergensor.co.jp.emergensor.domain.entity.AutoEmergencyCall
import team_emergensor.co.jp.emergensor.domain.entity.EmergencyCall
import team_emergensor.co.jp.emergensor.domain.entity.EmergencyType
import java.util.*


class MapFragment : Fragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationSource {

    private lateinit var binding: FragmentMapBinding

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MapViewModel::class.java)
    }

    private val emergencyCallRepository by lazy {
        val context = context ?: return@lazy null
        EmergencyCallRepository(context)
    }

    private val dangerousAreaRepository by lazy {
        val context = context ?: return@lazy null
        DangerousAreaRepository(context)
    }

    private var map: GoogleMap? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        binding.viewModel = viewModel

        initSubscribe()
        initMap()

        if (googleMapFragment == null) {
            googleMapFragment = SupportMapFragment.newInstance()
            googleMapFragment?.getMapAsync(this)
        }
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.map, googleMapFragment).commit()

        val manager = LinearLayoutManager(context).also {
            it.orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.markerListView.also {
            it.layoutManager = manager
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                private var isScrollRight = true
                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    when (newState) {
                        RecyclerView.SCROLL_STATE_IDLE -> {
                            val visibleItemCount = recyclerView?.childCount ?: return
                            val manager = recyclerView.layoutManager as LinearLayoutManager
                            val firstVisibleItem = manager.findFirstVisibleItemPosition()
                            val lastInScreen = firstVisibleItem + visibleItemCount - 1
                            if (isScrollRight) {
                                manager.smoothScrollToPosition(recyclerView, RecyclerView.State(), lastInScreen)
                            } else {
                                manager.smoothScrollToPosition(recyclerView, RecyclerView.State(), firstVisibleItem)
                            }
                        }
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    isScrollRight = dx > 0
                }
            })
            it.adapter = viewModel.adapter
        }
        return binding.root
    }

    override fun onMapReady(p0: GoogleMap?) {
        map = p0
        val context = context ?: return
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            map?.run {
                isMyLocationEnabled = true
                setLocationSource(this@MapFragment)
                isMyLocationEnabled = true
            }
        }
    }

    private fun initMap() {
        val context = context ?: return
        mGoogleApiClient = GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()
    }

    private fun initSubscribe() {
        viewModel.emergencyCallPublisher.observe(this, emergencyCallObserver)
        val repo = dangerousAreaRepository ?: return
        compositeDisposable.add(
                repo.observeDangerousArea()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            viewModel.dangerousAreas = it
                        }
        )
    }


    private var sending = false
    private var shouldCall = false
    private val emergencyCallObserver = Observer<Unit> {
        if (sending) {
            Toast.makeText(activity, "now sending another call", Toast.LENGTH_SHORT).show()
            return@Observer
        } else {
            Toast.makeText(activity, "sending call", Toast.LENGTH_LONG).show()
        }
        if (!viewModel.canUseGPS) {
            Toast.makeText(activity, "turn on GPS", Toast.LENGTH_SHORT).show()
            return@Observer
        } else {
            if (lastPosition == null) {
                shouldCall = true
            } else {
                sendLocation(lastPosition)
            }
        }
    }

    override fun onConnected(p0: Bundle?) {
        // check permission
        val context = context ?: return
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // FusedLocationApi
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, locationListener)
        } else {
            return
        }
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    private var lastPosition: Location? = null

    private val locationListener = LocationListener { p0 ->
        val location = p0 ?: return@LocationListener
        lastPosition = location

        if (shouldCall) sendLocation(location)

        val lat = location.latitude
        val lng = location.longitude

        val newLocation = LatLng(lat, lng)
        map?.addMarker(MarkerOptions().position(newLocation).title("My Location"))
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 8f))
        val marker = MarkerOptions().position(LatLng(35.5, 140.0)).title("hoge")
        map?.addMarker(marker)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(35.5, 140.0), 8f))
    }

    private val locationRequest = LocationRequest.create().also {
        val priority = 1
        when (priority) {
            0 -> // 高い精度の位置情報を取得したい場合
                // インターバルを例えば5000msecに設定すれば
                // マップアプリのようなリアルタイム測位となる
                // 主に精度重視のためGPSが優先的に使われる
                it.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            1 -> // バッテリー消費を抑えたい場合、精度は100mと悪くなる
                // 主にwifi,電話網での位置情報が主となる
                // この設定の例としては　setInterval(1時間)、setFastestInterval(1分)
                it.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            2 -> // バッテリー消費を抑えたい場合、精度は10kmと悪くなる
                it.priority = LocationRequest.PRIORITY_LOW_POWER
            else -> // 受け身的な位置情報取得でアプリが自ら測位せず、
                // 他のアプリで得られた位置情報は入手できる
                it.priority = LocationRequest.PRIORITY_NO_POWER
        }

        // アップデートのインターバル期間設定
        // このインターバルは測位データがない場合はアップデートしません
        // また状況によってはこの時間よりも長くなることもあり
        // 必ずしも正確な時間ではありません
        // 他に同様のアプリが短いインターバルでアップデートしていると
        // それに影響されインターバルが短くなることがあります。
        // 単位：msec
        it.interval = 60000
        // このインターバル時間は正確です。これより早いアップデートはしません。
        // 単位：msec
        it.fastestInterval = 5000
    }


    private fun sendLocation(location: Location?) {
        val location = location ?: return
        shouldCall = false
        val user = emergencyCallRepository?.getMyInfoLocal() ?: return
        val call = EmergencyCall(user.id, Calendar.getInstance().time, GeoPoint(location.latitude, location.longitude), "", EmergencyType.VIOLENCE)
        val autoCall = AutoEmergencyCall(user.id, Calendar.getInstance().time, GeoPoint(location.latitude, location.longitude), "", ActionType.RUN)
        emergencyCallRepository?.call(call)
        emergencyCallRepository?.autoCall(autoCall)
        Toast.makeText(context, "emergency call sent", Toast.LENGTH_SHORT).show()
        sending = false
    }

    override fun deactivate() {
    }

    override fun activate(p0: LocationSource.OnLocationChangedListener?) {
    }

    override fun onResume() {
        super.onResume()
        mGoogleApiClient?.connect()
    }

    override fun onPause() {
        super.onPause()
        mGoogleApiClient?.disconnect()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    companion object {
        var googleMapFragment: SupportMapFragment? = null
    }

}

