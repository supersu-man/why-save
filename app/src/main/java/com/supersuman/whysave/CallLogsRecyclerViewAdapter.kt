package com.supersuman.whysave

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.supersuman.whysave.databinding.EachCallLogBinding
import com.wickerlabs.logmanager.LogObject
import java.util.Calendar

class CallLogsRecyclerViewAdapter(private val mutableList: MutableList<LogObject>?) :
    RecyclerView.Adapter<CallLogsRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(val binding: EachCallLogBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EachCallLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mutableList == null) return
        holder.binding.eachPhoneNumber.text = mutableList[position].contactName
        holder.binding.eachCallType.text = getcallType(mutableList[position].type)
        holder.binding.eachCallTime.text = timeDuration(mutableList[position].date)
        holder.binding.eachCallLogCard.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://api.whatsapp.com/send/?phone=%2B${mutableList[position].number}&text&app_absent=0")
            )
            holder.binding.root.context.startActivity(browserIntent)
        }
        holder.binding.eachCallLogCard.setOnLongClickListener {
            copyToClipboard(holder.binding.root.context, mutableList[position].number)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int { return mutableList?.size ?: 0 }

    private fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Phonenumber", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Phonenumber copied", Toast.LENGTH_SHORT).show()
    }

    private fun getcallType(i: Int): String {
        when (i) {
            1 -> return "Incoming"
            2 -> return "Outgoing"
            3 -> return "Missed"
            4 -> return "Voicemail"
            5 -> return "Declined"
            6 -> return "Blocked"
            7 -> return "Answered Externally"
        }
        return ""
    }

    private fun timeDuration(long: Long): String {
        val cal  = Calendar.getInstance().timeInMillis
        val diff = cal - long
        val seconds  = diff/1000
        val minutes = seconds/60
        val hours = minutes/60
        val days = hours/24
        val weeks = days/7
        val months = days/30
        val years = months/12
        val timelist = listOf(years,months,weeks,days,hours,minutes,seconds)
        val abblist = listOf("y","mo","w","d","h","m","s")
        for (i in timelist.indices){
            if (timelist[i].toInt()>=1){
                return "${timelist[i].toInt()}${abblist[i]} ago"
            }
        }
        return ""
    }

}