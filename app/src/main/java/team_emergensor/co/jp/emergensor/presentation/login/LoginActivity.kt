package team_emergensor.co.jp.emergensor.presentation.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
import io.reactivex.schedulers.Schedulers
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.data.firebase.FirebaseDao
import team_emergensor.co.jp.emergensor.data.repository.FacebookRepository
import team_emergensor.co.jp.emergensor.presentation.home.HomeActivity


class LoginActivity : AppCompatActivity() {

    private val callbackManager = CallbackManager.Factory.create()
    private val facebookRepository = FacebookRepository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Facebook Login button
        val loginButton = findViewById<LoginButton>(R.id.login_button)
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


        // if user already login and exist in firebase, intent to map
        FirebaseAuth.getInstance().currentUser?.let {
            if (FacebookRepository(this).isExistUserInFirebase()) {
                Toast.makeText(applicationContext, "hello, ${it.displayName}", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(intent)
            } else {
                facebookRepository.getMyInfo()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { t1, t2 ->
                            t2?.let {
                                return@subscribe
                            }
                            val firebaseDao = FirebaseDao(it)
                            firebaseDao.setMyFacebookInfo(t1)
                            facebookRepository.setExistingInFirebase(true)
                            Toast.makeText(applicationContext, "hello, ${it.displayName}", Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext, HomeActivity::class.java)
                            startActivity(intent)
                        }
            }
        }

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
                        val firebaseDao = FirebaseDao(task.result.user)
                        facebookRepository.getMyInfo().subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe { t1, t2 ->
                                    firebaseDao.setMyFacebookInfo(t1)
                                }
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "login failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT).show()
                    }
                }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}
