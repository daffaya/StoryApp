package com.example.storyapp.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.storyapp.R
import java.util.regex.Pattern

class EditText : AppCompatEditText {
    private lateinit var editTextBackground: Drawable
    private lateinit var editTextErrorBackground: Drawable
    private var isError = false
    private var isValidEmail = false
    private var isEmailValidated = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = if (isError) editTextErrorBackground else editTextBackground
        addTextChangedListener(onTextChanged = { text, _, _, _ ->
            if (!TextUtils.isEmpty(text) && text.toString().length < 8 && compoundDrawables[DRAWABLE_RIGHT] != null) {
                error = resources.getString(R.string.minimum_password_character)
                isError = true
            } else {
                error = null
                isError = false
            }
        })
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            validateEmail(s.toString())
        }
    }

    private fun validateEmail(email: String) {
        isValidEmail = isValidEmailFormat(email)
        isEmailValidated = false
    }

    private fun isValidEmailFormat(email: String): Boolean {
        val emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
        return emailPattern.matcher(email).matches()
    }

    private fun init() {
        editTextBackground = ContextCompat.getDrawable(context, R.drawable.bg_edit_text) as Drawable
        editTextErrorBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_edit_text_error) as Drawable
        addTextChangedListener(textWatcher)
    }

    fun isEmailValid(): Boolean {
        if (!isEmailValidated) {
            validateEmail(text.toString())
            isEmailValidated = true
        }
        return isValidEmail
    }

    override fun onDetachedFromWindow() {
        removeTextChangedListener(textWatcher)
        super.onDetachedFromWindow()
    }

    companion object {
        const val DRAWABLE_RIGHT = 2

    }
}
