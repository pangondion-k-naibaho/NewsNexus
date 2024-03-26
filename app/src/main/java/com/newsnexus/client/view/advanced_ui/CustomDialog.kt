package com.newssphere.client.view.advanced_ui

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import com.newsnexus.client.R
import com.newsnexus.client.databinding.PopupLayoutBinding
import com.newsnexus.client.databinding.PopupNotificationLayoutBinding
import com.newsnexus.client.databinding.PopupQuestionLayoutBinding

interface PopUpDialogListener_e1{
    fun onClickListener()
}

interface PopUpNotificationListener{
    fun onClickListener()
}

interface PopUpDialogListener{
    fun onRightButtonClickListener()

    fun onLeftButtonClickListener()
}

fun Activity.showPopUpDialog_e1(
    textDesc: String,
    backgroundImage: Int,
    listener: PopUpDialogListener_e1?= null
){
    val dialog = Dialog(this)
    val binding = PopupLayoutBinding.bind(layoutInflater.inflate(R.layout.popup_layout, null))
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setContentView(binding.root)
    dialog.setCancelable(listener == null)
    binding.apply {
        tvPopUp.text = textDesc
        ivPopup.background = ContextCompat.getDrawable(this@showPopUpDialog_e1, backgroundImage)
        btnPopup.setOnClickListener {
            listener?.onClickListener()
            dialog.dismiss()
        }
        if(!isFinishing) dialog.show()
    }
}

fun Activity.showPopupNotification(
    textTitle: String,
    textDesc: String,
    backgroundImage: Int,
    listener: PopUpNotificationListener?= null
){
    val dialog = Dialog(this)
    val binding = PopupNotificationLayoutBinding.bind(layoutInflater.inflate(R.layout.popup_notification_layout, null))
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setContentView(binding.root)
    dialog.setCancelable(listener == null)
    binding.apply {
        tvPopUpTitle.text = textTitle
        tvPopUpDescription.text = textDesc
        ivPopUp.background = ContextCompat.getDrawable(this@showPopupNotification, backgroundImage)
        btnPopup.setOnClickListener {
            listener?.onClickListener()
            dialog.dismiss()
        }
        if(!isFinishing) dialog.show()
    }
}

fun Activity.showPopupDialog(
    textTitle: String,
    textDesc: String,
    backgroundImage: Int,
    btnTextLeft: String,
    btnTextRight: String,
    listener: PopUpDialogListener?= null
){
    val dialog = Dialog(this)
    val binding = PopupQuestionLayoutBinding.bind(layoutInflater.inflate(R.layout.popup_notification_layout, null))
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setContentView(binding.root)
    dialog.setCancelable(listener == null)
    binding.apply {
        tvPopUpTitle.text = textTitle
        tvPopUpDescription.text = textDesc
        ivPopUp.background = ContextCompat.getDrawable(this@showPopupDialog, backgroundImage)
        btnLeft.apply {
            text
            setOnClickListener {
                listener?.onLeftButtonClickListener()
                dialog.dismiss()
            }
        }
        btnRight.setOnClickListener {
            listener?.onRightButtonClickListener()
            dialog.dismiss()
        }
        if(!isFinishing) dialog.show()
    }
}