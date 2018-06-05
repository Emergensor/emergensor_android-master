package team_emergensor.co.jp.emergensor.domain.entity

import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by koichihasegawa on 2018/06/06.
 */
@IgnoreExtraProperties
data class MyFirebaseUser(val firebase_id: String, val name: String)