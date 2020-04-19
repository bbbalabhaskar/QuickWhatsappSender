package com.sarada.quickwhatsappsender.ui.dialpad

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sarada.quickwhatsappsender.R


class DialPadFragment : Fragment() {

    private lateinit var dialPadViewModel: DialPadViewModel
    private lateinit var mobileNumberTextEditText: EditText
    private lateinit var ctryCodeEditText: EditText
    private lateinit var sendBtn: Button

    companion object {
        fun newInstance() = DialPadFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialPadViewModel = ViewModelProvider(this).get(DialPadViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dial_pad, container, false)

        initViews(root)
        addHandlers()

        return root
    }

    private fun initViews(root: View) {
        mobileNumberTextEditText = root.findViewById(R.id.mobile_number_text)
        ctryCodeEditText = root.findViewById(R.id.country_code_text)
        sendBtn = root.findViewById(R.id.btn_dial_pad_send_message)
    }

    private fun addHandlers() {
        mobileNumberTextEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                initCtryCode()
                s?.toString()?.let { dialPadViewModel.setMobileNumber(it) }
            }
        })

        mobileNumberTextEditText.setOnLongClickListener {
            val clipboardManager: ClipboardManager =
                context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.primaryClip?.let { clipData: ClipData ->
                clipData.getItemAt(0)?.let { clipDataItem ->
                    clipDataItem.text?.let {
                        setMobileNumber(it.toString())
                    }
                }
            }
            true
        }

        sendBtn.setOnClickListener {
            if (dialPadViewModel.mobileNumber.value.toString().length == 10) {
                val uri = dialPadViewModel.getUri()
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(uri))

                startActivity(browserIntent)
            }

        }
    }

    private fun setMobileNumber(mobileNumber: String) {
        if (mobileNumber.matches(Regex("""[0-9]{10}"""))) {
            initCtryCode()
            mobileNumberTextEditText.setText(mobileNumber)
        } else if (mobileNumber.matches(Regex("""(\+)[0-9]{12}"""))) {
            ctryCodeEditText.setText(mobileNumber.substring(1, 3))
            mobileNumberTextEditText.setText(mobileNumber.substring(3))
        }
    }

    private fun initCtryCode() {
        if (TextUtils.isEmpty(ctryCodeEditText.text)) {
            ctryCodeEditText.setText(getString(R.string.default_ctry_code))
        }
    }
}