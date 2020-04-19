package com.sarada.quickwhatsappsender.ui.calllogs

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sarada.quickwhatsappsender.R
import com.sarada.quickwhatsappsender.adapters.CallLogsAdapter
import com.sarada.quickwhatsappsender.models.CallLog

class CallLogFragment : Fragment() {

    companion object {
        fun newInstance() = CallLogFragment()
    }

    private lateinit var callLogViewModel: CallLogViewModel
    private lateinit var callLogsRecyclerView: RecyclerView
    private lateinit var callLogsAdapter: CallLogsAdapter
    private val requestReadLog = 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        callLogViewModel =
            ViewModelProvider(this).get(CallLogViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_call_logs, container, false)

        callLogViewModel.callLogs.observe(
            viewLifecycleOwner,
            Observer { callLogs -> prepareCallLogsView(callLogs, root) })

        requestPermission()

        return root
    }

    private fun requestPermission() {
        context?.let {
            if (
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_CALL_LOG
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                activity?.let { activity ->
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(
                            Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.READ_CONTACTS
                        ),
                        requestReadLog
                    )
                }
            } else {
                callLogViewModel.updateCallData(requireContext())
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == requestReadLog) {
            callLogViewModel.updateCallData(requireContext())
        }
    }

    private fun prepareCallLogsView(
        callLogs: List<CallLog>,
        root: View
    ) {
        root.let {
            callLogsAdapter = CallLogsAdapter(callLogs)

            callLogsRecyclerView = it.findViewById(R.id.rc_call_logs)
            callLogsRecyclerView.layoutManager = LinearLayoutManager(context)
            callLogsRecyclerView.itemAnimator = DefaultItemAnimator()
            callLogsRecyclerView.adapter = callLogsAdapter

        }
    }

}