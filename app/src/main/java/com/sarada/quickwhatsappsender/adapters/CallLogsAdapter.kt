package com.sarada.quickwhatsappsender.adapters

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sarada.quickwhatsappsender.R
import com.sarada.quickwhatsappsender.models.CallLog
import kotlinx.android.synthetic.main.item_call_log.view.*

class CallLogsAdapter(private val callLogs: List<CallLog>?) :
    RecyclerView.Adapter<CallLogsAdapter.ViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.context = parent.context
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_call_log,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = callLogs!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val callLog = callLogs!![position]
        with(holder) {
            callDetailsTextView1.text = callLog.number
            contactedDate.text = callLog.dayTime

            if (callLog.count == 1) {
                callLogCounter.visibility = View.GONE
            } else {
                callLogCounter.visibility = View.VISIBLE
                callLogCounter.text = String.format("(%d)", callLog.count)
            }

            if (callLog.number != callLog.name) {
                callDetailsTextView2.visibility = View.VISIBLE
                callDetailsTextView1.text = callLog.name
                callDetailsTextView2.text = callLog.number
            } else {
                callDetailsTextView2.visibility = View.INVISIBLE
            }

         //   contactIconImageView.visibility = View.INVISIBLE
            when (callLog.type) {
                "Incoming" -> {
                    incomingImageView.visibility = View.VISIBLE
                    outgoingImageView.visibility = View.INVISIBLE
                    missedImageView.visibility = View.INVISIBLE
                }
                "Outgoing" -> {
                    incomingImageView.visibility = View.INVISIBLE
                    outgoingImageView.visibility = View.VISIBLE
                    missedImageView.visibility = View.INVISIBLE
                }
                "Missed" -> {
                    incomingImageView.visibility = View.INVISIBLE
                    outgoingImageView.visibility = View.INVISIBLE
                    missedImageView.visibility = View.VISIBLE
                }
                else -> {
                    incomingImageView.visibility = View.VISIBLE
                    outgoingImageView.visibility = View.INVISIBLE
                    missedImageView.visibility = View.INVISIBLE
                }
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contactIconImageView: ImageView = itemView.ic_call_contact
        val callDetailsTextView1: TextView = itemView.lbl_call_desc_1
        val callDetailsTextView2: TextView = itemView.lbl_call_desc_2
        val incomingImageView: ImageView = itemView.ic_call_ic
        val outgoingImageView: ImageView = itemView.ic_call_og
        val missedImageView: ImageView = itemView.ic_call_missed
        val contactedDate: TextView = itemView.lbl_contacted_time
        val callLogCounter: TextView = itemView.call_log_count
    }

}