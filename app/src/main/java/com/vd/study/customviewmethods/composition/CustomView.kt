package com.vd.study.customviewmethods.composition

import android.content.Context
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.vd.study.customviewmethods.R
import com.vd.study.customviewmethods.databinding.CompositionViewBinding

enum class ButtonsListeners {
    CONFIRM
}

typealias onButtonsActionListener = (ButtonsListeners) -> Unit

class CustomView(
    context: Context,
    attr: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : ConstraintLayout(context, attr, defStyleAttr, defStyleRes) {

    private val binding: CompositionViewBinding

    private var listeners = mutableSetOf<onButtonsActionListener?>()

    private var currentButtonColor: Int = Color.RED

    var isProgressMode: Boolean = false
        set(value) {
            field = value
            setProgressMode(value)
        }

    var editTextValue: String? = ""
        set(value) {
            field = value
            setEditTextValue(value)
        }

    var editTextHint: String? = ""
        set(value) {
            field = value
            setEditTextHint(value)
        }

    var buttonColor: Int? = Color.RED
        set(value) {
            field = value
            setButtonColor(value)
        }

    var buttonText: String? = resources.getString(R.string.confirm)
        set(value) {
            field = value
            setButtonText(value)
        }

    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : this(context, attr, defStyleAttr, 0)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)
    constructor(context: Context) : this(context, null)

    init {
        LayoutInflater.from(context).inflate(R.layout.composition_view, this, true)
        binding = CompositionViewBinding.bind(this)
        initAttributes(attr, defStyleAttr, defStyleRes)
        initListeners()
    }

    @JvmName("setEditTextValue1")
    private fun setEditTextValue(value: String?) {
        binding.etEnter.setText(value)
    }

    @JvmName("setEditTextHint1")
    private fun setEditTextHint(value: String?) {
        binding.etEnter.hint = value
    }

    @JvmName("setButtonColor1")
    private fun setButtonColor(color: Int?) {
        binding.btnConfirm.setBackgroundColor(color ?: Color.RED)
        currentButtonColor = color ?: Color.RED
    }

    @JvmName("setButtonText1")
    private fun setButtonText(text: String?) {
        binding.btnConfirm.text = text
    }

    @JvmName("setLoadingMode")
    private fun setProgressMode(mode: Boolean) = with(binding) {
        when (mode) {
            true -> {
                progressBar.isVisible = true
                etEnter.isInvisible = true
                btnConfirm.isInvisible = true
            }
            else -> {
                progressBar.isVisible = false
                etEnter.isVisible = true
                btnConfirm.isVisible = true
            }
        }
    }

    private fun initAttributes(attr: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attr == null) return
        val typedArray = context.obtainStyledAttributes(attr, R.styleable.CustomView, defStyleAttr, defStyleRes)

        val defaultEditTextValue = typedArray.getString(R.styleable.CustomView_defaultEditTextValue)
        setEditTextValue(defaultEditTextValue)

        val defaultHintValue = typedArray.getString(R.styleable.CustomView_defaultHintValue)
        setEditTextHint(defaultHintValue)

        val buttonColor = typedArray.getColor(R.styleable.CustomView_buttonColor, Color.RED)
        setButtonColor(buttonColor)

        val buttonText = typedArray.getText(R.styleable.CustomView_buttonConfirmText) ?: getDefaultConfirmString()
        setButtonText(buttonText.toString())

        setProgressMode(typedArray.getBoolean(R.styleable.CustomView_loadingMode, false))

        typedArray.recycle()
    }

    private fun getDefaultConfirmString(): String {
        return resources.getString(R.string.confirm)
    }

    fun onButtonClickListener(listener: onButtonsActionListener) {
        this.listeners += listener
    }

    private fun initListeners() = with(binding) {
        btnConfirm.setOnClickListener {
            listeners.forEach {
                it?.invoke(ButtonsListeners.CONFIRM)
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()!!
        val savedState = SavedState(superState)
        savedState.buttonColor = currentButtonColor
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        setButtonColor(savedState.buttonColor)
    }

    class SavedState : BaseSavedState {

        var buttonColor: Int? = null

        constructor(superState: Parcelable) : super(superState)
        constructor(parcel: Parcel): super(parcel) {
            buttonColor = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(buttonColor ?: Color.RED)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(parcel: Parcel): SavedState {
                    return SavedState(parcel)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return Array(size) { null }
                }
            }
        }
    }
}