package com.example.sms_to_mail_responder

import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.sms_to_mail_responder.Contact
import com.example.sms_to_mail_responder.R

class ListAdapter(private val contacts: ArrayList<Contact>, var clickListner: OnItemClickListner):
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.contact_data, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initialize(contacts[position],clickListner)
    }
    class ViewHolder(item: View): RecyclerView.ViewHolder(item){
        var name:TextView = item.findViewById(R.id.name)
        var phone:TextView = item.findViewById(R.id.phone)
        var select:Button = item.findViewById(R.id.selectBtn)

        fun initialize(item:Contact,action:OnItemClickListner){
            name.text=item.name
            phone.text=item.phone

            select.setOnClickListener {
                action.OnSelectItemClick(item,adapterPosition,name,phone,select)
            }

        }
    }
    interface OnItemClickListner{

        fun OnSelectItemClick(item:Contact , Position:Int,name : TextView,phone: TextView,select:Button){

        }
    }
}