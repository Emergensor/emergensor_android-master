package team_emergensor.co.jp.emergensor.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import team_emergensor.co.jp.emergensor.domain.entity.MyFacebookInfo

@Dao
interface MyFacebookInfoDao {

    @Query("SELECT * FROM myFacebookInfo ORDER BY id LIMIT 1")
    fun get(): MyFacebookInfo?

    @Insert
    fun insert(myFacebookInfo: MyFacebookInfo)

}