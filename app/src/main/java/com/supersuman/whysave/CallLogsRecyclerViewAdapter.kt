package com.supersuman.whysave

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText

class CallLogsRecyclerViewAdapter(
    private val mutableList: MutableList<CallData>,
    private val phonenumberEditText: TextInputEditText
) : RecyclerView.Adapter<CallLogsRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val context: Context = view.context
        val number : TextView = view.findViewById(R.id.eachPhoneNumber)
        val type : TextView = view.findViewById(R.id.eachCallType)
        val time : TextView = view.findViewById(R.id.eachCallTime)
        val materialCardView : MaterialCardView = view.findViewById(R.id.eachCallLogCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.each_call_log,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.number.text = mutableList[position].number
        holder.type.text = mutableList[position].type
        holder.time.text = mutableList[position].duration
        holder.materialCardView.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://api.whatsapp.com/send/?phone=%2B${mutableList[position].number}&text&app_absent=0"))
            holder.context.startActivity(browserIntent)
        }
        holder.materialCardView.setOnLongClickListener {
            copyToClipboard(holder.context, mutableList[position].number)
            phonenumberEditText.setText(mutableList[position].number)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }

    private fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Phonenumber",text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context,"Phonenumber copied",Toast.LENGTH_SHORT).show()
    }

}