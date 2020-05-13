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
import java.util.Currency.getInstance
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class SMSReceiver: BroadcastReceiver() {

    private val channelId = "channel-01"
    private val channelName = "SIL Channel"
    private val importance = NotificationManager.IMPORTANCE_HIGH
    var phoneNumber :String? = null
    lateinit var messageText :String
    lateinit var appExecutors: AppExecutors
    lateinit var mainActivity : MainActivity

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

        val testIntent = Intent(context, MainActivity::class.java)
        val pTestIntent = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), testIntent, 0)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                channelId, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)

        }



        //sendEmail("goumeidaahmedseyfeddine@gmail.com")
        //---------------------send mail function------------------------
        val emailsPreference = EmailsPrefrence(context)
        val emails = emailsPreference.getEmails()
        if (emails != null) {
            for (i in emails){
                sendEmail(i)
                val notif = Notification.Builder(context, channelId)
                    .setContentTitle("SMS To eMail")
                    .setContentText("This app sent an email after receiving a message")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentIntent(pTestIntent)
                    .setAutoCancel(true)
                    .build()
                notificationManager.notify(0, notif)

            }
        }


    }
    private fun sendEmail(email :String){
        appExecutors = AppExecutors()
        appExecutors.diskIO().execute {
            val props = System.getProperties()
            props.put("mail.smtp.host", "smtp.gmail.com")
            props.put("mail.smtp.socketFactory.port", "465")
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            props.put("mail.smtp.auth", "true")
            props.put("mail.smtp.port", "465")

            val session =  Session.getInstance(props,
                object : javax.mail.Authenticator() {
                    //Authenticating the password
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(Credentials.EMAIL, Credentials.PASSWORD)
                    }
                })

            try {
                //Creating MimeMessage object
                val mm = MimeMessage(session)
                //Setting sender address
                mm.setFrom(InternetAddress(Credentials.EMAIL))
                //Adding receiver
                mm.addRecipient(
                    Message.RecipientType.TO,
                    InternetAddress(email))
                //Adding subject
                mm.subject = "APP TEST"
                //Adding message
                mm.setText("This is a message from the app that reply to SMS by Emails :) !")
                //Sending email
                Transport.send(mm)
                appExecutors.mainThread().execute {
                    //Something that should be executed on main thread.
                }

            } catch (e: MessagingException) {
                e.printStackTrace()
            }
        }
    }

}
