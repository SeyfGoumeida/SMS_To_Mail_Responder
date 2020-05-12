package com.example.sms_to_mail_responder

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() , ListAdapter.OnItemClickListner {

    lateinit var sendbtn : Button
    lateinit var loadbtn : Button
    lateinit var listContacts : TextView
    var selectedContacts: ArrayList<Contact> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sendbtn=findViewById(R.id.SendBtn)
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

                            var contact = Contact(name, phone)
                            contacts.add(contact)
                        }
                        recyclerView.adapter = ListAdapter(contacts as ArrayList<Contact>,this)
                        list.close()
                    }
                }
            }
        }

        sendbtn.setOnClickListener() {
            var recyclerView: RecyclerView = findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = ListAdapter(selectedContacts ,this)
        }

    }
    override fun OnSelectItemClick(item:Contact , Position:Int,name : TextView,phone: TextView,select:Button){
        selectedContacts.add(item)
        select.text="Selected"

    }

}

