package team_emergensor.co.jp.emergensor.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import team_emergensor.co.jp.emergensor.data.firebase.FirebaseDao
import team_emergensor.co.jp.emergensor.presentation.login.LoginActivity

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    private var isExist = false

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        checkAuth()
    }

    override fun onResume() {
        super.onResume()
        checkAuth()
    }

    private fun checkAuth() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            if (isExist) return
            // FIXME
            val firebaseDao = FirebaseDao(FirebaseAuth.getInstance().currentUser!!)
            firebaseDao.isExistsMyFacebookInfo().subscribe { boolean, e ->
                e?.let {
                    Toast.makeText(this, "ログイン失敗", Toast.LENGTH_SHORT).show()
                }
                isExist = boolean
                if (boolean) {
                    return@subscribe
                } else {
                    Toast.makeText(this, "ないんご", Toast.LENGTH_SHORT).show()

                    return@subscribe
                }
            }
        }
    }
}