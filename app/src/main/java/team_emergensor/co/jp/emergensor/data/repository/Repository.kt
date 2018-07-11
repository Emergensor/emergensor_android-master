package team_emergensor.co.jp.emergensor.data.repository

import android.content.Context
import es.dmoral.prefs.Prefs
import team_emergensor.co.jp.emergensor.domain.entity.EmergensorUser

/**
 * Created by koichihasegawa on 2018/06/11.
 */
open class Repository(private val context: Context) {
    fun getMyInfoLocal(): EmergensorUser {
        val id = Prefs.with(context).read(EmergensorUserRepository.ID_KEY, "")
        val name = Prefs.with(context).read(EmergensorUserRepository.NAME_KEY, "")
        val picUrl = Prefs.with(context).read(EmergensorUserRepository.PIC_KEY, "")
        return EmergensorUser(id, name, picUrl)
    }
}