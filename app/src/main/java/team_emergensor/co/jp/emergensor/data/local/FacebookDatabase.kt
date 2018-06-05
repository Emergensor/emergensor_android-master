package team_emergensor.co.jp.emergensor.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import team_emergensor.co.jp.emergensor.data.local.dao.MyFacebookInfoDao
import team_emergensor.co.jp.emergensor.domain.entity.MyFacebookInfo

@Database(entities = [MyFacebookInfo::class], version = 1)
abstract class FacebookDatabase : RoomDatabase() {
    abstract fun facebookMyInfoDao(): MyFacebookInfoDao
}