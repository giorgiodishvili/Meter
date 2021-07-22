package com.example.meter.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputType
import android.view.MotionEvent
import android.widget.EditText
import androidx.core.content.ContextCompat


@SuppressLint("ClickableViewAccessibility")
fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
    this.setOnTouchListener { v, event ->
        var hasConsumed = false
        if (v is EditText) {
            if (event.x >= v.width - v.totalPaddingRight) {
                if (event.action == MotionEvent.ACTION_UP) {
                    onClicked(this)
                }
                hasConsumed = true
            }
        }
        hasConsumed
    }
}

fun EditText.setReadOnly(value: Boolean, inputType: Int = InputType.TYPE_NULL) {
    isFocusable = !value
    isFocusableInTouchMode = !value
    this.inputType = inputType
}

fun EditText.setDrawableEnd(context: Context, drawable: Int) {
    setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, drawable), null)


}

fun EditText.removeDrawableEnd() {
    setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

}