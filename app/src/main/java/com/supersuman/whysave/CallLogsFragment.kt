package com.supersuman.whysave

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton


class CallLogsFragment : Fragment() {

    private lateinit var button: MaterialButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initListeners()
        button.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/supersu-man/WhySave/releases/latest"))
            startActivity(browserIntent)
        }
    }

    private fun initListeners() {
        button = requireActivity().findViewById(R.id.button)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_call_logs, container, false)
    }
}