package com.newsnexus.client.view.advanced_ui

import android.content.Context
import android.text.Editable
import android.text.Html
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.newsnexus.client.R
import com.newsnexus.client.databinding.InputtextLayoutBinding
import com.newsnexus.client.model.Extensions.Companion.dpToPx

class InputTextView: ConstraintLayout {
    private lateinit var mContext: Context
    private lateinit var binding: InputtextLayoutBinding

    private var listener: InputViewListener?= null
    private var retrievedInputType: INPUT_TYPE?= null

    constructor(context: Context): super(context){
        init(context, null)
    }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet){
        init(context, attributeSet)
    }

    enum class INPUT_TYPE{
        REGULER, EMAIL, PASSWORD, MULTILINE
    }

    private fun init(context: Context, attrs: AttributeSet?){
        mContext = context

        binding = InputtextLayoutBinding.bind(
            LayoutInflater.from(mContext)
                .inflate(R.layout.inputtext_layout, this, true)
        )


        binding.etInput.apply {
            addTextChangedListener(textWatcher)
        }
        binding.ivAction.setOnClickListener { listener?.onClickReveal() }
    }

    private val textWatcher = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if(binding.etInput.text.toString().length > 0){
                binding.etInput.apply {
                    background = ContextCompat.getDrawable(mContext, R.drawable.bg_rectangle_satin_white)
                    setTextColor(ContextCompat.getColor(mContext, R.color.astrochopus_grey))
                }
            }
        }

    }

    fun setTitle(inputTitle: String){
//        val titleText =
        binding.tvTitle.text = HtmlCompat.fromHtml(inputTitle, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    fun getInputType(): String{
        return binding.etInput.inputType.toString()
    }

    fun setListener(inputViewListener: InputViewListener?){
        listener = inputViewListener
    }

    fun setInputType(typeInput: INPUT_TYPE){
        retrievedInputType = typeInput
        binding.etInput.apply {
            val drawable = when(retrievedInputType){
                INPUT_TYPE.EMAIL ->{
                    ContextCompat.getDrawable(mContext, R.drawable.ic_email)
                }
                INPUT_TYPE.PASSWORD ->{
                    ContextCompat.getDrawable(mContext, R.drawable.ic_password)
                }
                else ->{
                    null
                }
            }

            setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

            inputType = when(retrievedInputType){
                INPUT_TYPE.EMAIL -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                INPUT_TYPE.PASSWORD -> InputType.TYPE_TEXT_VARIATION_PASSWORD
                INPUT_TYPE.MULTILINE -> InputType.TYPE_TEXT_FLAG_MULTI_LINE
                else -> InputType.TYPE_CLASS_TEXT
            }
            if(retrievedInputType == INPUT_TYPE.PASSWORD) transformationMethod = PasswordTransformationMethod.getInstance()

            if(retrievedInputType == INPUT_TYPE.MULTILINE){
                val updatedLayoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    85.dpToPx(mContext) // Convert dp to pixels
                )
                layoutParams = updatedLayoutParams
            }
        }
        binding.ivAction.apply {
            visibility = when(retrievedInputType){
                INPUT_TYPE.EMAIL -> View.GONE
                INPUT_TYPE.PASSWORD -> View.VISIBLE
                else -> View.GONE
            }
        }
    }

    fun revealPassword(){
        if(binding.etInput.transformationMethod == PasswordTransformationMethod.getInstance()){
            binding.etInput.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }else{
            binding.etInput.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        binding.etInput.setSelection(binding.etInput.text.length)
    }

    fun getText(): String{
        return binding.etInput.text.toString()
    }

    fun setTextHelper(text: String){
        binding.etInput.hint = text
    }

    fun setOnError(){
        binding.etInput.apply {
            background = ContextCompat.getDrawable(mContext, R.drawable.bg_rectangle_red_white)
            setTextColor(ContextCompat.getColor(mContext, R.color.red))
        }
    }

    interface InputViewListener{
        fun onClickReveal()
    }
}