package team_emergensor.co.jp.emergensor.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import team_emergensor.co.jp.emergensor.ui.login.LoginActivity

/**
 * Created by koichihasegawa on 2018/05/27.
 */
open class BaseActivity : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        Log.d("login", "refresh")
        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}