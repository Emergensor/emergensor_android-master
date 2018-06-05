package team_emergensor.co.jp.emergensor.domain.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties

@Entity
data class MyFacebookInfo(@PrimaryKey val id: String, val name: String)