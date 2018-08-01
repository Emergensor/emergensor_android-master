package team_emergensor.co.jp.emergensor.service.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.presentation.login.LoginActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    @SuppressLint("NewApi")
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        // カテゴリー名（通知設定画面に表示される情報）
        val name = "通知のタイトル的情報を設定"
        // システムに登録するChannelのID
        val id = "casareal_chanel"


        // 通知設定
        val data = remoteMessage!!.data
        val title = "dangerous detected!"
        val body = data["body"]
        // タップ時の動作設定
        val intent = Intent(this, LoginActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        notificationCompatBuilder.setFullScreenIntent(pendingIntent, false)
        // 通知表示

        // 通知の詳細情報（通知設定画面に表示される情報）
        val notifyDescription = "この通知の詳細情報を設定します"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Channelの取得と生成
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.getAutomaticZenRule(id) == null
            val mChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            mChannel.apply {
                description = notifyDescription
            }
            notificationManager.createNotificationChannel(mChannel)
            val notification = NotificationCompat
                    .Builder(this, id)
                    .apply {
                        setSmallIcon(R.drawable.ic_launcher_background)
                        setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_launcher))
                        setContentTitle("dangerous detected!!")
                        setContentText(body ?: "")
                        setBadgeIconType(R.mipmap.ic_launcher)
                        setDefaults(Notification.DEFAULT_ALL)
                        setAutoCancel(true)
                        setContentIntent(pendingIntent)
                        priority = Notification.PRIORITY_HIGH
                    }.build()
            notificationManager.notify(1, notification)
        } else {
            val notification = NotificationCompat
                    .Builder(this, id)
                    .apply {
                        setSmallIcon(R.drawable.ic_launcher_background)
                        setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_launcher))
                        setContentTitle("dangerous detected!!")
                        setContentText(body ?: "")
                        setBadgeIconType(R.mipmap.ic_launcher)
                        setDefaults(Notification.DEFAULT_ALL)
                        setAutoCancel(true)
                        setContentIntent(pendingIntent)
                        priority = Notification.PRIORITY_HIGH
                    }.build()
            notificationManager.notify(0, notification)
        }
    }

}