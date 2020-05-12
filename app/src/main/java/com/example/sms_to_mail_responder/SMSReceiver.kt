package com.example.sms_to_mail_responder

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class SMSReceiver: BroadcastReceiver() {

    private val channelId = "01"
    private val channelName = "MyChannel"
    private val importance = NotificationManager.IMPORTANCE_HIGH

    override fun onReceive(context: Context, intent: Intent) {
      Toast.makeText(context,"SMS Received",Toast.LENGTH_LONG).show()


        var titre = ""
        var contenu = ""
        if (intent.action === "dz.esi.demobroadcast") {
            titre = "Un titre"
            contenu = "Diffusion"
        } else {
            titre = "Un appel"
            contenu = "Appel suivi"
        }
        val pIntent = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), intent, 0)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                channelId, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)
            val noti = Notification.Builder(context,"ch00")
                .setContentTitle(titre)
                .setContentText(contenu).setSmallIcon(android.R.drawable.btn_star)
                .setContentIntent(pIntent).setAutoCancel(true)

                .build()
            notificationManager.notify(0, noti)

        }else{

            val noti = Notification.Builder(context)
                .setContentTitle(titre)
                .setContentText(contenu).setSmallIcon(android.R.drawable.btn_star)
                .setContentIntent(pIntent).setAutoCancel(true)

                .build()
            notificationManager.notify(0, noti)

        }

    }
}
