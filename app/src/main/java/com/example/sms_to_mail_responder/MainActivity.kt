package com.example.sms_to_mail_responder

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.ArraySet
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class MainActivity : AppCompatActivity() , ListAdapter.OnItemClickListner {
private val requestReceiveSms = 2
    lateinit var submit : Button
    lateinit var loadbtn : Button
    lateinit var listContacts : TextView
    lateinit var appExecutors: AppExecutors

    var selectedContacts: ArrayList<Contact> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //-------------contacts in sharedprefrences-------------
        var recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS),requestReceiveSms)
        }
        submit=findViewById(R.id.Submit)
        loadbtn=findViewById(R.id.LoadBtn)
        listContacts=findViewById(R.id.listConstacts)
        var contacts: MutableList<Contact> = ArrayList()

        loadbtn.setOnClickListener(){

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

                    listContacts.text="permission denied"

                } else {
                    listContacts.text="permission allowed "

                    // No explanation needed, we can request the permission.

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                    var recyclerView: RecyclerView = findViewById(R.id.recyclerView)
                    recyclerView.layoutManager = LinearLayoutManager(this)

                    var list = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                    )

                    if (list != null) {
                        while (list.moveToNext()) {
                            val name =
                                list.getString(list.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                            val phone =
                                list.getString(list.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                            //---------------email ---------------------

                            var email :String = " "
                            val contactId= list.getString(list.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID))
                            val emails: Cursor? = contentResolver.query(Email.CONTENT_URI, null, Email.CONTACT_ID + " = " + contactId, null, null)
                            if (emails != null) {
                                while (emails.moveToNext()) {
                                    email = emails.getString(emails.getColumnIndex(Email.DATA))
                                    break
                                }
                            }

                            if (email.isNullOrEmpty()){email=" df"}
                            var contact = Contact(name, phone,email)
                            contacts.add(contact)

                        }
                        recyclerView.adapter = ListAdapter(contacts as ArrayList<Contact>,this)
                        list.close()

                    }
                }
            }
        }
        loadbtn.performClick()


        submit.setOnClickListener() {
            var recyclerView: RecyclerView = findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = ListAdapter(selectedContacts,this)
            var emails :MutableSet<String> = ArraySet()
            for(i in selectedContacts){
                    emails.add(i.email)
            }

             if (emails != null) {
                 val emailsPreference = EmailsPrefrence(this)
                 emailsPreference.setEmails(emails)
                }
        }

    }

    //-----------------------------------------------------------------
    override fun OnSelectItemClick(item:Contact , Position:Int,name : TextView,phone: TextView,email :TextView,select:Button){

        if(!selectedContacts.contains(item)) {
            if (item.email!=" "){
            selectedContacts.add(item)
            }
        }
        select.text="Selected"
        select.visibility = View.GONE
    }


}

