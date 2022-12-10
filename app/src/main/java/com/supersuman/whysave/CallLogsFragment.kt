package com.supersuman.whysave

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.supersuman.whysave.databinding.FragmentCallLogsBinding
import com.wickerlabs.logmanager.LogsManager
import kotlin.concurrent.thread


class CallLogsFragment : Fragment() {

    private lateinit var binding: FragmentCallLogsBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCallLogsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isEmpty()) return
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            getSetCallLogs()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPref = requireActivity().getSharedPreferences("WhySave", Context.MODE_PRIVATE)
        binding.recyclerView.adapter = CallLogsRecyclerViewAdapter(mutableListOf())
        setupRefresh()
        if (isPermissionsPresent()) {
            getSetCallLogs()
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS), 100)
        }
    }

    private fun getSetCallLogs() = thread {
        val logsManager = LogsManager(context)
        val callLogs = logsManager.getLogs(LogsManager.ALL_CALLS)
        activity?.runOnUiThread {
            binding.recyclerView.adapter = CallLogsRecyclerViewAdapter(callLogs)
        }
    }

    private fun isPermissionsPresent(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.READ_CALL_LOG
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun openPermissionPage() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun setupRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            if (isPermissionsPresent()) {
                getSetCallLogs()
                Toast.makeText(requireActivity(), "Log refreshed", Toast.LENGTH_SHORT).show()
            } else {
                openPermissionPage()
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

}