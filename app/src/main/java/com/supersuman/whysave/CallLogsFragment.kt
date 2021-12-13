package com.supersuman.whysave

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import java.util.*


class CallLogsFragment : Fragment() {

    private val callsLogsArray : MutableList<CallData> = mutableListOf()
    private lateinit var recyclerView : RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var textView: TextView
    private lateinit var phonenumberEditText: TextInputEditText
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var sharedPref: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initViews()
        initListeners()
        setupRefresh()

        if (isPermissionPresent()){
            getSetCallsLog()
        }else{
            requestPermission()
        }
    }

    private fun initListeners() {
        floatingActionButton.setOnClickListener {
            MaterialAlertDialogBuilder(this.requireContext())
                .setTitle("Reverse list?")
                .setMessage("This will reverse the call list")
                .setPositiveButton("Yes"){ _, _ ->
                    if (sharedPref.contains("ReverseLayout")){
                        val linearManager =LinearLayoutManager(requireActivity().applicationContext,LinearLayoutManager.VERTICAL,false)
                        linearManager.stackFromEnd = false
                        recyclerView.layoutManager = linearManager
                        sharedPref.edit().remove("ReverseLayout").apply()
                    } else{
                        val linearManager =LinearLayoutManager(requireActivity().applicationContext,LinearLayoutManager.VERTICAL,true)
                        linearManager.stackFromEnd = true
                        recyclerView.layoutManager = linearManager
                        sharedPref.edit().putBoolean("ReverseLayout",true).apply()
                    }
                }
                .setNegativeButton("No"){ _, _ ->
                }
                .show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            100 -> {
                if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    getSetCallsLog()
                }
            }
        }
    }

    private fun initViews() {
        sharedPref = requireActivity().getSharedPreferences(
            "WhySave", Context.MODE_PRIVATE)
        phonenumberEditText = requireActivity().findViewById(R.id.phonenumberEditText)
        swipeRefreshLayout = requireActivity().findViewById(R.id.swipeRefreshLayout)
        recyclerView = requireActivity().findViewById(R.id.recyclerView)
        if (!sharedPref.contains("ReverseLayout")){
            val linearManager =LinearLayoutManager(requireActivity().applicationContext,LinearLayoutManager.VERTICAL,false)
            linearManager.stackFromEnd = false
            recyclerView.layoutManager = linearManager
        } else{
            val linearManager =LinearLayoutManager(requireActivity().applicationContext,LinearLayoutManager.VERTICAL,true)
            linearManager.stackFromEnd = true
            recyclerView.layoutManager = linearManager
        }
        recyclerView.adapter = CallLogsRecyclerViewAdapter(callsLogsArray,phonenumberEditText)
        textView = requireActivity().findViewById(R.id.warningTextView)
        floatingActionButton = requireActivity().findViewById(R.id.floatingButton)
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CALL_LOG),100)
    }

    private fun isPermissionPresent(): Boolean {
        return ContextCompat.checkSelfPermission(activity?.applicationContext!!, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED
    }

    private fun openPermissionPage(){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun setupRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            if (!isPermissionPresent()){
                openPermissionPage()
            } else{
                getSetCallsLog()
                Toast.makeText(requireActivity(), "Log refreshed",Toast.LENGTH_SHORT).show()
            }
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun getcallCodes(i : Int): String {
        when(i){
            1->return "Incoming Call"
            2 ->return "Outgoing Call"
            3 ->return "Missed Call"
            4 ->return "Voicemail"
            5->return "Declined Call"
            6->return "Blocked Call"
            7->return "Answered Externally"
        }
        return ""
    }

    private fun getSetCallsLog() {
        textView.visibility = View.GONE
        callsLogsArray.clear()
        val projection = arrayOf(
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.NUMBER,
            CallLog.Calls.TYPE,
            CallLog.Calls.DATE
        )
        val cursor: Cursor? = activity?.applicationContext!!.contentResolver
            .query(CallLog.Calls.CONTENT_URI, projection, null, null, null)
        if (cursor != null) {
            val currentTime = Calendar.getInstance().timeInMillis
            while (cursor.moveToNext()) {
                val name = cursor.getString(0)
                if (name == ""){
                    val number = cursor.getString(1)
                    val type = cursor.getString(2)
                    val time = cursor.getString(3)

                    val callType = getcallCodes(type.toInt())
                    val duration = timeDuration(currentTime - time.toLong())
                    val log = CallData(number, callType, duration)
                    callsLogsArray.add(log)
                }
            }
        }
        cursor?.close()
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun timeDuration(long: Long): String {
        val seconds  = long/1000
        val minutes = seconds/60
        val hours = minutes/60
        val days = hours/24
        val weeks = days/7
        val months = days/30
        val years = months/12
        val timelist = listOf(years.toInt(),months.toInt(),weeks.toInt(),days.toInt(),hours.toInt(),minutes.toInt(),seconds.toInt())
        val abblist = listOf("y","mo","w","d","h","m","s")
        for (i in timelist.indices){
            if (timelist[i]>=1){
                return "${timelist[i]}${abblist[i]} ago"
            }
        }
        return ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_call_logs, container, false)
    }
}