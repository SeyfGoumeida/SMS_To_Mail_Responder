package com.example.sms_to_mail_responder

import android.content.Context
import android.graphics.Color

class EmailsPrefrence(context: Context){
    val name = "emails_SharedPreference"

    val preference = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    fun getEmails():MutableSet<String>?{
        return preference.getStringSet("selectedMails", null)
    }
    fun setEmails(selectedmails:MutableSet<String>){
        val editor = preference.edit()
        editor.clear()
        editor.commit()
        editor.putStringSet("selectedMails", selectedmails)
        editor.apply()
    }
}