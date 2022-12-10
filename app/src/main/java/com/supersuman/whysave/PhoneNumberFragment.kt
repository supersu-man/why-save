package com.supersuman.whysave

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.supersuman.whysave.databinding.FragmentPhoneNumberBinding
import kotlin.concurrent.thread


class PhoneNumberFragment : Fragment() {

    private lateinit var binding: FragmentPhoneNumberBinding
    private var countryCode = "91"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPhoneNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addChips()

        binding.myButton.setOnClickListener {
            closeKeyboard()
            val phoneNumber = binding.phonenumberEditText.text.toString()
            binding.chipGroup.checkedChipId
            phoneNumber.replace(" ", "")
            if ("+" in phoneNumber) countryCode = ""
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://api.whatsapp.com/send/?phone=%2B$countryCode$phoneNumber&text&app_absent=0")
            )
            startActivity(browserIntent)
        }

    }

    private fun addChips() = thread {
        val countryCodes = listOf(
            "India+91", "Brazil+55", "USA+1", "Indonesia+62", "Russia+7", "Mexico+52", "Germany+49", "Italy+39"
        )
        for (code in countryCodes) {
            val chip = Chip(requireContext())
            chip.text = code
            chip.isCheckable = true
            chip.isCheckedIconVisible = true
            if ("91" in code) activity?.runOnUiThread { chip.isChecked = true }
            chip.setOnCheckedChangeListener { _, b ->
                if(b) countryCode = code.split("+")[1]
            }
            activity?.runOnUiThread { binding.chipGroup.addView(chip) }
        }
    }

    private fun closeKeyboard() {
        val view: View? = activity?.currentFocus
        if (view != null) {
            val imm: InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}