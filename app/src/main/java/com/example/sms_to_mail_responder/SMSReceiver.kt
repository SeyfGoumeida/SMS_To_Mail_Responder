package com.example.sms_to_mail_responder

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.widget.Toast
import androidx.annotation.RequiresApi

class SMSReceiver: BroadcastReceiver() {

    private val channelId = "channel-01"
    private val channelName = "SIL Channel"
    private val importance = NotificationManager.IMPORTANCE_HIGH
    var phoneNumber :String? = null
    lateinit var messageText :String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val extras = intent.extras

        if (extras!= null){
            val sms = extras.get("pdus") as Array<Any>
            for (i in sms.indices){
                val format =extras.getString("format")
                var smsMessage= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    SmsMessage.createFromPdu(sms[i]as ByteArray,format)
                }else{
                    SmsMessage.createFromPdu(sms[i]as ByteArray)
                }
                 phoneNumber = smsMessage.originatingAddress
                 messageText = smsMessage.messageBody.toString()
                Toast.makeText(context,"Phone Number : (private)\n"+"MessageText: $messageText",Toast.LENGTH_SHORT).show()
            }
        }
      Toast.makeText(context,"SMS Received",Toast.LENGTH_LONG).show()


        val testIntent = Intent(context, MainActivity::class.java)
        val pTestIntent = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), testIntent, 0)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                channelId, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)

        }
        val noti = Notification.Builder(context, channelId)
            .setContentTitle("$phoneNumber Sent You A Message")
            .setContentText(messageText)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pTestIntent)
            .setAutoCancel(true)

            .build()

        notificationManager.notify(0, noti)

    }
}
