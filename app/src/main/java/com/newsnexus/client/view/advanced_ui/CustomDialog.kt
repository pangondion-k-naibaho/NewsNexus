package com.newssphere.client.view.advanced_ui

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import com.newsnexus.client.R
import com.newsnexus.client.databinding.PopupNotificationLayoutBinding
import com.newsnexus.client.databinding.PopupQuestionLayoutBinding

interface PopUpNotificationListener{
    fun onClickListener()
}

interface PopUpDialogListener{
    fun onRightButtonClickListener()

    fun onLeftButtonClickListener()
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
    val binding = PopupQuestionLayoutBinding.bind(layoutInflater.inflate(R.layout.popup_question_layout, null))
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
            text = btnTextLeft
            setOnClickListener {
                listener?.onLeftButtonClickListener()
                dialog.dismiss()
            }
        }
        btnRight.apply {
            text = btnTextRight
            setOnClickListener {
                listener?.onRightButtonClickListener()
                dialog.dismiss()
            }
        }
        if(!isFinishing) dialog.show()
    }
}