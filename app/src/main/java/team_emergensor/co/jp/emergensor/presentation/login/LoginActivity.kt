package team_emergensor.co.jp.emergensor.presentation.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.data.firebase.FirebaseFacebookUserDao
import team_emergensor.co.jp.emergensor.data.repository.EmergensorUserRepository
import team_emergensor.co.jp.emergensor.presentation.home.HomeActivity


class LoginActivity : AppCompatActivity() {

    private val callbackManager by lazy {
        CallbackManager.Factory.create()
    }
    private val emergensorUserRepository by lazy {
        EmergensorUserRepository(this)
    }
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Facebook Login button
        val loginButton: LoginButton = findViewById(R.id.login_button)
        loginButton.apply {
            setReadPermissions("email", "public_profile", "user_friends")
            registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(applicationContext, "ログイン失敗", Toast.LENGTH_SHORT).show()
                }
            })
        }

        checkPermission()

    }

    private fun handleFacebookAccessToken(token: AccessToken) {

        val credential = FacebookAuthProvider.getCredential(token.token)
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(applicationContext, "login success", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
                        val firebaseDao = FirebaseFacebookUserDao()
                        val disposable = emergensorUserRepository.fetchMyInfo().subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe { t1, t2 ->
                                    firebaseDao.setMyFacebookInfo(t1)
                                    finish()
                                }
                        compositeDisposable.add(disposable)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "login failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnCanceledListener {
                }
                .addOnFailureListener {
                }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    // 位置情報許可の確認
    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission()
        } else {
            autoLogin()
        }
    }

    private fun autoLogin() {
        if (!intent.getBooleanExtra(IS_AUTO_START, true)) return
        // if user already login and exist in firebase, intent to map
        FirebaseAuth.getInstance().currentUser?.let {
            if (emergensorUserRepository.isUserExistInFirebase()) {
                Toast.makeText(applicationContext, "hello, ${it.displayName}", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val disposable = emergensorUserRepository.getMyInfoWithAsync()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { t1, t2 ->
                            t2?.let {
                                Log.d("login activity", it.message)
                                return@subscribe
                            }
                            val firebaseDao = FirebaseFacebookUserDao()
                            firebaseDao.setMyFacebookInfo(t1)
                            emergensorUserRepository.setUserExistingInFirebase(true)
                            Toast.makeText(applicationContext, "hello, ${it.displayName}", Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                compositeDisposable.add(disposable)
            }
        }
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION)
        } else {
            val toast = Toast.makeText(this,
                    "GPS permission rejected", Toast.LENGTH_SHORT)
            toast.show()
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        autoLogin()
    }

    companion object {
        const val IS_AUTO_START = "is auto start"
        const val REQUEST_PERMISSION = 10
    }
}
