package team_emergensor.co.jp.emergensor.ui

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
//        if (FirebaseAuth.getInstance().currentUser == null) {
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//        }
    }
}