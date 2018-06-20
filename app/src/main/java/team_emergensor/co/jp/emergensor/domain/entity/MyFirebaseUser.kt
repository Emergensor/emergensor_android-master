package team_emergensor.co.jp.emergensor.domain.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class MyFirebaseUser(val firebase_id: String, val name: String, val picture: String)